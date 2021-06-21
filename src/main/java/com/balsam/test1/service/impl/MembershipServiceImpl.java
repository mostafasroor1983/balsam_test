package com.balsam.test1.service.impl;

import com.balsam.test1.domain.Membership;
import com.balsam.test1.repository.MembershipRepository;
import com.balsam.test1.service.MembershipService;
import com.balsam.test1.service.dto.MembershipDTO;
import com.balsam.test1.service.mapper.MembershipMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Membership}.
 */
@Service
@Transactional
public class MembershipServiceImpl implements MembershipService {

    private final Logger log = LoggerFactory.getLogger(MembershipServiceImpl.class);

    private final MembershipRepository membershipRepository;

    private final MembershipMapper membershipMapper;

    public MembershipServiceImpl(MembershipRepository membershipRepository, MembershipMapper membershipMapper) {
        this.membershipRepository = membershipRepository;
        this.membershipMapper = membershipMapper;
    }

    @Override
    public MembershipDTO save(MembershipDTO membershipDTO) {
        log.debug("Request to save Membership : {}", membershipDTO);
        Membership membership = membershipMapper.toEntity(membershipDTO);
        membership = membershipRepository.save(membership);
        return membershipMapper.toDto(membership);
    }

    @Override
    public Optional<MembershipDTO> partialUpdate(MembershipDTO membershipDTO) {
        log.debug("Request to partially update Membership : {}", membershipDTO);

        return membershipRepository
            .findById(membershipDTO.getId())
            .map(
                existingMembership -> {
                    membershipMapper.partialUpdate(existingMembership, membershipDTO);
                    return existingMembership;
                }
            )
            .map(membershipRepository::save)
            .map(membershipMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MembershipDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Memberships");
        return membershipRepository.findAll(pageable).map(membershipMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MembershipDTO> findOne(Long id) {
        log.debug("Request to get Membership : {}", id);
        return membershipRepository.findById(id).map(membershipMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Membership : {}", id);
        membershipRepository.deleteById(id);
    }
}
