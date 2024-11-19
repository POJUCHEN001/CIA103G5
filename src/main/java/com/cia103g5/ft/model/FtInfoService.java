package com.cia103g5.ft.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FtInfoService {

	@Autowired
	private FtInfoRepository ftInfoRepository;

	public FtInfo saveMember(FtInfo ft) {
		return ftInfoRepository.save(ft);
	}

	// 根據 memId 獲取占卜師資料
	public FtInfo getFtInfoByMemId(Integer memId) {
		return ftInfoRepository.findById(memId).orElseThrow(() -> new RuntimeException("占卜師資料不存在"));
	}
	
	// 啟用占卜師會員
		public void activateFtInfo(Integer memId) {
			FtInfo ftInfo = getFtInfoByMemId(memId);

			// 更新狀態為啟用
			ftInfo.setStatus(1);

			// 恢復默認可用權限
			ftInfo.setCanPost(1);
			ftInfo.setCanRev(1);
			ftInfo.setCanSell(1);
			
			// 清除停權時間
	        ftInfo.setActionStartedAt(null);
	        ftInfo.setActionEndedAt(null);

			// 保存到資料庫
			ftInfoRepository.save(ftInfo);
		}

	// 檢查占卜師是否可以執行某操作
	public boolean canPerformAction(Integer memId, String actionType) {
		FtInfo ftInfo = getFtInfoByMemId(memId);

		// 如果狀態為停權（0），則禁止操作
		if (ftInfo.getStatus() == 0) {
			return false;
		}

		// 當前時間
		LocalDateTime now = LocalDateTime.now();

		// 檢查可用時間範圍
		if (ftInfo.getActionStartedAt() != null && ftInfo.getActionEndedAt() != null) {
			if (now.isBefore(ftInfo.getActionStartedAt().toLocalDateTime())
					|| now.isAfter(ftInfo.getActionEndedAt().toLocalDateTime())) {
				return false;
			}
		}

		// 根據操作類型檢查
		switch (actionType.toLowerCase()) {
		case "post": // 發文
			return ftInfo.getCanPost() == 1;

		case "resv": // 接受預約
			return ftInfo.getCanRev() == 1;

		case "sell": // 上架
			return ftInfo.getCanSell() == 1;

		default:
			throw new IllegalArgumentException("未知的操作類型: " + actionType);
		}
	}

	// 更新占卜師狀態為停權
	public void suspendFtInfo(Integer memId) {
		FtInfo ftInfo = getFtInfoByMemId(memId);

		// 更新狀態為停權
		ftInfo.setStatus(0);
		ftInfo.setCanPost(0); // 禁止發文
		ftInfo.setCanRev(0); // 禁止接受預約
		ftInfo.setCanSell(0); // 禁止上架

		// 設置停權結束時間為當前時間 + 3 天
		LocalDateTime now = LocalDateTime.now();
		ftInfo.setActionStartedAt(Timestamp.valueOf(now));
	    ftInfo.setActionEndedAt(Timestamp.valueOf(now.plusDays(3)));

	    
        ftInfoRepository.save(ftInfo);
	}
	
	// 定時檢查停權到期的占卜師並自動恢復
    @Scheduled(cron = "0 0 * * * *") // 每小時執行一次
    public void checkAndRestoreFtStatus() {
        LocalDateTime now = LocalDateTime.now();

        // 查詢所有狀態為停權且停權結束時間已過的占卜師
        List<FtInfo> suspendedFtInfos = ftInfoRepository.findByStatusAndActionEndedAtBefore(0, now);

        for (FtInfo ftInfo : suspendedFtInfos) {
            // 恢復占卜師狀態
            activateFtInfo(ftInfo.getFtId());
            System.out.println("占卜師 " + ftInfo.getFtId() + " 已自動解除停權");
        }
    }
    

	
	

}
