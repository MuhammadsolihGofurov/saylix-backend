package api.saylix.uz.repository;

import api.saylix.uz.entity.TeacherEntity;
import api.saylix.uz.entity.UsersEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends CrudRepository<TeacherEntity, String> {
    void deleteByUser(UsersEntity user);

    Optional<TeacherEntity> findByUserId(String id);
}
