package com.cia103g5.user.ftlist.model;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cia103g5.user.ft.model.FtVO;

public interface FtListRepository extends JpaRepository<FtVO, Integer> {

	// 根據(公司、暱稱、專長、時間、價格、等級)查詢占卜師列表
	@Query(value = "SELECT DISTINCT ft.* " +
				"FROM ft_info ft " +
				"LEFT JOIN ft_service ps ON ps.ft_id = ft.ft_id " +
				"LEFT JOIN available_time atv ON atv.ft_id = ft.ft_id " +
				"WHERE ft.status = 1 " +
				"AND (?1 IS NULL OR ft.company_name LIKE CONCAT('%', ?1, '%')) " +
				"AND (?2 IS NULL OR ft.nickname LIKE CONCAT('%', ?2, '%')) " +
				"AND (?3 IS NULL OR ps.skill_no = ?3) " +
				"AND (?4 IS NULL OR (atv.start_time >= ?4 AND atv.start_time < DATE_ADD(?4, INTERVAL 1 DAY) AND atv.status = 0)) " +
				"AND (?5 IS NULL OR ft.price >= ?5) " +
				"AND (?6 IS NULL OR ft.price <= ?6) " +
				"AND (?7 IS NULL OR ft.ft_rank = ?7)",
				nativeQuery = true)
	List<FtVO> findAllByConditions(String companyName,
					String nickname,
					Integer skillNo,
					Date startTime,
					Integer minPrice,
					Integer maxPrice,
					Integer ftRank
	);
	
	// 根據會員 ID 查詢該會員收藏的所有占卜師
	@Query("select ftv from FtVO ftv INNER JOIN FavFtVO favftv ON favftv.ftVO.ftId = ftv.ftId where favftv.memberVO.memberId = ?1 order by ftv.ftId")
	List<FtVO> findByFavFts(Integer memberId);
        
	// 根據占卜師 ID 查詢占卜師的詳細資訊
	Optional<FtVO> findById(Integer ftId);

}
