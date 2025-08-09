package api.saylix.uz.controller;

import api.saylix.uz.dto.lesson.LessonRequestDTO;
import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.entity.LessonEntity;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.services.LessonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/lesson")
public class LessonController {


    @Autowired
    private LessonService lessonService;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    public ResponseEntity<AppResponse> createLesson(@Valid @RequestBody LessonRequestDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) {
        return ResponseEntity.ok().body(lessonService.lessonService(dto, language));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse> getLessonById(
            @PathVariable String id,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language
    ) {
        return ResponseEntity.ok().body(lessonService.getLessonById(id, language));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<AppResponse> updateLesson(
            @PathVariable String id,
            @Valid @RequestBody LessonRequestDTO dto,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language
    ) {
        return ResponseEntity.ok().body(lessonService.updateLesson(id, dto, language));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AppResponse> deleteLesson(
            @PathVariable String id,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language
    ) {
        return ResponseEntity.ok().body(lessonService.deleteLesson(id, language));
    }

    @GetMapping("")
    public ResponseEntity<Page<LessonEntity>> getAllLessons(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok().body(lessonService.getAllLessons(pageable));
    }

}
