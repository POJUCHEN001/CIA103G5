package com.cia103g5.user.favft.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cia103g5.user.ft.model.FtVO;

@Service
public class FavFtService {

	@Autowired
	private FavFtRepository repository;

	// 取得會員收藏的占卜師編號
	public List<Integer> getFavFtsByMemId(Integer memId) {
		return repository.findByMemId(memId).stream().map(FavFtVO::getFtVO).map(FtVO::getFtId)
				.collect(Collectors.toList());
	}

	// 加入收藏
	public void addFavoriteFt(Integer memId, Integer ftId) {
		repository.insert(memId, ftId);
	}

	// 取消收藏
	public void deleteFavoriteFt(Integer memId, Integer ftId) {
		repository.delete(memId, ftId);
	}

}
