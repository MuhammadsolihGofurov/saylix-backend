package api.saylix.uz.services;

import api.saylix.uz.dto.subject.SubjectRequestDTO;
import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.entity.SubjectEntity;
import api.saylix.uz.entity.TeacherEntity;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.exps.AppBadException;
import api.saylix.uz.repository.SubjectRepository;
import api.saylix.uz.repository.TeacherRepository;
import api.saylix.uz.utils.SpringSecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AppLanguageService getLanguage;

    public AppResponse<?> createSubject(SubjectRequestDTO dto, AppLanguage language) {
        String userId = SpringSecurityUtil.getCurrentUserId();
        TeacherEntity teacher = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new AppBadException(getLanguage.getMessage("teacher.not.found", language)));

        SubjectEntity subject = new SubjectEntity();
        subject.setId(UUID.randomUUID().toString());
        subject.setTitle(dto.getTitle());
        subject.setDescription(dto.getDescription());
        subject.setPhotoUrl(dto.getPhotoUrl());
        subject.setPhotoKey(dto.getPhotoKey());
        subject.setTeacher(teacher);
        subject.setUpdatedAt(LocalDateTime.now());
        subject.setCreatedAt(LocalDateTime.now());
        subjectRepository.save(subject);

        return new AppResponse<>(subject, getLanguage.getMessage("subject.created.success", language));

    }

    public AppResponse<?> updateSubject(String id, SubjectRequestDTO dto, AppLanguage language) {
        Optional<SubjectEntity> optional = subjectRepository.findByIdAndVisibleTrue(id);
        String userId = SpringSecurityUtil.getCurrentUserId();
        TeacherEntity teacher = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new AppBadException(getLanguage.getMessage("teacher.not.found", language)));

        if (optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("subject.not.found", language));
        }
        SubjectEntity subject = optional.get();

        if(!subject.getTeacher().getId().equals(teacher.getId())) {
            throw new AppBadException(getLanguage.getMessage("subject.updated.wrong.with.teacher", language));
        }

        subjectRepository.updateDetailsById(id, dto.getTitle(), dto.getDescription(), dto.getPhotoKey(), dto.getPhotoUrl(), LocalDateTime.now());


        return new AppResponse<>(getLanguage.getMessage("subject.updated.success", language));

    }

    public AppResponse<?> deleteSubject(String id, AppLanguage language) {
        Optional<SubjectEntity> optional = subjectRepository.findByIdAndVisibleTrue(id);
        String userId = SpringSecurityUtil.getCurrentUserId();
        TeacherEntity teacher = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new AppBadException(getLanguage.getMessage("teacher.not.found", language)));

        if (optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("subject.not.found", language));
        }
        SubjectEntity subject = optional.get();

        if(!subject.getTeacher().getId().equals(teacher.getId())) {
            throw new AppBadException(getLanguage.getMessage("subject.updated.wrong.with.teacher", language));
        }

        subjectRepository.updateVisibleIsFalseById(id);


        return new AppResponse<>(getLanguage.getMessage("subject.deleted.success", language));

    }

    public Page getAllSubjects(Pageable pageable, AppLanguage language) {
        Page<SubjectEntity> page = subjectRepository.findAllVisible(pageable);
        return page;
    }

    public AppResponse<SubjectEntity> getSubjectById(String id, AppLanguage language) {
        SubjectEntity subject = subjectRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new AppBadException(getLanguage.getMessage("subject.not.found", language)));
        return new AppResponse<>(subject, getLanguage.getMessage("subject.detail.success", language));
    }

}
