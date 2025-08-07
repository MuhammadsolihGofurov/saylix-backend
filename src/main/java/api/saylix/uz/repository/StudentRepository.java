package api.saylix.uz.repository;

import api.saylix.uz.entity.StudentEntity;
import api.saylix.uz.entity.UsersEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, String> {
    void deleteByUser(UsersEntity user);

    Optional<StudentEntity> findByUserId(String id);
}
