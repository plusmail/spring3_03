package kroryi.spring.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

    @Bean
    public ModelMapper getMapper(){
        ModelMapper modelMapper=new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        // STRICT 완전히 일치, STANDARD 대부분 일치,
        // LOOSE (fileName, name 같을걸루 취급해서 오류 날 확률이 높다) 구조적으로 유사
        return modelMapper;
    }

}
