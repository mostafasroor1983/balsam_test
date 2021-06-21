package com.balsam.test1.service.impl;

import com.balsam.test1.domain.ServicePackage;
import com.balsam.test1.repository.ServicePackageRepository;
import com.balsam.test1.service.ServicePackageService;
import com.balsam.test1.service.dto.ServicePackageDTO;
import com.balsam.test1.service.mapper.ServicePackageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ServicePackage}.
 */
@Service
@Transactional
public class ServicePackageServiceImpl implements ServicePackageService {

    private final Logger log = LoggerFactory.getLogger(ServicePackageServiceImpl.class);

    private final ServicePackageRepository servicePackageRepository;

    private final ServicePackageMapper servicePackageMapper;

    public ServicePackageServiceImpl(ServicePackageRepository servicePackageRepository, ServicePackageMapper servicePackageMapper) {
        this.servicePackageRepository = servicePackageRepository;
        this.servicePackageMapper = servicePackageMapper;
    }

    @Override
    public ServicePackageDTO save(ServicePackageDTO servicePackageDTO) {
        log.debug("Request to save ServicePackage : {}", servicePackageDTO);
        ServicePackage servicePackage = servicePackageMapper.toEntity(servicePackageDTO);
        servicePackage = servicePackageRepository.save(servicePackage);
        return servicePackageMapper.toDto(servicePackage);
    }

    @Override
    public Optional<ServicePackageDTO> partialUpdate(ServicePackageDTO servicePackageDTO) {
        log.debug("Request to partially update ServicePackage : {}", servicePackageDTO);

        return servicePackageRepository
            .findById(servicePackageDTO.getId())
            .map(
                existingServicePackage -> {
                    servicePackageMapper.partialUpdate(existingServicePackage, servicePackageDTO);
                    return existingServicePackage;
                }
            )
            .map(servicePackageRepository::save)
            .map(servicePackageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServicePackageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ServicePackages");
        return servicePackageRepository.findAll(pageable).map(servicePackageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServicePackageDTO> findOne(Long id) {
        log.debug("Request to get ServicePackage : {}", id);
        return servicePackageRepository.findById(id).map(servicePackageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServicePackage : {}", id);
        servicePackageRepository.deleteById(id);
    }
}
