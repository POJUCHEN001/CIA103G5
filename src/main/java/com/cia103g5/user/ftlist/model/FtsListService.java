package com.cia103g5.user.ftlist.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cia103g5.user.favft.model.FavFtService;
import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.ftgrade.model.FtGrade;
import com.cia103g5.user.ftgrade.model.FtGradeRepository;
import com.cia103g5.user.ftskill.model.FtSkillVO;
import com.cia103g5.user.personalskill.model.PersonalSkillDTO;
import com.cia103g5.user.personalskill.model.PersonalSkillService;

@Service
public class FtsListService {

	@Autowired
	FtsListRepository ftsListRepository;
  
	@Autowired
	FtGradeRepository ftGradeRepository;
	  
	@Autowired
	FavFtService favFtService;
	
	@Autowired
	PersonalSkillService personalSkillService;
  
	// 取得所有 FtVO 資料
	public FtVO findById(Integer ftId) {
      // 使用 JpaRepository 的 findById 方法
		return ftsListRepository.findById(ftId).orElse(null);
	}

	// 查詢所有占卜師資訊(無須登入)
	public List<FtDTO> getAllFtsByUser(String keyword, String nickname, Integer skillNo, LocalDate startDate,
          Integer minPrice, Integer maxPrice, Integer ftRank) {
 
	// 處理日期範圍參數
	Date sqlStartDate = null;
	if (startDate != null) {
		sqlStartDate = Date.valueOf(startDate);
	}

	// 查詢符合條件的 FtVO
	List<FtVO> fts = ftsListRepository.findAllByConditions(keyword, nickname, skillNo, sqlStartDate, minPrice, maxPrice, ftRank);
   
	// 獲取專長名稱
	List<PersonalSkillDTO> personalSkillNames = personalSkillService.getPersonalSkillNames();

	// 將 FtVO 轉換為 FtDTO 並設置資訊
	List<FtDTO> ftDTOs = fts.stream()
		.map(ft -> new FtDTO(
           ft.getFtId(),
           ft.getCompanyName(),
           ft.getNickname(),
           ft.getPhoto(),
           ft.getIntro(),
           ft.getPrice(),
           personalSkillNames.stream()
               .filter(personalSkill -> personalSkill.getFtId().equals(ft.getFtId())) // 根據 ftId 進行過濾
               .map(PersonalSkillDTO::getSkillName) // 提取專長名稱
               .collect(Collectors.toList()), // 收集專長名稱到列表
           ft.getFtRank(), false))
		.collect(Collectors.toList());
	return ftDTOs;
	}
	
	// 取得所有 FtGrade
	public List<FtGrade> getAllftRanks() {
		return ftGradeRepository.findAll();
	}

	// 取得所有占卜師資訊(無須登入)
	public FtDTO getFtByIdByUser(Integer ftId) {
	FtVO ft = ftsListRepository.findById(ftId).orElse(null);
	return new FtDTO(
		ft.getFtId(),
		ft.getCompanyName(),
		ft.getNickname(),
		ft.getPhoto(),
		ft.getIntro(),
		ft.getPrice(),
		personalSkillService.getFtSkillsByFtId(ftId).stream()
			.map(FtSkillVO::getSkillName)
			.collect(Collectors.toList()),
		ft.getFtRank(),
		false);
	}
  
}
