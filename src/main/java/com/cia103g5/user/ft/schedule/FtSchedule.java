package com.cia103g5.user.ft.schedule;

import com.cia103g5.user.ft.model.FtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FtSchedule {
    @Autowired
    private FtService ftService;

    // 每小時執行一次
    @Scheduled(fixedRate = 3600000) // 3600000 毫秒 = 1 小時
    public void scheduleReactivationCheck() {
        ftService.reactivateSuspendedRecords();
    }
}
