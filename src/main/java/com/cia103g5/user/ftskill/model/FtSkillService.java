package com.cia103g5.user.ftskill.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FtSkillService {

	@Autowired
	private FtSkillRepository ftSkillRepository;

	// 取得所有 FtSkillVO 資料
	public List<FtSkillVO> getAllFtSkills() {
		return ftSkillRepository.findAll();
	}

	// 新增 FtSkillVO
	public void addFtSkill(String skillName) {
		ftSkillRepository.insert(skillName);
	}

	// 刪除 FtSkillVO 根據 skillNo
	public void deleteFtSkillBySkillNo(Integer skillNo) {
		ftSkillRepository.delete(skillNo);
	}

	// 更新 FtSkillVO 根據 skillNo
	public void updateSkillName(Integer skillNo, String newSkillName) {
		ftSkillRepository.update(skillNo, newSkillName);
	}

	// 檢查名稱是否已經存在
	public boolean isSkillNameExists(String skillName) {
		return ftSkillRepository.existsBySkillName(skillName);
	}

	// 查詢 FtSkill By skillNo
	public String getFtSkillNameById(Integer skillNo) {
		return ftSkillRepository.findById(skillNo) // 使用 JpaRepository 的 findById 方法
				.map(FtSkillVO::getSkillName) // 提取 skillName
				.orElseThrow(() -> new RuntimeException("專長編號 " + skillNo + " 不存在"));
	}

}
