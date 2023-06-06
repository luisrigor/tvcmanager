package com.gsc.tvcmanager.repository;


import com.gsc.tvcmanager.model.entity.LoginKey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LoginKeyRepository extends JpaRepository<LoginKey, Long> {

    Optional<LoginKey> findFirstByEnabledIsTrue();
}

