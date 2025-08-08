package api.saylix.uz.repository;

import api.saylix.uz.entity.SubjectEntity;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, String> {

    @Transactional
    @Modifying
    @Query("UPDATE SubjectEntity t " +
            "SET t.title = :title, " +
            "t.description = :description, " +
            "t.photoKey = :photoKey, " +
            "t.photoUrl = :photoUrl, " +
            "t.updatedAt = :updatedAt " +
            "WHERE t.id = :id")
    void updateDetailsById(String id,String title, String description, String photoKey, String photoUrl, LocalDateTime updatedAt);


    @Query("SELECT s FROM SubjectEntity s WHERE s.visible = true")
    Page<SubjectEntity> findAllVisible(Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE SubjectEntity t " +
            "SET t.visible = false " +
            "WHERE t.id = :id")
    void updateVisibleIsFalseById(String id);


    @Query("SELECT s FROM SubjectEntity s WHERE s.id = :id AND s.visible = true")
    Optional<SubjectEntity> findByIdAndVisibleTrue(String id);
}
