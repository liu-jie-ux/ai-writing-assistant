package com.example.aiwriting.repository;

import com.example.aiwriting.model.ImprovementRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImprovementRecordRepository extends JpaRepository<ImprovementRecord, Long> {
}
