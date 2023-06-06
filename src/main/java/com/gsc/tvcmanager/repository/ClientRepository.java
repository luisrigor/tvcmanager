package com.gsc.tvcmanager.repository;
import com.gsc.tvcmanager.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByApplicationId(Long applicationId);
}
