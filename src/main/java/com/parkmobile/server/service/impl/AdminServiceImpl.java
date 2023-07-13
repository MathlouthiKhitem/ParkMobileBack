package com.parkmobile.server.service.impl;

import com.parkmobile.server.domain.Admin;
import com.parkmobile.server.repository.AdminRepository;
import com.parkmobile.server.service.AdminService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Admin}.
 */
@Service
public class AdminServiceImpl implements AdminService {

    private final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin save(Admin admin) {
        log.debug("Request to save Admin : {}", admin);
        return adminRepository.save(admin);
    }

    @Override
    public Admin update(Admin admin) {
        log.debug("Request to update Admin : {}", admin);
        // no save call needed as we have no fields that can be updated
        return admin;
    }

    @Override
    public Optional<Admin> partialUpdate(Admin admin) {
        log.debug("Request to partially update Admin : {}", admin);

        return adminRepository
            .findById(admin.getId())
            .map(existingAdmin -> {
                return existingAdmin;
            })// .map(adminRepository::save)
        ;
    }

    @Override
    public List<Admin> findAll() {
        log.debug("Request to get all Admins");
        return adminRepository.findAll();
    }

    @Override
    public Optional<Admin> findOne(String id) {
        log.debug("Request to get Admin : {}", id);
        return adminRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Admin : {}", id);
        adminRepository.deleteById(id);
    }
}
