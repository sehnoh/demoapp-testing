package devnoh.demoapp.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataMongoTest
public class StudentRepositoryTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void findByName() {
    }

    @Test
    public void findByGrade() {
    }

}
