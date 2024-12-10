package com.cia103g5.user.ftgrade.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FtGradeRepository extends JpaRepository<FtGrade, Integer>{

    Optional<FtGrade> findByFtRank(Integer ftRank);
}
