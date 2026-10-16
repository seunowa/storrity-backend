package com.storrity.storrity.storeaccess.repository;

import com.storrity.storrity.storeaccess.entity.UserStoreAccess;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoreAccessRepository extends JpaRepository<UserStoreAccess, UUID> {
    List<UserStoreAccess> findAllByUsername(String username);
    List<UserStoreAccess> findAllByStoreId(UUID storeId);
    Optional<UserStoreAccess> findByUsernameAndStoreId(String username, UUID storeId);
    boolean existsByUsernameAndStoreId(String username, UUID storeId);
    void deleteAllByUsername(String username);
    void deleteByUsernameAndStoreId(String username, UUID storeId);
    List<UserStoreAccess> findAllByStoreIdIn(List<UUID> storeIds);
}
