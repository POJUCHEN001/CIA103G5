package com.cia103g5.user.favft.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cia103g5.user.ft.model.FtVO;

@Service
public class FavFtService {

	@Autowired
	private FavFtRepository favFtrepository;

	// 取得會員收藏的占卜師編號
	public List<Integer> getFavFtsByMemId(Integer memId) {
		return favFtrepository.findByMemId(memId).stream().map(FavFtVO::getFtVO).map(FtVO::getFtId)
				.collect(Collectors.toList());
	}

	// 加入收藏
	public void addFavoriteFt(Integer memId, Integer ftId) {
		favFtrepository.insert(memId, ftId);
	}

	// 取消收藏
	public void deleteFavoriteFt(Integer memId, Integer ftId) {
		favFtrepository.delete(memId, ftId);
	}

}
