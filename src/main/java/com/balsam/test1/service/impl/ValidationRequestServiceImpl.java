package com.balsam.test1.service.impl;

import com.balsam.test1.domain.ValidationRequest;
import com.balsam.test1.repository.ValidationRequestRepository;
import com.balsam.test1.service.ValidationRequestService;
import com.balsam.test1.service.dto.ValidationRequestDTO;
import com.balsam.test1.service.mapper.ValidationRequestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ValidationRequest}.
 */
@Service
@Transactional
public class ValidationRequestServiceImpl implements ValidationRequestService {

    private final Logger log = LoggerFactory.getLogger(ValidationRequestServiceImpl.class);

    private final ValidationRequestRepository validationRequestRepository;

    private final ValidationRequestMapper validationRequestMapper;

    public ValidationRequestServiceImpl(
        ValidationRequestRepository validationRequestRepository,
        ValidationRequestMapper validationRequestMapper
    ) {
        this.validationRequestRepository = validationRequestRepository;
        this.validationRequestMapper = validationRequestMapper;
    }

    @Override
    public ValidationRequestDTO save(ValidationRequestDTO validationRequestDTO) {
        log.debug("Request to save ValidationRequest : {}", validationRequestDTO);
        ValidationRequest validationRequest = validationRequestMapper.toEntity(validationRequestDTO);
        validationRequest = validationRequestRepository.save(validationRequest);
        return validationRequestMapper.toDto(validationRequest);
    }

    @Override
    public Optional<ValidationRequestDTO> partialUpdate(ValidationRequestDTO validationRequestDTO) {
        log.debug("Request to partially update ValidationRequest : {}", validationRequestDTO);

        return validationRequestRepository
            .findById(validationRequestDTO.getId())
            .map(
                existingValidationRequest -> {
                    validationRequestMapper.partialUpdate(existingValidationRequest, validationRequestDTO);
                    return existingValidationRequest;
                }
            )
            .map(validationRequestRepository::save)
            .map(validationRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ValidationRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ValidationRequests");
        return validationRequestRepository.findAll(pageable).map(validationRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ValidationRequestDTO> findOne(Long id) {
        log.debug("Request to get ValidationRequest : {}", id);
        return validationRequestRepository.findById(id).map(validationRequestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ValidationRequest : {}", id);
        validationRequestRepository.deleteById(id);
    }
}
