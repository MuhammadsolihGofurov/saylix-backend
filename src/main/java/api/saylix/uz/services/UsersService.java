package api.saylix.uz.services;


import api.saylix.uz.dto.auth.*;
import api.saylix.uz.dto.response.auth.LoginResponseDTO;
import api.saylix.uz.dto.response.auth.StudentInfoDTO;
import api.saylix.uz.dto.response.auth.TeacherInfoDTO;
import api.saylix.uz.dto.users.UserUpdateUsernameConfirmDTO;
import api.saylix.uz.dto.users.UserUpdateUsernameDTO;
import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.entity.StudentEntity;
import api.saylix.uz.entity.TeacherEntity;
import api.saylix.uz.entity.UsersEntity;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.enums.UsersRoles;
import api.saylix.uz.enums.UsersStatus;
import api.saylix.uz.exps.AppBadException;
import api.saylix.uz.repository.StudentRepository;
import api.saylix.uz.repository.TeacherRepository;
import api.saylix.uz.repository.UsersRepository;
import api.saylix.uz.services.sendCode.SendCodeHistoryService;
import api.saylix.uz.services.sendCode.SendCodeService;
import api.saylix.uz.utils.JwtUtil;
import api.saylix.uz.utils.SpringSecurityUtil;
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

    @Autowired
    private SendCodeHistoryService sendCodeHistoryService;

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
            sendCodeService.sendCodeForRegisterUser(dto.getUsername(), language);
        } else if (UsernameCheckingUtil.isValidPhone(dto.getUsername())) {
            return new AppResponse<>(getLanguage.getMessage("send.code.to.phone", language));
        }

        return new AppResponse<>(getLanguage.getMessage("send.code", language));
    }

    public AppResponse<String> registrationConfirm(RegisterConfirmUserDTO dto, AppLanguage language) {
        Optional<UsersEntity> optional = usersRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if (optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("username.not.exist", language));
        }

        UsersEntity user = optional.get();

        if (!user.getStatus().equals(UsersStatus.IN_REGISTRATION)) {
            throw new AppBadException(getLanguage.getMessage("username.wrong.status", language));
        }

        sendCodeHistoryService.check(dto.getUsername(), dto.getCode(), language);


        if (user.getUserRole().equals(UsersRoles.TEACHER)) {
            usersRepository.changeStatus(user.getId(), UsersStatus.CHECKING);
            return new AppResponse<>(getLanguage.getMessage("wait.admin.to.approve", language));
        } else {
            usersRepository.changeStatus(user.getId(), UsersStatus.ACTIVE);
        }


        return new AppResponse<>(getLanguage.getMessage("registration.confirm.success", language));
    }

    public AppResponse<String> resetPassword(ResetPasswordDTO dto, AppLanguage language) {
        Optional<UsersEntity> optional = usersRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if (optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("username.not.exist", language));
        }
        UsersEntity user = optional.get();
        if (!user.getStatus().equals(UsersStatus.ACTIVE)) {
            throw new AppBadException(getLanguage.getMessage("username.wrong.status", language));
        }

        if (UsernameCheckingUtil.isEmailValid(dto.getUsername())) {
            sendCodeService.sendCodeForResetPassword(dto.getUsername(), language);
        } else if (UsernameCheckingUtil.isValidPhone(dto.getUsername())) {
            return new AppResponse<>(getLanguage.getMessage("send.code.to.phone", language));
        }

        return new AppResponse<>(getLanguage.getMessage("send.to.code.for.forgot.password", language));
    }

    public AppResponse<String> resetPasswordConfirm(ResetPasswordConfirmDTO dto, AppLanguage language) {
        Optional<UsersEntity> optional = usersRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if (optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("username.not.exist", language));
        }
        UsersEntity user = optional.get();
        if (!user.getStatus().equals(UsersStatus.ACTIVE)) {
            throw new AppBadException(getLanguage.getMessage("username.wrong.status", language));
        }

        sendCodeHistoryService.check(dto.getUsername(), dto.getCode(), language);

        usersRepository.updatePassword(user.getId(), bCryptPasswordEncoder.encode(dto.getNewPassword()));


        return new AppResponse<>(getLanguage.getMessage("reset.password.confirm", language));
    }

    public AppResponse<LoginResponseDTO> login(LoginUserDTO dto, AppLanguage language) {
        Optional<UsersEntity> optional = usersRepository.findByUsernameAndVisibleTrue(dto.getUsername());

        if (optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("username.not.exist", language));
        }

        UsersEntity user = optional.get();

        if (!user.getStatus().equals(UsersStatus.ACTIVE)) {
            throw new AppBadException(getLanguage.getMessage("username.wrong.status", language));
        }

        if (!bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new AppBadException(getLanguage.getMessage("login.password.incorrect", language));
        }

        LoginResponseDTO response = getLoginResponseDTO(user, language);

        response.setToken(JwtUtil.encode(user.getUsername(), user.getId(), user.getUserRole()));

        return new AppResponse<>(response, getLanguage.getMessage("login.success", language));

    }

    public AppResponse<LoginResponseDTO> getMe(AppLanguage language) {
        String userId = SpringSecurityUtil.getCurrentUserId();
        Optional<UsersEntity> optional = usersRepository.findById(userId);

        if (optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("user.not.exist", language));
        }

        UsersEntity user = optional.get();

        if (!user.getStatus().equals(UsersStatus.ACTIVE)) {
            throw new AppBadException(getLanguage.getMessage("username.wrong.status", language));
        }

        LoginResponseDTO response = getLoginResponseDTO(user, language);

        response.setToken(JwtUtil.encode(user.getUsername(), user.getId(), user.getUserRole()));

        return new AppResponse<>(response, getLanguage.getMessage("success", language));

    }

    public AppResponse<String> updateUsername(UserUpdateUsernameDTO dto, AppLanguage language) {
        Optional<UsersEntity> optional = usersRepository.findByUsernameAndVisibleTrue(dto.getNewUsername());

        if (optional.isPresent()) {
            throw new AppBadException(getLanguage.getMessage("username.exist", language));
        }

        if (UsernameCheckingUtil.isEmailValid(dto.getNewUsername())) {
            sendCodeService.sendCodeForUsernameUpdate(dto.getNewUsername(), language);
        } else if (UsernameCheckingUtil.isValidPhone(dto.getNewUsername())) {
            return new AppResponse<>(getLanguage.getMessage("send.code.to.phone", language));
        }


        return new AppResponse<>(getLanguage.getMessage("send.code.for.update", language));
    }

    public AppResponse<String> updateUsernameConfirm(UserUpdateUsernameConfirmDTO dto, AppLanguage language) {
        String userId = SpringSecurityUtil.getCurrentUserId();

        // if username is phone, add if statement
        sendCodeHistoryService.check(dto.getNewUsername(), dto.getCode(), language);

        usersRepository.updateUsernameById(dto.getNewUsername(), userId);

        Optional<UsersEntity> optional = usersRepository.findById(userId);
        UsersEntity user = optional.get();
        String newToken = JwtUtil.encode(dto.getNewUsername(), user.getId(), user.getUserRole());

        return new AppResponse<>(newToken, getLanguage.getMessage("user.update.details.success", language));
    }

    public AppResponse<String> checkUserStatusForTeacher(CheckStatusByUsernameDTO dto , AppLanguage language) {
        Optional<UsersEntity> optional = usersRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if (optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("username.not.exist", language));
        }

        UsersEntity user = optional.get();

        if (user.getStatus().equals(UsersStatus.CHECKING)) {
            throw new AppBadException(getLanguage.getMessage("status.checking.for.teacher", language));
        }

        return new AppResponse<>(getLanguage.getMessage("check.status.success", language));
    }

    //    ADDITIONAL METHODS
    private LoginResponseDTO getLoginResponseDTO(UsersEntity user, AppLanguage language) {
        LoginResponseDTO response = new LoginResponseDTO();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getUserRole());
        response.setVisible(user.getVisible());
        response.setStatus(user.getStatus());
        response.setUpdatedAt(user.getUpdatedAt());
        response.setCreatedAt(user.getCreatedAt());

        if (user.getUserRole().equals(UsersRoles.TEACHER)) {
            TeacherEntity teacher = teacherRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new AppBadException(getLanguage.getMessage("teacher.not.exist", language)));

            TeacherInfoDTO teacherInfo = getTeacherInfoDTO(teacher);

            response.setTeacher(teacherInfo);

        } else if (user.getUserRole().equals(UsersRoles.STUDENT)) {
            StudentEntity student = studentRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new AppBadException(getLanguage.getMessage("student.not.exist", language)));

            StudentInfoDTO studentInfo = getStudentInfoDTO(student);

            response.setStudent(studentInfo);
        }

        return response;
    }

    private static StudentInfoDTO getStudentInfoDTO(StudentEntity student) {
        StudentInfoDTO studentInfo = new StudentInfoDTO();
        studentInfo.setId(student.getId());
        studentInfo.setName(student.getName());
        studentInfo.setSurname(student.getSurname());
        studentInfo.setAge(student.getAge());
        studentInfo.setUserId(student.getUser().getId());
        studentInfo.setPhotoUrl(student.getPhotoUrl());
        studentInfo.setPhotoKey(student.getPhotoKey());
        studentInfo.setCreatedAt(student.getCreatedAt());
        studentInfo.setUpdatedAt(student.getUpdatedAt());
        studentInfo.setVisible(student.getVisible());
        return studentInfo;
    }

    private static TeacherInfoDTO getTeacherInfoDTO(TeacherEntity teacher) {
        TeacherInfoDTO teacherInfo = new TeacherInfoDTO();
        teacherInfo.setId(teacher.getId());
        teacherInfo.setName(teacher.getName());
        teacherInfo.setSurname(teacher.getSurname());
        teacherInfo.setBio(teacher.getBio());
        teacherInfo.setExperienceYears(teacher.getExperienceYears());
        teacherInfo.setVisible(teacher.getVisible());
        teacherInfo.setPhotoUrl(teacher.getPhotoUrl());
        teacherInfo.setPhotoKey(teacher.getPhotoKey());
        teacherInfo.setCreatedAt(teacher.getCreatedAt());
        teacherInfo.setUpdatedAt(teacher.getUpdatedAt());
        teacherInfo.setUserId(teacher.getUser().getId());

        return teacherInfo;
    }


}
