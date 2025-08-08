package api.saylix.uz.services;

import api.saylix.uz.dto.users.teacher.TeacherUpdateDetails;
import api.saylix.uz.dto.users.teacher.TeacherUpdatePhotoDTO;
import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.entity.TeacherEntity;
import api.saylix.uz.entity.UsersEntity;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.exps.AppBadException;
import api.saylix.uz.repository.TeacherRepository;
import api.saylix.uz.repository.UsersRepository;
import api.saylix.uz.utils.SpringSecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AppLanguageService getLanguage;

    @Autowired
    private GoogleDriveService googleDriveService;

    public void deleteTeacherWithUser(UsersEntity user) {
        teacherRepository.deleteByUser(user);
    }

    public AppResponse<String> updateDetails(TeacherUpdateDetails dto, AppLanguage language) {
        String userId = SpringSecurityUtil.getCurrentUserId();

        teacherRepository.updateNameAndSurnameAndBioAndExperienceYearsByUserId(userId, dto.getName(), dto.getSurname(), dto.getBio(), dto.getExperienceYears());


        return new AppResponse<>(getLanguage.getMessage("user.update.details.success", language));
    }

    public AppResponse<String> updatePhoto(TeacherUpdatePhotoDTO dto, AppLanguage language) throws IOException {
        String userId = SpringSecurityUtil.getCurrentUserId();
        Optional<TeacherEntity> optional = teacherRepository.findByUserId(userId);

        if(optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("user.not.found", language));
        }
        TeacherEntity teacher = optional.get();

        if (teacher.getPhotoKey() == dto.getPhotoKey()) {
            googleDriveService.deleteMedia(dto.getPhotoKey());
        }

        teacherRepository.updatePhotoKeyAndPhotoUrlById(dto.getPhotoUrl(), dto.getPhotoKey(), teacher.getId());


        return new AppResponse<>(getLanguage.getMessage("user.update.details.success", language));
    }

}
