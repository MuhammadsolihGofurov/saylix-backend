package api.saylix.uz.repository.sendCode;

import api.saylix.uz.entity.SendCodeHistoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SendCodeHistoryRepository extends CrudRepository<SendCodeHistoryEntity, String> {
    Long countByUsernameAndCreatedAtBetween(String username, LocalDateTime start, LocalDateTime end);


    Optional<SendCodeHistoryEntity> findTop1ByUsernameOrderByCreatedAtDesc(String username);

    @Modifying
    @Transactional
    @Query("update SendCodeHistoryEntity set attemptCount = coalesce(attemptCount, 0) + 1 where id =?1")
    void updateAttemptCount(String id);
}
