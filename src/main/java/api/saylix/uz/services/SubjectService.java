package api.saylix.uz.services;

import api.saylix.uz.dto.lesson.LessonPublicResponseDTO;
import api.saylix.uz.dto.section.SectionPublicResponseDTO;
import api.saylix.uz.dto.subject.SubjectPublicResponseDTO;
import api.saylix.uz.dto.subject.SubjectRequestDTO;
import api.saylix.uz.dto.users.student.StudentPublicResponseDTO;
import api.saylix.uz.dto.users.teacher.TeacherPublicResponseDTO;
import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.entity.StudentEntity;
import api.saylix.uz.entity.SubjectEntity;
import api.saylix.uz.entity.TeacherEntity;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.exps.AppBadException;
import api.saylix.uz.repository.StudentRepository;
import api.saylix.uz.repository.SubjectRepository;
import api.saylix.uz.repository.TeacherRepository;
import api.saylix.uz.utils.SpringSecurityUtil;
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
    @Autowired
    private StudentRepository studentRepository;

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

    public Page<SubjectPublicResponseDTO> getAllSubjects(Pageable pageable, AppLanguage language) {
        Page<SubjectEntity> page = subjectRepository.findAllVisible(pageable);
        return page.map(this::toPublicDTO);
    }

    public AppResponse<SubjectPublicResponseDTO> getSubjectById(String id, AppLanguage language) {
        SubjectEntity subject = subjectRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new AppBadException(getLanguage.getMessage("subject.not.found", language)));
        return new AppResponse<>(toPublicDTO(subject), getLanguage.getMessage("subject.detail.success", language));
    }

    public AppResponse<?> enrollToSubject(String subjectId, AppLanguage language) {
        String userId = SpringSecurityUtil.getCurrentUserId();

        Optional<StudentEntity> optional = studentRepository.findByUserIdAndVisibleTrue(userId);

        if(optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("student.not.found", language));
        }
        StudentEntity student = optional.get();

        SubjectEntity subject = subjectRepository.findByIdAndVisibleTrue(subjectId)
                .orElseThrow(() -> new AppBadException(getLanguage.getMessage("subject.not.found", language)));

        if (subject.getStudents().contains(student)) {
            throw new AppBadException(getLanguage.getMessage("subject.already.enrolled", language));
        }

        subject.getStudents().add(student);
        subjectRepository.save(subject);

        return new AppResponse<>(getLanguage.getMessage("subject.enrolled.success", language));
    }

    private SubjectPublicResponseDTO toPublicDTO(SubjectEntity subject) {
        SubjectPublicResponseDTO dto = new SubjectPublicResponseDTO();
        dto.setId(subject.getId());
        dto.setTitle(subject.getTitle());
        dto.setDescription(subject.getDescription());
        dto.setPhotoUrl(subject.getPhotoUrl());
        dto.setPhotoKey(subject.getPhotoKey());
        dto.setVisible(subject.getVisible());
        dto.setCreatedAt(subject.getCreatedAt());
        dto.setUpdatedAt(subject.getUpdatedAt());

        // Teacher
        TeacherPublicResponseDTO teacherDTO = new TeacherPublicResponseDTO();
        teacherDTO.setId(subject.getTeacher().getId());
        teacherDTO.setName(subject.getTeacher().getName());
        teacherDTO.setSurname(subject.getTeacher().getSurname());
        teacherDTO.setBio(subject.getTeacher().getBio());
        teacherDTO.setPhotoUrl(subject.getTeacher().getPhotoUrl());
        teacherDTO.setPhotoKey(subject.getTeacher().getPhotoKey());
        teacherDTO.setExperienceYears(subject.getTeacher().getExperienceYears());
        teacherDTO.setRole(subject.getTeacher().getUser().getUserRole());
        teacherDTO.setVisible(subject.getTeacher().getVisible());
        teacherDTO.setCreatedAt(subject.getTeacher().getCreatedAt());
        dto.setTeacher(teacherDTO);

        // Sections
        dto.setSections(subject.getSections().stream().map(section -> {
            SectionPublicResponseDTO sectionDTO = new SectionPublicResponseDTO();
            sectionDTO.setId(section.getId());
            sectionDTO.setTitle(section.getTitle());
            sectionDTO.setDescription(section.getDescription());

            // Lessons inside section
            sectionDTO.setLessons(section.getLessons().stream().map(lesson -> {
                LessonPublicResponseDTO lessonDTO = new LessonPublicResponseDTO();
                lessonDTO.setId(lesson.getId());
                lessonDTO.setTitle(lesson.getTitle());
                lessonDTO.setDescription(lesson.getDescription());
                lessonDTO.setLessonType(lesson.getTypeOfLesson());
                lessonDTO.setScore(lesson.getScore());
                lessonDTO.setContent(lesson.getContent());
                lessonDTO.setAdditionalDetails(lesson.getAdditionalDetails());
                lessonDTO.setVisible(lesson.getVisible());
                lessonDTO.setCreatedAt(lesson.getCreatedAt());
                lessonDTO.setUpdatedAt(lesson.getUpdatedAt());
                lessonDTO.setIsFinished(lesson.getFinished());
                return lessonDTO;
            }).toList());

            return sectionDTO;
        }).toList());

        // Students
        dto.setStudents(subject.getStudents().stream().map(student -> {
            StudentPublicResponseDTO studentDTO = new StudentPublicResponseDTO();
            studentDTO.setId(student.getId());
            studentDTO.setName(student.getName());
            studentDTO.setSurname(student.getSurname());
            studentDTO.setAge(student.getAge());
            studentDTO.setPhotoUrl(student.getPhotoUrl());
            studentDTO.setPhotoKey(student.getPhotoKey());
            studentDTO.setRole(student.getUser().getUserRole());
            studentDTO.setVisible(student.getVisible());
            studentDTO.setCreatedAt(student.getCreatedAt());
            return studentDTO;
        }).toList());

        return dto;
    }


}
