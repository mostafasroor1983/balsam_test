package com.balsam.test1.service.impl;

import com.balsam.test1.domain.Corporate;
import com.balsam.test1.repository.CorporateRepository;
import com.balsam.test1.service.CorporateService;
import com.balsam.test1.service.dto.CorporateDTO;
import com.balsam.test1.service.mapper.CorporateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Corporate}.
 */
@Service
@Transactional
public class CorporateServiceImpl implements CorporateService {

    private final Logger log = LoggerFactory.getLogger(CorporateServiceImpl.class);

    private final CorporateRepository corporateRepository;

    private final CorporateMapper corporateMapper;

    public CorporateServiceImpl(CorporateRepository corporateRepository, CorporateMapper corporateMapper) {
        this.corporateRepository = corporateRepository;
        this.corporateMapper = corporateMapper;
    }

    @Override
    public CorporateDTO save(CorporateDTO corporateDTO) {
        log.debug("Request to save Corporate : {}", corporateDTO);
        Corporate corporate = corporateMapper.toEntity(corporateDTO);
        corporate = corporateRepository.save(corporate);
        return corporateMapper.toDto(corporate);
    }

    @Override
    public Optional<CorporateDTO> partialUpdate(CorporateDTO corporateDTO) {
        log.debug("Request to partially update Corporate : {}", corporateDTO);

        return corporateRepository
            .findById(corporateDTO.getId())
            .map(
                existingCorporate -> {
                    corporateMapper.partialUpdate(existingCorporate, corporateDTO);
                    return existingCorporate;
                }
            )
            .map(corporateRepository::save)
            .map(corporateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CorporateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Corporates");
        return corporateRepository.findAll(pageable).map(corporateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CorporateDTO> findOne(Long id) {
        log.debug("Request to get Corporate : {}", id);
        return corporateRepository.findById(id).map(corporateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Corporate : {}", id);
        corporateRepository.deleteById(id);
    }
}
