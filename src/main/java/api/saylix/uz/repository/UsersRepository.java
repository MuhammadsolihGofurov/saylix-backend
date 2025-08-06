package api.saylix.uz.repository;

import api.saylix.uz.entity.UsersEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<UsersEntity, String> {
    Optional<UsersEntity> findByUsernameAndVisibleTrue(String username);

    void flush();
}
