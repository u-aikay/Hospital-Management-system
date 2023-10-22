package com.hospitalservice.mkobo.repositories;

import com.hospitalservice.mkobo.entities.Patient;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PatientRepository extends CrudRepository<Patient, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Patient p WHERE p.age BETWEEN ?1 AND ?2")
    int deleteByAgeRange(int ageStart, int ageEnd);
    List<Patient> findByAgeGreaterThanEqual(int age);

    @Query("SELECT p FROM Patient p WHERE p.age = ?1")
    List<Patient> findByAgeEqual(int age);

    @Modifying
    @Transactional
    @Query("DELETE FROM Patient p WHERE p.age = ?1")
    int deleteByAgeEqual(int age);

}
