# Testing Spring Boot Applications

This page describes how to write tests using the framework support in Spring Boot 1.4.
It will cover unit tests that can run in isolation as well as integration tests
that will bootstrap Spring context before executing tests.

## Maven Dependencies

The ```spring-boot-starter-test``` is the primary dependency that contains the majority of
elements required for our tests.

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```


With the ```spring-boot-starter-test```, the following libraries will be provided.

* **JUnit** — The de-facto standard for unit testing Java applications.
* **Spring Test & Spring Boot Test** — Utilities and integration test support for Spring Boot applications.
* **AssertJ** — A fluent assertion library.
* **Hamcrest** — A library of matcher objects (also known as constraints or predicates).
* **Mockito** — A Java mocking framework.
* **JSONassert** — An assertion library for JSON.
* **JsonPath** — XPath for JSON.

## Unit Tests vs. Integration Tests

### Unit Tests
A **unit test** is a test written by the programmer to verify that a relatively small piece
of code is doing what it is intended to do. They are narrow in scope, they should be
easy to write and execute, and their effectiveness depends on what the programmer considers
to be useful. The tests are intended for the use of the programmer, they are not directly
useful to anybody else, though, if they do their job, testers and users downstream should
benefit from seeing fewer bugs.

Part of being a unit test is the implication that things outside the code under test are
mocked or stubbed out. Unit tests shouldn't have dependencies on outside systems. They test
internal consistency as opposed to proving that they play nicely with some outside system.

### Integration Tests

An **integration test** is done to demonstrate that different pieces of the system work together.
Integration tests cover whole applications, and they require much more effort to put together.
They usually require resources like database instances and hardware to be allocated for them.
The integration tests do a more convincing job of demonstrating the system works (especially
to non-programmers) than a set of unit tests can, at least to the extent the integration test
environment resembles production.

Actually "integration test" gets used for a wide variety of things, from full-on system tests
against an environment made to resemble production to any test that uses a resource (like a
database or queue) that isn't mocked out.

> __Note:__
Ideally, we should keep the integration tests separated from the unit tests and should not
run along with the unit tests. We can do that by using a different profile to only run the
integration tests. A couple of reasons for doing this could be that the integration tests
are time-consuming and might need an actual database to execute.


## Unit Testing with JUnit

The following code is a standard test case using JUnit 4. At least one ```@Test``` is required,
and ```@BeforeClass```, ```@Before```, ```@After``` and ```@AfterClass``` are all optional.

```
public class SomeClassTest {

    @BeforeClass
    public static void setUpBeforeAll() throws Exception {
        // Set up once before all tests
    }

    @Before
    public void setUp() throws Exception {
        // Set up before each test
    }

    @Test
    public void testMethod1() {
        // Test method 1
    }

    @Test
    public void testMethod2() {
        // Test method 2
    }

    @After
    public void tearDown() throws Exception {
        // Tear down after each test
    }

    @AfterClass
    public static void tearDownAfterAll() throws Exception {
        // Tear down once after all tests
    }
}
```


## Integration Testing with @SpringBootTest

Spring Boot offers a lot of support for writing integration tests for your application.
First of all, you can use the new ```@SpringBootTest``` annotation. This annotation configures
a complete test environment with all your beans and everything set up.
You can choose to start a mock servlet environment, or even start a real one with the
```webEnvironment``` attribute of the ```@SpringBootTest``` annotation.

A typical Spring Boot 1.4 integration test will look like this:

```
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class MyTest {

    // write test cases here

}
```

* **```@RunWith(SpringRunner.class)```** tells JUnit to run using Spring's testing support.
* **```@SpringBootTest```** is saying "bootstrap with Spring Boot's support" (e.g. load
```application.properties``` and give me all the Spring Boot goodness).
* **```@TestPropertySource```** can be used to configure locations of properties files specific
to the tests. The property file loaded with ```@TestPropertySource``` will override the existing
```application.properties``` file.



## Testing JPA Repositories with @JpaDataTest

To test the persistence layer with ```@JpaDataTest```, the following dependency is required.
The H2 DB is an in-memory database that eliminates the need for configuring and starting
an actual database for test purposes.

```
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

```@DataJpaTest``` provides some standard setup needed for testing the persistence layer:

* configuring H2, an in-memory database
* setting Hibernate, Spring Data, and the DataSource
* performing an ```@EntityScan```
* turning on SQL logging

To carry out some DB operation, we need some records already setup in our database.
To setup such data, we can use ```TestEntityManager```. The ```TestEntityManager```
provided by Spring Boot is an alternative to the standard JPA EntityManager that provides
methods commonly used when writing tests.

Example:

```
@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void setUp() throws Exception {
        Product product = new Product();
        product.setCode("P001");
        product.setName("iPhone 7");
        product.setDescription("This is an Apple phone.");
        product.setActive(true);
        entityManager.persist(product);
    }

    @Test
    public void findByCode() {
        Product product = productRepository.findByCode("P001");
        assertNotNull(product);
        assertEquals("P001", product.getCode());
        assertEquals("iPhone 7", product.getName());
        assertEquals("This is an Apple phone.", product.getDescription());
        assertEquals(true, product.isActive());
    }

    @Test
    public void findByCode_NotFound() {
        Product product = productRepository.findByCode("P003");
        assertNull(product);
    }
}
```

## Testing Mongo Repositories with @MongoDataTest

To test the persistence layer with ```@MongoDataTest```, the following dependency is required.

```
<dependency>
    <groupId>de.flapdoodle.embed</groupId>
    <artifactId>de.flapdoodle.embed.mongo</artifactId>
    <scope>test</scope>
</dependency>
```

Example:

```

```

## Testing Services with @MockBean

Spring Boot includes a ```@MockBean``` annotation that can be used to define a Mockito mock
for a bean inside your ```ApplicationContext```. You can use the annotation to add new beans,
or replace a single existing bean definition. Mock beans are automatically reset after each
test method.

Example:

```

```

## Testing RestControllers with @WebMvcTest



## Naming Conventions

### Naming TestCase Classes

As each class has its own test class, name the TestCase class after the class being tested,
following the naming conventions below.

* **{ClassName}Test**
* **{ClassName}Tests** — Only for multi-featured integration tests or test suites.

Examples:

* ProductService — A target class under test
* ProductServiceTest — A test case class

> __Note:__
It is recommended to locate a test class in the same package with the target class,
so that it will help minimize the access level of test methods.

### Naming Test Methods

Test method names should follow one of the naming conventions below.

* **MethodName(_ExpectedBehavior(_StateUnderTest))**
* **test{FeatureBeingTested}**

Examples:

* postMessage_InternalServerError
* withdrawMoney_ThrowsException_IfAccountIsInvalid
* testCRUD


## References

* https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html
* https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4
* http://www.baeldung.com/spring-boot-testing
* https://stackoverflow.com/questions/5357601/whats-the-difference-between-unit-tests-and-integration-tests
* https://vitalflux.com/7-popular-unit-test-naming-conventions/
