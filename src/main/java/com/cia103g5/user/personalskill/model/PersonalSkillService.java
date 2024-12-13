package com.cia103g5.user.personalskill.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cia103g5.user.ftskill.model.FtSkillVO;

@Service
public class PersonalSkillService {

	@Autowired
	PersonalSkillRepository personalSkillRepository;

	// 根據 FtId 查詢該占卜師的專長列表
	public List<FtSkillVO> getFtSkillsByFtId(Integer ftId) {
		return personalSkillRepository.findFtSkillsByFtId(ftId);
	}

	// 查詢所有占卜師的個人專長名稱，返回 PersonalSkillDTO 類型的列表
	public List<PersonalSkillDTO> getPersonalSkillNames() {
		return personalSkillRepository.findPersonalSkillNames();
	}

	// 根據 ftId 和 skillNo 插入一筆新的個人專長記錄
	public void addPersonalSkill(Integer ftId, Integer skillNo) {
		personalSkillRepository.insert(ftId, skillNo);
	}

	// 根據 ftId 和 skillNo 刪除一筆個人專長記錄
	public void deletePersonalSkill(Integer ftId, Integer skillNo) {
		personalSkillRepository.delete(ftId, skillNo);
	}

}
