package api.saylix.uz.services;

import api.saylix.uz.dto.users.student.StudentUpdateDetailsDTO;
import api.saylix.uz.dto.users.teacher.TeacherUpdatePhotoDTO;
import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.entity.StudentEntity;
import api.saylix.uz.entity.TeacherEntity;
import api.saylix.uz.entity.UsersEntity;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.exps.AppBadException;
import api.saylix.uz.repository.StudentRepository;
import api.saylix.uz.utils.SpringSecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AppLanguageService getLanguage;

    @Autowired
    private GoogleDriveService googleDriveService;

    public void deleteStudentWithUser(UsersEntity user) {
        studentRepository.deleteByUser(user);
    }

    public AppResponse<String> updateDetails(@Valid StudentUpdateDetailsDTO dto, AppLanguage language) {
        String userId = SpringSecurityUtil.getCurrentUserId();

        studentRepository.updateNameAndSurnameAndAgeByUserId(userId, dto.getName(), dto.getSurname(), dto.getAge());


        return new AppResponse<>(getLanguage.getMessage("user.update.details.success", language));
    }

    public AppResponse<String> updatePhoto(TeacherUpdatePhotoDTO dto, AppLanguage language) throws IOException {
        String userId = SpringSecurityUtil.getCurrentUserId();
        Optional<StudentEntity> optional = studentRepository.findByUserId(userId);

        if(optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("user.not.found", language));
        }
        StudentEntity student = optional.get();

        if (student.getPhotoKey() == dto.getPhotoKey()) {
            googleDriveService.deleteMedia(dto.getPhotoKey());
        }

        studentRepository.updatePhotoKeyAndPhotoUrlById(dto.getPhotoUrl(), dto.getPhotoKey(), student.getId());


        return new AppResponse<>(getLanguage.getMessage("user.update.details.success", language));
    }
}
