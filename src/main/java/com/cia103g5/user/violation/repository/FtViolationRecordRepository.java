package com.cia103g5.user.violation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cia103g5.user.violation.model.FtViolationRecord;
@Repository
public interface FtViolationRecordRepository extends JpaRepository<FtViolationRecord, Integer>{

}
