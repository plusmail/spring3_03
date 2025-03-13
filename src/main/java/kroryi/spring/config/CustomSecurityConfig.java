package kroryi.spring.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {

    private final DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        return username -> {
//            log.info("로딩 Username: {}", username);
//            return User.builder()
//                    .username(username)
//                    .password(passwordEncoder.encode("1111")) // ✅ 반드시 암호화된 비밀번호 사용
//                    .authorities("ROLE_USER")
//                    .build();
//        };
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {

        log.info("------------------- security configure ---------------------");
        http
                .csrf(AbstractHttpConfigurer::disable)// 실무에서는 disable하면 안됨.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/login/oauth2/code/kakao").permitAll()
                        .requestMatchers("/member/join").permitAll()
                        .requestMatchers("/board/list").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("username")
                        .loginProcessingUrl("/login-proc")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")

                )
                .rememberMe(me -> me
                        .key("12345678")
                        .tokenRepository(persistentTokenRepository())
                        .userDetailsService(userDetailsService)
                        .tokenValiditySeconds(60 * 60 * 24 * 30)
                )
                .oauth2Login(login->login.loginPage("/login"))
                .logout(logout -> logout
//                        .logoutSuccessUrl("/")
                                .deleteCookies("JSESSIONID")
                                .invalidateHttpSession(true)
                );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("------------------- web configure ---------------------");
//        static 내에는 현재는 js, css, image 등 자원에 대한 접근을 풀어준 설정
        return (web) ->
                web.ignoring()
                        .requestMatchers(
                                PathRequest.toStaticResources().atCommonLocations()
                        );

    }


}
