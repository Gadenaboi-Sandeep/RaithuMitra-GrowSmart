package com.raithumitra.backend.repository;

import com.raithumitra.backend.model.Laborer;
import com.raithumitra.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LaborerRepository extends JpaRepository<Laborer, Integer> {
    Optional<Laborer> findByUser(User user);
}
