package devnoh.demoapp.repository;

import devnoh.demoapp.domain.Student;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

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
        List<Student> list = studentRepository.findByName("Jack Bauer");
        assertEquals(1, list.size());
        assertEquals(student1, list.get(0));
    }

    @Test
    public void findByName_NotFound() {
        List<Student> list = studentRepository.findByName("Chloe O'Brian");
        assertEquals(0, list.size());
    }

    @Test
    public void findByGrade() {
        Page<Student> page = studentRepository.findByGrade(6, new PageRequest(0, 5, Sort.Direction.ASC, "name"));
        assertEquals(1, page.getTotalPages());
        assertEquals(1, page.getTotalElements());
        assertEquals(1, page.getContent().size());
        assertEquals(student1, page.getContent().get(0));
    }

}
