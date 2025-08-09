package api.saylix.uz.dto.section;


import api.saylix.uz.dto.lesson.LessonPublicResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class SectionPublicResponseDTO {

    private String id;
    private String title;
    private String description;

    private List<LessonPublicResponseDTO> lessons;

}
