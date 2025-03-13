package kroryi.spring.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Log4j2
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("----------------userRequest: {}", userRequest);
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        log.info("-------------clientName: {}", clientName);
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String email = null;
        switch (clientName) {
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;
            default:
        }

        log.info("-------------email: {}", email);


        return oAuth2User;
    }

    private String getKakaoEmail(Map<String, Object> paramMap) {
        log.info("-------------KAKAO paramMap: {}", paramMap);
        Object value = paramMap.get("kakao_account");
        log.info("-------------value: {}", value);

        LinkedHashMap accountmap = (LinkedHashMap) value;
        String email = (String) accountmap.get("email");
        log.info("-------------email: {}", email);
        return email;

    }
}
