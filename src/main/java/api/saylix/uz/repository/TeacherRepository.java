package api.saylix.uz.repository;

import api.saylix.uz.entity.TeacherEntity;
import api.saylix.uz.entity.UsersEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends CrudRepository<TeacherEntity, String> {
    void deleteByUser(UsersEntity user);

    Optional<TeacherEntity> findByUserId(String id);

    String user(UsersEntity user);


    @Transactional
    @Modifying
    @Query("UPDATE TeacherEntity t " +
            "SET t.name = :name, " +
            "t.surname = :surname, " +
            "t.bio = :bio, " +
            "t.experienceYears = :experienceYears " +
            "WHERE t.user.id = :userId")
    int updateNameAndSurnameAndBioAndExperienceYearsByUserId(String userId, String name, String surname, String bio, Integer experienceYears);

    @Transactional
    @Modifying
    @Query("UPDATE TeacherEntity t " +
            "SET t.photoUrl = :photoUrl, " +
            "t.photoKey = :photoKey " +
            "WHERE t.id = :id")
    void updatePhotoKeyAndPhotoUrlById(String photoUrl, String photoKey, String id);
}
