package com.balsam.test1.service.impl;

import com.balsam.test1.domain.ServicePackageType;
import com.balsam.test1.repository.ServicePackageTypeRepository;
import com.balsam.test1.service.ServicePackageTypeService;
import com.balsam.test1.service.dto.ServicePackageTypeDTO;
import com.balsam.test1.service.mapper.ServicePackageTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ServicePackageType}.
 */
@Service
@Transactional
public class ServicePackageTypeServiceImpl implements ServicePackageTypeService {

    private final Logger log = LoggerFactory.getLogger(ServicePackageTypeServiceImpl.class);

    private final ServicePackageTypeRepository servicePackageTypeRepository;

    private final ServicePackageTypeMapper servicePackageTypeMapper;

    public ServicePackageTypeServiceImpl(
        ServicePackageTypeRepository servicePackageTypeRepository,
        ServicePackageTypeMapper servicePackageTypeMapper
    ) {
        this.servicePackageTypeRepository = servicePackageTypeRepository;
        this.servicePackageTypeMapper = servicePackageTypeMapper;
    }

    @Override
    public ServicePackageTypeDTO save(ServicePackageTypeDTO servicePackageTypeDTO) {
        log.debug("Request to save ServicePackageType : {}", servicePackageTypeDTO);
        ServicePackageType servicePackageType = servicePackageTypeMapper.toEntity(servicePackageTypeDTO);
        servicePackageType = servicePackageTypeRepository.save(servicePackageType);
        return servicePackageTypeMapper.toDto(servicePackageType);
    }

    @Override
    public Optional<ServicePackageTypeDTO> partialUpdate(ServicePackageTypeDTO servicePackageTypeDTO) {
        log.debug("Request to partially update ServicePackageType : {}", servicePackageTypeDTO);

        return servicePackageTypeRepository
            .findById(servicePackageTypeDTO.getId())
            .map(
                existingServicePackageType -> {
                    servicePackageTypeMapper.partialUpdate(existingServicePackageType, servicePackageTypeDTO);
                    return existingServicePackageType;
                }
            )
            .map(servicePackageTypeRepository::save)
            .map(servicePackageTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServicePackageTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ServicePackageTypes");
        return servicePackageTypeRepository.findAll(pageable).map(servicePackageTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServicePackageTypeDTO> findOne(Long id) {
        log.debug("Request to get ServicePackageType : {}", id);
        return servicePackageTypeRepository.findById(id).map(servicePackageTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServicePackageType : {}", id);
        servicePackageTypeRepository.deleteById(id);
    }
}
