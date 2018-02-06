package devnoh.demoapp.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "student")
public class Student {

    @Id
    private Long id;

    private String name;

    private Integer grade;

}
