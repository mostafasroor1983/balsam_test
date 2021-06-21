package com.balsam.test1.service.impl;

import com.balsam.test1.domain.ValidationRequestFile;
import com.balsam.test1.repository.ValidationRequestFileRepository;
import com.balsam.test1.service.ValidationRequestFileService;
import com.balsam.test1.service.dto.ValidationRequestFileDTO;
import com.balsam.test1.service.mapper.ValidationRequestFileMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ValidationRequestFile}.
 */
@Service
@Transactional
public class ValidationRequestFileServiceImpl implements ValidationRequestFileService {

    private final Logger log = LoggerFactory.getLogger(ValidationRequestFileServiceImpl.class);

    private final ValidationRequestFileRepository validationRequestFileRepository;

    private final ValidationRequestFileMapper validationRequestFileMapper;

    public ValidationRequestFileServiceImpl(
        ValidationRequestFileRepository validationRequestFileRepository,
        ValidationRequestFileMapper validationRequestFileMapper
    ) {
        this.validationRequestFileRepository = validationRequestFileRepository;
        this.validationRequestFileMapper = validationRequestFileMapper;
    }

    @Override
    public ValidationRequestFileDTO save(ValidationRequestFileDTO validationRequestFileDTO) {
        log.debug("Request to save ValidationRequestFile : {}", validationRequestFileDTO);
        ValidationRequestFile validationRequestFile = validationRequestFileMapper.toEntity(validationRequestFileDTO);
        validationRequestFile = validationRequestFileRepository.save(validationRequestFile);
        return validationRequestFileMapper.toDto(validationRequestFile);
    }

    @Override
    public Optional<ValidationRequestFileDTO> partialUpdate(ValidationRequestFileDTO validationRequestFileDTO) {
        log.debug("Request to partially update ValidationRequestFile : {}", validationRequestFileDTO);

        return validationRequestFileRepository
            .findById(validationRequestFileDTO.getId())
            .map(
                existingValidationRequestFile -> {
                    validationRequestFileMapper.partialUpdate(existingValidationRequestFile, validationRequestFileDTO);
                    return existingValidationRequestFile;
                }
            )
            .map(validationRequestFileRepository::save)
            .map(validationRequestFileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ValidationRequestFileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ValidationRequestFiles");
        return validationRequestFileRepository.findAll(pageable).map(validationRequestFileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ValidationRequestFileDTO> findOne(Long id) {
        log.debug("Request to get ValidationRequestFile : {}", id);
        return validationRequestFileRepository.findById(id).map(validationRequestFileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ValidationRequestFile : {}", id);
        validationRequestFileRepository.deleteById(id);
    }
}
