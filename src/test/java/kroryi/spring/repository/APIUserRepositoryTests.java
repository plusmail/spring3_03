package kroryi.spring.repository;


import kroryi.spring.entity.APIUser;
import kroryi.spring.repository.api.APIUserRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class APIUserRepositoryTests {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private APIUserRepository apiUserRepository;

    @Test
    public void testInsert() {
        IntStream.range(0, 10).forEach(i -> {

            APIUser apiUser = APIUser.builder()
                    .mid("apiuser" +i)
                    .mpw(passwordEncoder.encode("1111"))
                    .build();
            apiUserRepository.save(apiUser);
        });
    }

}
