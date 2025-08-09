package api.saylix.uz.controller;

import api.saylix.uz.dto.section.SectionRequestDTO;
import api.saylix.uz.dto.subject.SubjectRequestDTO;
import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.entity.SectionEntity;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.services.SectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/section")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    public ResponseEntity<AppResponse> createSection(@Valid @RequestBody SectionRequestDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language) {
        return ResponseEntity.ok().body(sectionService.createSection(dto, language));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<AppResponse> updateSection(@Valid @RequestBody SectionRequestDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language, @PathVariable String id) {
        return ResponseEntity.ok().body(sectionService.updateSection(id, dto, language));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AppResponse> deleteSection(@RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage language, @PathVariable String id) {
        return ResponseEntity.ok().body(sectionService.deleteSection(id, language));
    }

    @GetMapping
    public ResponseEntity<Page<SectionEntity>> getAllSections(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<SectionEntity> page = sectionService.getAllSections(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<SectionEntity>> getSectionById(
            @PathVariable String id,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage lang
    ) {
        AppResponse<SectionEntity> response = sectionService.getSectionById(id, lang);
        return ResponseEntity.ok(response);
    }



}
