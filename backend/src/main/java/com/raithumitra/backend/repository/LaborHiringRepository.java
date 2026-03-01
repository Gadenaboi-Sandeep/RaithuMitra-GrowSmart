package com.raithumitra.backend.repository;

import com.raithumitra.backend.model.LaborHiring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LaborHiringRepository extends JpaRepository<LaborHiring, Integer> {
    List<LaborHiring> findByLaborer_User_PhoneNumber(String phoneNumber);
}
