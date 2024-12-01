package com.cia103g5.user.admin.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**############################
 #                            #
 #       管理員 Repository     #
 #                            #
 ############################ */


@Repository
public interface AdminRepository extends JpaRepository<AdminVO, Integer> {


}
