package com.cia103g5.user.personalskill.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cia103g5.user.ftskill.model.FtSkillVO;

public interface PersonalSkillRepository extends JpaRepository<PersonalSkillVO, PersonalSkillVO.PersonalSkillKey> { // PersonalSkillVO

	// 只查詢 ftId 與 skillName
	@Query("select new com.cia103g5.user.personalskill.model.PersonalSkillDTO(ps.ftVO.ftId, ps.ftSkillVO.skillName) from PersonalSkillVO ps")
	List<PersonalSkillDTO> findPersonalSkillNames();

	// 根據 ftId 查詢該占卜師所有專長
	@Query("select ps.ftSkillVO from PersonalSkillVO ps where ps.ftVO.ftId = :ftId")
	List<FtSkillVO> findFtSkillsByFtId(@Param("ftId") Integer ftId);

	// 插入占卜師和專長的關聯記錄到 ft_service 表格
	@Transactional
	@Modifying
	@Query(value = "insert into ft_service (ft_id, skill_no) values (?1, ?2)", nativeQuery = true)
	void insert(Integer ftId, Integer skillNo);

	// 刪除 ft_service 表格中占卜師和專長的關聯記錄
	@Transactional
	@Modifying
	@Query(value = "delete from ft_service where ft_id = ?1 and skill_no = ?2", nativeQuery = true)
	void delete(Integer ftId, Integer skillNo);

	// 根據 ftId 查詢個人專長
	List<PersonalSkillVO> findByFtVO_FtId(Integer ftId);

}
