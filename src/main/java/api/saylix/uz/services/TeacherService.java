package api.saylix.uz.services;

import api.saylix.uz.entity.UsersEntity;
import api.saylix.uz.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public void deleteTeacherWithUser(UsersEntity user) {
        teacherRepository.deleteByUser(user);
    }

}
