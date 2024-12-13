package com.cia103g5.user.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cia103g5.user.post.model.PostReport;


@Repository
public interface PostReportRepository extends JpaRepository<PostReport, Integer> {
}