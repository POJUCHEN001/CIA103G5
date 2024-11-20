package com.cia103g5.user.admin.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**############################
 #                            #
 #       管理員 service        #
 #                            #
 ############################ */

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public AdminVo findById(Integer id) {
        return adminRepository.findById(id).orElse(null);
    }

    public List<AdminVo> findAll() {
        return adminRepository.findAll();
    }

    public void save(AdminVo admin) {
        adminRepository.save(admin);
    }
}
