package devnoh.demoapp.repository;

import devnoh.demoapp.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentRepository extends MongoRepository<Student, String> {

    List<Student> findByName(String name);

    Page<Student> findByGrade(int grade, Pageable pageable);

}
