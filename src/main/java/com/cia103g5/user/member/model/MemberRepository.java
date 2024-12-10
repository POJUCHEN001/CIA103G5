package com.cia103g5.user.member.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MemberRepository extends JpaRepository<MemberVO, Integer> {

	// 查詢帳號和密碼
	Optional<MemberVO> findByAccountAndPassword(String account, String password);

	// 根據會員ID查詢所有資料 JPA內建的

	// 查詢所有會員 JPA內建方法 findAll()

	// 根據帳號查詢會員
	Optional<MemberVO> findByAccount(String account);


    // 修改會員資料
	@Modifying
	@Transactional
    @Query("UPDATE MemberVO m SET m.name = :name, m.email = :email, m.nickname = :nickname, m.phone = :phone WHERE m.memberId = :memberId")
    int updateMemberInfo(@Param("name") String name,
    					 @Param("email") String email, 
    					 @Param("nickname") String nickname,
    					 @Param("phone") String phone,
    					 @Param("memberId") String memberId
    					 );
    
    // 變更會員狀態
    @Modifying
    @Transactional
    @Query("UPDATE MemberVO m SET m.status = :status WHERE m.memberId = :memberId")
    int updateMemberStatus(@Param("status") Integer status,
    					   @Param("memberId") Integer memberId
    					   );

    // 
    @Query("SELECT m FROM MemberVO m WHERE " +
    	       "(:isNumber = true AND m.memberId = :memberId) OR " +
    	       "(:isNumber = false AND m.account = :account)")
	Optional<MemberVO> findByAccountOrMemberId(@Param("isNumber") boolean isNumber,
	                                           @Param("memberId") Integer memberId,
	                                           @Param("account") String account);

    // 根據email查會員
	Optional<MemberVO> findByEmail(String email);

    
    
}
