package kroryi.spring.service;

import kroryi.spring.dto.MemberJoinDTO;
import kroryi.spring.dto.MemberSecurityDTO;
import org.springframework.stereotype.Service;

public interface MemberService {

    static class MidExitException extends Exception {

    }

    static class MpwExitException extends Exception {

    }

    void join(MemberJoinDTO dto) throws MidExitException, MpwExitException;

    void updateMember(MemberSecurityDTO dto) throws MidExitException, MpwExitException;
}
