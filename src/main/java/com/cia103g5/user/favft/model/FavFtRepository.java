package com.cia103g5.user.favft.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FavFtRepository extends JpaRepository<FavFtVO, FavFtVO.FavFtKey> {

	// 取得會員收藏的占卜師
	@Query("select atv from FavFtVO atv where atv.memberVO.memberId = ?1 order by atv.ftVO.ftId")
	List<FavFtVO> findByMemId(int memId);

	// 加入收藏
	@Transactional
	@Modifying
	@Query(value = "insert into fav_ft (mem_id, ft_id) values (?1, ?2)", nativeQuery = true)
	void insert(int memId, int ftId);

	// 刪除收藏記錄
	@Transactional
	@Modifying
	@Query(value = "delete from fav_ft where mem_id = ?1 and ft_id = ?2", nativeQuery = true)
	void delete(int memId, int ftId);

}
