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

    public AdminVO findById(Integer id) {
        return adminRepository.findById(id).orElse(null);
    }

    public List<AdminVO> findAll() {
        return adminRepository.findAll();
    }

    public void save(AdminVO admin) {
        adminRepository.save(admin);
    }
}
