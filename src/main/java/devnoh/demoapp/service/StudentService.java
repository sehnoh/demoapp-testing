package devnoh.demoapp.service;

import devnoh.demoapp.domain.Student;
import devnoh.demoapp.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student saveStudent(Student product) {
        return studentRepository.save(product);
    }

    public Student findStudentById(String id) {
        return studentRepository.findOne(id);
    }

    public List<Student> findStudentsByName(String name) {
        return studentRepository.findByName(name);
    }

    public Page<Student> findStudentsByGrade(int grade, Pageable pageable) {
        return studentRepository.findByGrade(grade, pageable);
    }

}
