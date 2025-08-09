package api.saylix.uz.repository;

import api.saylix.uz.entity.SectionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<SectionEntity, String> {
    Optional<SectionEntity> findByIdAndVisibleTrue(String id);

    @Transactional
    @Modifying
    @Query("UPDATE SectionEntity t " +
            "SET t.visible = false " +
            "WHERE t.id = :id")
    void updateVisibleIsFalseById(String id);

    @Query("SELECT s FROM SectionEntity s WHERE s.visible = true")
    Page<SectionEntity> findAllVisibleTrue(Pageable pageable);
}
