package com.example.testjwtserver.config;

import com.example.testjwtserver.config.jwt.JwtAuthenticationFilter;
import com.example.testjwtserver.config.jwt.JwtAuthorizationFilter;
import com.example.testjwtserver.filter.MyFilter3;
import com.example.testjwtserver.filter.MyFilter4;
import com.example.testjwtserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // SecurityContextPersistenceFilter : 스프링 시큐리티 필터 이전에 호출 (가장먼저)
        // TODO. 임시 주석
//        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);
        
        
        // BasicAuthenticationFilter 다음 호출됨
//        http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class);
//        http.addFilterAfter(new MyFilter4(), BasicAuthenticationFilter.class);

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter)  // Controller 어노테이트 @CrossOrigin(인증 X), 시큐리티 필터에 등록 인증(O)

                .formLogin().disable()
                .httpBasic().disable()

                .addFilter(new JwtAuthenticationFilter(authenticationManager()))   // AuthenticationManager
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))

                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
//                .and().formLogin().loginProcessingUrl("/login")
        ;
    }
}
