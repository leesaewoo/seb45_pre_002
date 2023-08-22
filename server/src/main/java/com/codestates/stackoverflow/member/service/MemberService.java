package com.codestates.stackoverflow.member.service;

import com.codestates.stackoverflow.exception.BusinessLogicException;
import com.codestates.stackoverflow.exception.ExceptionCode;
import com.codestates.stackoverflow.member.entity.Member;
import com.codestates.stackoverflow.member.repository.MemberRepository;
import com.codestates.stackoverflow.security.utils.CustomAuthorityUtils;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final CustomAuthorityUtils authorityUtils;

    public Member createMember(Member member) {
        verifyExistMember(member);

        member.setPassword(member.getPassword());
        member.setRoles(authorityUtils.createRoles());

        return repository.save(member);
    }

    public Member updateMember(Member member) {
        Member updateMember = findVerifiedMember(member.getMemberId());

        Optional.ofNullable(member.getEmail())
                .ifPresent(email -> updateMember.setEmail(email));
        Optional.ofNullable(member.getUsername())
                .ifPresent(username -> updateMember.setUsername(username));
        Optional.ofNullable(member.getPassword())
                .ifPresent(password -> updateMember.setPassword(passwordEncoder.encode(password)));

        return updateMember;
    }

    public Member changePassword(long memberId, String oldPassword, String newPassword) {
        Member findMember = findVerifiedMember(memberId);

        if(oldPassword.equals(findMember.getPassword())) {
            findMember.setPassword(newPassword);
            return findMember;
        }
        else {
            throw new BusinessLogicException(ExceptionCode.PASSWORD_NOT_MATCHED);
        }
    }

    public Member findMember(long memberId) {
        return findVerifiedMember(memberId);
    }

    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember =
                repository.findById(memberId);

        Member findMember =
                optionalMember.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return findMember;
    }

    private void verifyExistMember(Member info) {
        Optional<Member> member = repository.findByEmail(info.getEmail());
        //member에 repository에 저장된 이메일을 저장.
        if (member.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }
}
