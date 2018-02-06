package devnoh.demoapp;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

// At least one @Test is required, and @BeforeClass, @Before, @After and @AfterClass are all optional.

@Slf4j
public class SomeClassTest {

    @BeforeClass
    public static void setUpBeforeAll() throws Exception {
        log.info("Set up before all tests");
    }

    @Before
    public void setUp() throws Exception {
        log.info("Set up before each test");
    }

    @Test
    public void testMethod1() {
        log.info("Test method 1");
    }

    @Test
    public void testMethod2() {
        log.info("Test method 2");
    }

    @After
    public void tearDown() throws Exception {
        log.info("Tear down after each test");
    }

    @AfterClass
    public static void tearDownAfterAll() throws Exception {
        log.info("Tear down after all tests");
    }
}
