package api.saylix.uz.services;


import api.saylix.uz.dto.auth.RegisterUserDTO;
import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.entity.StudentEntity;
import api.saylix.uz.entity.TeacherEntity;
import api.saylix.uz.entity.UsersEntity;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.enums.TeacherStatus;
import api.saylix.uz.enums.UsersRoles;
import api.saylix.uz.enums.UsersStatus;
import api.saylix.uz.exps.AppBadException;
import api.saylix.uz.repository.StudentRepository;
import api.saylix.uz.repository.TeacherRepository;
import api.saylix.uz.repository.UsersRepository;
import api.saylix.uz.services.sendCode.SendCodeService;
import api.saylix.uz.utils.UsernameCheckingUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AppLanguageService getLanguage;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SendCodeService sendCodeService;

    @Transactional
    public AppResponse<String> registrationUser(RegisterUserDTO dto, AppLanguage language) {
        Optional<UsersEntity> optional = usersRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if (!optional.isEmpty()) {
            UsersEntity user = optional.get();
            if (user.getStatus().equals(UsersStatus.IN_REGISTRATION)) {

                if (user.getUserRole().equals(UsersRoles.TEACHER)) {
                    teacherService.deleteTeacherWithUser(user);
                } else if (user.getUserRole().equals(UsersRoles.STUDENT)) {
                    studentService.deleteStudentWithUser(user);
                }

                usersRepository.delete(user);
                usersRepository.flush();
            } else {
                throw new AppBadException(getLanguage.getMessage("username.exist", language));
            }
        }

        UsersEntity user = new UsersEntity();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(dto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setVisible(true);
        user.setStatus(UsersStatus.IN_REGISTRATION);
        user.setUserRole(dto.getRole());
        user.setUpdatedAt(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());

        usersRepository.save(user);

        if (dto.getRole().equals(UsersRoles.TEACHER)) {
            TeacherEntity teacherEntity = new TeacherEntity();
            teacherEntity.setId(UUID.randomUUID().toString());
            teacherEntity.setName(dto.getName());
            teacherEntity.setUser(user);
            teacherEntity.setVisible(true);
            teacherEntity.setStatus(TeacherStatus.CHECKING);
            teacherEntity.setUpdatedAt(LocalDateTime.now());
            teacherEntity.setCreatedAt(LocalDateTime.now());
            teacherRepository.save(teacherEntity);
        } else if (dto.getRole().equals(UsersRoles.STUDENT)) {
            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setId(UUID.randomUUID().toString());
            studentEntity.setName(dto.getName());
            studentEntity.setUser(user);
            studentEntity.setVisible(true);
            studentEntity.setUpdatedAt(LocalDateTime.now());
            studentEntity.setCreatedAt(LocalDateTime.now());
            studentRepository.save(studentEntity);
        }

        if (UsernameCheckingUtil.isEmailValid(dto.getUsername())) {
            sendCodeService.sendRegistrationEmail(dto.getUsername(), language);
        } else if (UsernameCheckingUtil.isValidPhone(dto.getUsername())) {
            return new AppResponse<>(getLanguage.getMessage("send.code.to.phone", language));
        }

        return new AppResponse<>(getLanguage.getMessage("send.code", language));
    }


}
