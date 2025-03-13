package kroryi.spring.service;

import kroryi.spring.dto.MemberJoinDTO;
import org.springframework.stereotype.Service;

public interface MemberService {

    static class MidExitException extends Exception {

    }

    static class MpwExitException extends Exception {

    }

    void join(MemberJoinDTO dto) throws MidExitException, MpwExitException;
}
