package api.saylix.uz.repository;

import api.saylix.uz.entity.UsersEntity;
import api.saylix.uz.enums.UsersStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<UsersEntity, String> {
    Optional<UsersEntity> findByUsernameAndVisibleTrue(String username);

    void flush();

    @Modifying
    @Transactional
    @Query("update UsersEntity set status  =?2 where id = ?1")
    void changeStatus(String id, UsersStatus usersStatus);

    @Modifying
    @Transactional
    @Query("update UsersEntity set password =?2 where id = ?1")
    void updatePassword(String id, String password);

    @Transactional
    @Query("update UsersEntity u set u.username = :username where u.id = :id")
    @Modifying
    void updateUsernameById(String username, String id);
}
