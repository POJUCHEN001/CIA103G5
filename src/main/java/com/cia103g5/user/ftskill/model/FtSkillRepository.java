package com.cia103g5.user.ftskill.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FtSkillRepository extends JpaRepository<FtSkillVO, Integer> {

	// 查詢所有專長，返回 FtSkillVO 物件（所有專長列表）
	List<FtSkillVO> findAll();

	// 插入一個新的專長到 ft_skill 表格
	@Transactional
	@Modifying
	@Query(value = "insert into ft_skill (skill_name) values (?1)", nativeQuery = true)
	void insert(String skillName);

	// 根據專長編號刪除專長
	@Transactional
	@Modifying
	@Query(value = "delete from ft_skill where skill_no = ?1", nativeQuery = true)
	void delete(Integer skillNo);

	// 根據專長編號更新專長名稱
	@Transactional
	@Modifying
	@Query(value = "update ft_skill set skill_name = ?2 where skill_no = ?1", nativeQuery = true)
	void update(Integer skillNo, String newSkillName);

	// 根據 skillName 查詢是否存在相同名稱
	boolean existsBySkillName(String skillName);

}
