package com.cia103g5.user.ftlist.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
public class FtListService {

	@Autowired
	FtListRepository ftListRepository;
  
	@Autowired
	FtGradeRepository ftGradeRepository;
	  
	@Autowired
	FavFtService favFtService;
	
	@Autowired
	PersonalSkillService personalSkillService;
  
	// 取得所有 FtVO 資料
	public FtVO getById(Integer ftId) {
		return ftListRepository.findById(ftId).orElse(null);
	}
	
	// 查詢所有占卜師資訊，結合 memId
	public List<FtDTO> getAllFts(Integer memId, String keyword, String nickname, Integer skillNo, LocalDate startDate,
		Integer minPrice, Integer maxPrice, Integer ftRank) {
	
		// 處理日期範圍參數
		Date sqlStartDate = null;
		if (startDate != null) {
			sqlStartDate = Date.valueOf(startDate);
		}
	
		// 查詢符合條件的 FtVO
		List<FtVO> fts = ftListRepository.findAllByConditions(keyword, nickname, skillNo, sqlStartDate, minPrice, maxPrice, ftRank);
	   
		// 獲取專長名稱
		List<PersonalSkillDTO> personalSkillNames = personalSkillService.getPersonalSkillNames();
		
		// 動態顯示收藏狀態
		List<Integer> favFts = favFtService.getFavFtsByMemId(memId);
	
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
	           ft.getFtRank(),
	           favFts.contains(ft.getFtId())))
			.collect(Collectors.toList());
		return ftDTOs;
	}
	
	public List<FtDTO> getAllFts(String keyword, String nickname, Integer skillNo, LocalDate startDate,
			Integer minPrice, Integer maxPrice, Integer ftRank) {
		
			// 處理日期範圍參數
			Date sqlStartDate = null;
			if (startDate != null) {
				sqlStartDate = Date.valueOf(startDate);
			}
		
			// 查詢符合條件的 FtVO
			List<FtVO> fts = ftListRepository.findAllByConditions(keyword, nickname, skillNo, sqlStartDate, minPrice, maxPrice, ftRank);
		   
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
		           ft.getFtRank(),
		           false))
				.collect(Collectors.toList());
			return ftDTOs;
		}
	
	// 取得所有 FtGrade
	public List<FtGrade> getAllftRanks() {
		return ftGradeRepository.findAll();
	}
	
	// 獲取指定占卜師的詳細訊息（FtDTO），結合 memId 判斷是否收藏
    public FtDTO getFtById(Integer memId, Integer ftId) {
		List<Integer> favFts = favFtService.getFavFtsByMemId(memId);
		FtVO ft = ftListRepository.findById(ftId).orElse(null);
		return new FtDTO(ft.getFtId(), ft.getCompanyName(), ft.getNickname(), ft.getPhoto(), ft.getIntro(),
				ft.getPrice(), personalSkillService.getFtSkillsByFtId(ftId).stream().map(FtSkillVO::getSkillName)
						.collect(Collectors.toList()),
				ft.getFtRank(), favFts.contains(ft.getFtId()));
	}
    
    // 獲取指定占卜師的詳細訊息（FtDTO）
    public FtDTO getFtById(Integer ftId) {
		FtVO ft = ftListRepository.findById(ftId).orElse(null);
		return new FtDTO(ft.getFtId(), ft.getCompanyName(), ft.getNickname(), ft.getPhoto(), ft.getIntro(),
				ft.getPrice(), personalSkillService.getFtSkillsByFtId(ftId).stream().map(FtSkillVO::getSkillName)
						.collect(Collectors.toList()),
				ft.getFtRank(), false);
	}

    public List<FtDTO> getFtByFavFts(Integer memId) {
        List<FtVO> fts = ftListRepository.findByFavFts(memId);

        // 將每個 FtVO 轉換為 FtDTO
        return fts.stream()
                .map(ft -> new FtDTO(ft.getFtId(), ft.getCompanyName(), ft.getNickname(), ft.getPhoto(), ft.getIntro(),
                        ft.getPrice(), personalSkillService.getFtSkillsByFtId(ft.getFtId()).stream()
                        .map(FtSkillVO::getSkillName).collect(Collectors.toList()),
                        ft.getFtRank(), true))
                .collect(Collectors.toList());
    }
    
    public void validateReservation(Integer ftId, Integer memId) {
        // 查詢占卜師所屬的會員編號
        Optional<FtVO> ftOptional = ftListRepository.findById(ftId);

        if (ftOptional.isPresent()) {
            FtVO ft = ftOptional.get();
            if (ft.getMember().getMemberId().equals(memId)) {
                throw new IllegalArgumentException("您不能預約自己的占卜服務！");
            }
        }
    }

    public String getNicknameByFtId(Integer ftId) {
        // 從 Repository 查詢 FtVO，然後取得 nickname
        return ftListRepository.findById(ftId)
                .map(FtVO::getNickname) // 如果找到 FtVO，返回 nickname
                .orElse("未找到占卜師暱稱"); // 若找不到則返回預設值
    }
    
}
