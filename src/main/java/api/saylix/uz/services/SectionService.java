package api.saylix.uz.services;

import api.saylix.uz.dto.section.SectionRequestDTO;
import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.entity.SectionEntity;
import api.saylix.uz.entity.SubjectEntity;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.exps.AppBadException;
import api.saylix.uz.repository.SectionRepository;
import api.saylix.uz.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private AppLanguageService getLanguage;

    @Autowired
    private SubjectRepository subjectRepository;

    public AppResponse createSection(SectionRequestDTO dto, AppLanguage language) {
        Optional<SubjectEntity> optional = subjectRepository.findByIdAndVisibleTrue(dto.getSubjectId());
        if (optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("subject.not.found", language));
        }
        SectionEntity sectionEntity = new SectionEntity();
        sectionEntity.setId(UUID.randomUUID().toString());
        sectionEntity.setTitle(dto.getTitle());
        sectionEntity.setDescription(dto.getDescription());
        sectionEntity.setSubject(optional.get());
        sectionEntity.setVisible(true);
        sectionEntity.setUpdatedAt(LocalDateTime.now());
        sectionEntity.setCreatedAt(LocalDateTime.now());
        sectionRepository.save(sectionEntity);

        return new AppResponse<>(getLanguage.getMessage("section.created.success", language));
    }

    // UPDATE
    public AppResponse<?> updateSection(String id, SectionRequestDTO dto, AppLanguage language) {
        Optional<SectionEntity> optional = sectionRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("section.not.found", language));
        }
        SectionEntity section = optional.get();

        if (!section.getSubject().getId().equals(dto.getSubjectId())) {
            SubjectEntity newSubject = subjectRepository.findByIdAndVisibleTrue(dto.getSubjectId())
                    .orElseThrow(() -> new AppBadException(getLanguage.getMessage("subject.not.found", language)));
            section.setSubject(newSubject);
        }

        section.setTitle(dto.getTitle());
        section.setDescription(dto.getDescription());
        section.setUpdatedAt(LocalDateTime.now());
        sectionRepository.save(section);

        return new AppResponse<>(getLanguage.getMessage("section.updated.success", language));
    }

    // DELETE (visible = false)
    public AppResponse<?> deleteSection(String id, AppLanguage language) {
        Optional<SectionEntity> optional = sectionRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("section.not.found", language));
        }

        sectionRepository.updateVisibleIsFalseById(id);

        return new AppResponse<>(getLanguage.getMessage("section.deleted.success", language));
    }

    // GET BY ID
    public AppResponse<SectionEntity> getSectionById(String id, AppLanguage language) {
        SectionEntity section = sectionRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new AppBadException(getLanguage.getMessage("section.not.found", language)));

        return new AppResponse<>(section, getLanguage.getMessage("section.detail.success", language));
    }

    // GET ALL WITH PAGINATION
    public Page<SectionEntity> getAllSections(Pageable pageable) {
        return sectionRepository.findAllVisibleTrue(pageable);
    }

}
