package api.saylix.uz.services;

import api.saylix.uz.entity.UsersEntity;
import api.saylix.uz.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public void deleteStudentWithUser(UsersEntity user) {
        studentRepository.deleteByUser(user);
    }

}
