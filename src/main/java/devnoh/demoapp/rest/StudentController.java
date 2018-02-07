package devnoh.demoapp.rest;

import devnoh.demoapp.domain.Student;
import devnoh.demoapp.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;

@RestController
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping(value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Student saveStudent(@Valid @RequestBody Student student) {
        log.debug("student={}", student);
        return studentService.saveStudent(student);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Student findStudentById(@PathVariable String id) {
        log.debug("id={}", id);
        return studentService.findStudentById(id);
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Student> findStudentsByName(@RequestParam(defaultValue = "") String name) {
        log.debug("name={}", name);
        return studentService.findStudentsByName(name);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Student> findActiveStudents(
            @RequestParam Integer grade,
            @PageableDefault(sort = {"name"}, direction = Sort.Direction.ASC, size = 5) Pageable pageable) {
        log.debug("pageable={}", pageable);
        return studentService.findStudentsByGrade(grade, pageable);
    }

}
