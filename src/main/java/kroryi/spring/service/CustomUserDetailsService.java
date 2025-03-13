package kroryi.spring.service;

import kroryi.spring.dto.MemberSecurityDTO;
import kroryi.spring.entity.Member;
import kroryi.spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername:{}", username);

        Optional<Member> result = memberRepository.getWithRoles(username);
        if(result.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        Member member = result.get();
        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getMid(),
                member.getMpw(),
                member.getEmail(),
                member.getDel(),
                false,
                member.getRoleSet().stream().map(role ->
                        new SimpleGrantedAuthority("ROLE_"+role.name()))
                        .collect(Collectors.toList())
        );
        log.info("memberSecurityDTO", memberSecurityDTO);

        return memberSecurityDTO;
    }
}
