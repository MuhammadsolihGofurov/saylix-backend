package api.saylix.uz.repository;

import api.saylix.uz.entity.StudentEntity;
import api.saylix.uz.entity.UsersEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, String> {
    void deleteByUser(UsersEntity user);

    Optional<StudentEntity> findByUserId(String id);

    @Transactional
    @Modifying
    @Query("UPDATE StudentEntity t " +
            "SET t.name = :name, " +
            "t.surname = :surname, " +
            "t.age = :age " +
            "WHERE t.user.id = :userId")
    int updateNameAndSurnameAndAgeByUserId(String userId, String name, String surname, Integer age);

    @Transactional
    @Modifying
    @Query("UPDATE StudentEntity t " +
            "SET t.photoUrl = :photoUrl, " +
            "t.photoKey = :photoKey " +
            "WHERE t.id = :id")
    void updatePhotoKeyAndPhotoUrlById(String photoUrl, String photoKey, String id);

}
