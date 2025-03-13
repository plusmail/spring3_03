package kroryi.spring.service;

import kroryi.spring.dto.MemberJoinDTO;
import kroryi.spring.entity.Member;
import kroryi.spring.entity.MemberRole;
import kroryi.spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDTO dto) throws MidExitException,MpwExitException {

        String mid = dto.getMid();
        boolean exist = memberRepository.existsById(mid);
        if (exist) {
            throw new MidExitException();
        }

        if(!dto.getMpw().equals(dto.getRe_mpw())){
            throw new MpwExitException();
        }

        Member member = modelMapper.map(dto, Member.class);
        member.changePassword(passwordEncoder.encode(dto.getMpw()));
        member.addRole(MemberRole.USER);
        log.info("--------------------");
        log.info(member);
        log.info(member.getRoleSet());

        memberRepository.save(member);
    }
}
