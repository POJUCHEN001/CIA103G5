package com.cia103g5.user.memreservation.model;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cia103g5.user.availabletime.model.AvailableTimeService;
import com.cia103g5.user.availabletime.model.AvailableTimeVO;
import com.cia103g5.user.ftlist.model.FtDTO;
import com.cia103g5.user.ftlist.model.FtListService;

@Service
public class MemReservationService {

	@Autowired
	MemReservationRepository memReservationRepository;

	@Autowired
	FtListService ftListService;

	@Autowired
	AvailableTimeService availableTimeService;
	
	@Transactional
	public void insert(int memId, int availableTimeNo) {
		AvailableTimeVO availableTimeVO = availableTimeService.getAvailableTimeById(availableTimeNo);
		Integer ftId = availableTimeVO.getFtVO().getFtId();
		FtDTO ftDTO = ftListService.getFtById(memId, ftId);
		Integer price = 0;
		if (ftDTO.getPrice() != null) {
			price = ftDTO.getPrice();
		}
		Timestamp createdTime = new Timestamp(System.currentTimeMillis());
		Integer payment = 0;
		Integer rsvStatus = 0;
		memReservationRepository.insert(memId, ftId, availableTimeNo, price, createdTime, payment, rsvStatus);
		availableTimeService.updateStatus(ftId, availableTimeNo, 1);
	}
	
	@Transactional
	public void addReservation(Integer memId, Integer ftId, Integer availableTimeNo, Integer price, Integer skillNo) {
		memReservationRepository.insert(memId, ftId, availableTimeNo, price, skillNo);
	}
	
	public Integer getSingleMemIdByAvailableTimeNo(Integer availableTimeNo) {
        return memReservationRepository.findSingleMemIdByAvailableTimeNo(availableTimeNo)
                .orElse(-1); // 當值不存在時返回 -1 作為預設值
    }
	
	@Transactional
    public void updatePaymentToOneByAvailableTimeNo(Integer availableTimeNo) {
        if (availableTimeNo == null) {
            throw new IllegalArgumentException("availableTimeNo 不能為空");
        }
        memReservationRepository.updatePaymentByAvailableTimeNo(availableTimeNo);
    }
	
}
