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
```@SpringBootTest``` is an annotation that can be specified on a test class that runs
Spring Boot based tests. This annotation configures a complete test environment with all
your beans and everything set up.

It provides the following features over and above the regular Spring TestContext Framework:
* Uses ```SpringBootContextLoader``` as the default ```ContextLoader``` when no specific
```@ContextConfiguration(loader=...)``` is defined.
* Automatically searches for a ```@SpringBootConfiguration``` when nested ```@Configuration```
is not used, and no explicit classes are specified.
* Allows custom ```Environment``` properties to be defined using the properties attribute.
* Provides support for different ```webEnvironment``` modes, including the ability to start
a fully running container listening on a defined or random port.
* Registers a ```TestRestTemplate``` bean for use in web tests that are using a fully running container.

A typical Spring Boot 1.4 integration test will look like this:

```
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class MyTest {

    // write test cases here

}
```

Here’s a breakdown of what’s happening:
* **```@RunWith(SpringRunner.class)```** tells JUnit to run using Spring's testing support.
* **```@SpringBootTest```** is saying "bootstrap with Spring Boot's support" (e.g. load
```application.properties``` and give me all the Spring Boot goodness).
* The ```webEnvironment``` attribute of ```@SpringBootTest``` can be used to configure the
runtime environment. You can start tests with a MOCK servlet environment or with a real
HTTP server running on either a ```RANDOM_PORT``` or a ```DEFINED_PORT```.
* ```WebEnvironment.RANDOM_PORT``` might be used so that the container will start at any random
port. It will be helpful if several integration tests are running in parallel on the same machine.
* **```@TestPropertySource```** can be used to configure locations of properties files specific
to the tests. The property file loaded with ```@TestPropertySource``` will override the existing
```application.properties``` file.


## Testing JPA Repositories with @DataJpaTest

```@DataJpaTest``` is an annotation that can be used in combination with
```@RunWith(SpringRunner.class)``` for a typical JPA test. It can be used when a test
focuses **only*** on JPA components. Using this annotation will disable full auto-configuration
and instead apply only configuration relevant to JPA tests.

By default, tests annotated with @DataJpaTest will use an embedded in-memory database
(replacing any explicit or usually auto-configured DataSource). The ```@AutoConfigureTestDatabase```
annotation can be used to override these settings.

If you are looking to load your full application configuration, but use an embedded database,
you should consider ```@SpringBootTest``` combined with ```@AutoConfigureTestDatabase```
rather than this annotation.

To test the persistence layer using an embedded in-memory database with ```@DataJpaTest```,
the following dependency is required. The H2 DB is an in-memory database that eliminates
the need for configuring and starting an actual database for test purposes.

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

    private Product product1;

    private Product product2;

    @Before
    public void setUp() throws Exception {
        product1 = new Product();
        product1.setCode("P001");
        product1.setName("iPhone 7");
        product1.setDescription("This is an Apple phone.");
        product1.setActive(true);
        entityManager.persist(product1);

        product2 = new Product();
        product2.setCode("P002");
        product2.setName("Pixel 2");
        product2.setDescription("This is a Google phone.");
        product2.setActive(false);
        entityManager.persist(product2);
    }

    @Test
    public void findByCode() {
        Product product = productRepository.findByCode("P001");
        assertNotNull(product);
        assertEquals(product1, product);
    }

    @Test
    public void findByCode_NotFound() {
        Product product = productRepository.findByCode("P003");
        assertNull(product);
    }
}
```


## Testing Mongo Repositories with @DataMongoTest

```@DataMongoTest``` is an annotation that can be used in combination with ```@RunWith(SpringRunner.class)```
for a typical MongoDB test. It can be used when a test focuses only on MongoDB components.
Using this annotation will disable full auto-configuration and instead apply only configuration
relevant to MongoDB tests.

By default, tests annotated with ```@DataMongoTest``` will use an embedded in-memory MongoDB process (if available).

To test the persistence layer using an embedded in-memory MongoDB with ```@DataMongoTest```,
the following dependency is required.

```
<dependency>
    <groupId>de.flapdoodle.embed</groupId>
    <artifactId>de.flapdoodle.embed.mongo</artifactId>
    <scope>test</scope>
</dependency>
```

Example:

```
@RunWith(SpringRunner.class)
@SpringBootTest
@DataMongoTest
public class StudentRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StudentRepository studentRepository;

    private Student student1;

    private Student student2;

    @Before
    public void setUp() throws Exception {
        student1 = new Student();
        student1.setName("Jack Bauer");
        student1.setGrade(6);
        mongoTemplate.save(student1);

        student2 = new Student();
        student2.setName("Tony Almeida");
        student2.setGrade(4);
        mongoTemplate.save(student2);
    }

    @Test
    public void findByName() {
        List<Student> students = studentRepository.findByName("Jack Bauer");
        assertEquals(1, students.size());
        assertEquals(student1, students.get(0));
    }

    @Test
    public void findByName_NotFound() {
        List<Student> students = studentRepository.findByName("Chloe O'Brian");
        assertEquals(0, students.size());
    }
}

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

Example:

```

```

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
