package com.cia103g5.user.ftgrade.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FtGradeService {

    @Autowired
    private FtGradeRepository ftGradeRepository;
    
	// 取得所有 FtGrade 資料 (added by 52)
	public List<FtGrade> getAll() {
		return ftGradeRepository.findAll();
	}

}
