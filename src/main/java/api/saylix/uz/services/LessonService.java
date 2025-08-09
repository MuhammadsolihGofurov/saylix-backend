package api.saylix.uz.services;

import api.saylix.uz.dto.lesson.LessonRequestDTO;
import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.entity.LessonEntity;
import api.saylix.uz.entity.SectionEntity;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.exps.AppBadException;
import api.saylix.uz.repository.LessonRepository;
import api.saylix.uz.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private AppLanguageService getLanguage;

    @Autowired
    private SectionRepository sectionRepository;

    public AppResponse lessonService(LessonRequestDTO dto, AppLanguage language) {
        Optional<SectionEntity> optional = sectionRepository.findByIdAndVisibleTrue(dto.getSectionId());
        if (optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("section.not.found", language));
        }

        LessonEntity lesson = new LessonEntity();
        lesson.setId(UUID.randomUUID().toString());
        lesson.setTitle(dto.getTitle());
        lesson.setDescription(dto.getDescription());
        lesson.setSection(optional.get());
        lesson.setTypeOfLesson(dto.getType());
        lesson.setAdditionalDetails(dto.getAdditionalDetails());
        lesson.setContent(dto.getContent());
        lesson.setScore(dto.getScore());
        lesson.setFinished(false);
        lesson.setCreatedAt(LocalDateTime.now());
        lesson.setUpdatedAt(LocalDateTime.now());

        lessonRepository.save(lesson);


        return new AppResponse<>(getLanguage.getMessage("lesson.created.success", language));
    }

    public AppResponse<?> updateLesson(String id, LessonRequestDTO dto, AppLanguage language) {
        LessonEntity lesson = lessonRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new AppBadException(getLanguage.getMessage("lesson.not.found", language)));

        if (!lesson.getSection().getId().equals(dto.getSectionId())) {
            SectionEntity newSection = sectionRepository.findByIdAndVisibleTrue(dto.getSectionId())
                    .orElseThrow(() -> new AppBadException(getLanguage.getMessage("section.not.found", language)));
            lesson.setSection(newSection);
        }

        lesson.setTitle(dto.getTitle());
        lesson.setDescription(dto.getDescription());
        lesson.setTypeOfLesson(dto.getType());
        lesson.setAdditionalDetails(dto.getAdditionalDetails());
        lesson.setContent(dto.getContent());
        lesson.setScore(dto.getScore());
        lesson.setUpdatedAt(LocalDateTime.now());

        lessonRepository.save(lesson);

        return new AppResponse<>(getLanguage.getMessage("lesson.updated.success", language));
    }

    public AppResponse<?> deleteLesson(String id, AppLanguage language) {
        LessonEntity lesson = lessonRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new AppBadException(getLanguage.getMessage("lesson.not.found", language)));

        lessonRepository.updateVisibleIsFalseById(id);

        return new AppResponse<>(getLanguage.getMessage("lesson.deleted.success", language));
    }

    public AppResponse<LessonEntity> getLessonById(String id, AppLanguage language) {
        LessonEntity lesson = lessonRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new AppBadException(getLanguage.getMessage("lesson.not.found", language)));

        return new AppResponse<>(lesson, getLanguage.getMessage("lesson.detail.success", language));
    }

    public Page<LessonEntity> getAllLessons(Pageable pageable) {
        return lessonRepository.findAllVisibleTrue(pageable);
    }

}
