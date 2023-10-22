package com.hospitalservice.mkobo.repositories;

import com.hospitalservice.mkobo.entities.Staff;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StaffRepository extends CrudRepository<Staff, Long> {
    Optional<Staff> findByEmail(String email);
    Staff findByUuid(String uuid);

}
