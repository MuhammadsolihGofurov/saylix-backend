package api.saylix.uz.dto.subject;

import api.saylix.uz.dto.section.SectionPublicResponseDTO;
import api.saylix.uz.dto.users.student.StudentPublicResponseDTO;
import api.saylix.uz.dto.users.teacher.TeacherPublicResponseDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SubjectPublicResponseDTO {

    private String id;
    private String title;
    private String description;
    private String photoUrl;
    private String photoKey;

//    teacher
    private TeacherPublicResponseDTO teacher;
//    sections
    private List<SectionPublicResponseDTO> sections;
//    students
    private List<StudentPublicResponseDTO> students;


    private Boolean visible;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

}
