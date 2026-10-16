package com.storrity.storrity.storeaccess.repository;

import com.storrity.storrity.storeaccess.entity.UserStoreAccessPolicy;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoreAccessPolicyRepository extends JpaRepository<UserStoreAccessPolicy, UUID> {
    Optional<UserStoreAccessPolicy> findByUsername(String username);
    List<UserStoreAccessPolicy> findAllByAccessAllStoresTrue();
}
