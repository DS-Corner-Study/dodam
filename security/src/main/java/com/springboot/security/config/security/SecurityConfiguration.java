package com.springboot.security.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 어플리케이션의 보안 설정

@Configuration
//@EnableWebSecurity // Spring Security에 대한 디버깅 모드를 사용하기 위한 어노테이션 (default : false)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic().disable() // REST API는 UI를 사용하지 않으므로 기본설정을 비활성화

            .csrf().disable() // REST API는 csrf 보안이 필요 없으므로 비활성화

            .sessionManagement()
            .sessionCreationPolicy(
                SessionCreationPolicy.STATELESS) // JWT Token 인증방식으로 세션은 필요 없으므로 비활성화

            .and()
            .authorizeRequests() // 리퀘스트에 대한 사용권한 체크
            .antMatchers("/sign-api/sign-in", "/sign-api/sign-up",
                "/sign-api/exception").permitAll() // 가입 및 로그인 주소는 허용
            .antMatchers(HttpMethod.GET, "/product/**").permitAll() // product로 시작하는 Get 요청은 허용

            .antMatchers("**exception**").permitAll()

            .anyRequest().hasRole("ADMIN") // 나머지 요청은 인증된 ADMIN만 접근 가능

            .and()
            .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler()) // 권한을 확인하는 과정에서 통과하지 못하는 예외가 발생할 경우 예외를 전달
            .and()
            .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증 과정에서 예외가 발생할 경우 예외를 전달

            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class); // JWT Token 필터를 id/password 인증 필터 이전에 추가
    }

    /**
     * Swagger 페이지 접근에 대한 예외 처리, 인증, 인가를 무시하는 경로를 설정한 것
     *
     * @param webSecurity
     */
    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
            "/swagger-ui.html", "/webjars/**", "/swagger/**", "/sign-api/exception");
    }
}