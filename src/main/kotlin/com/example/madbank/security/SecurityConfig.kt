package com.example.madbank.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.web.filter.CharacterEncodingFilter


@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    public fun filterChain(http: HttpSecurity):SecurityFilterChain
    {
        var filter: CharacterEncodingFilter = CharacterEncodingFilter()

        filter.encoding = "UTF-8"
        filter.setForceEncoding(true)

        http
                .authorizeHttpRequests()
                .antMatchers("/signup/**", "/login/**", "/auth/**", "/").permitAll()
                .anyRequest().permitAll()

        return http.build()
    }

    public fun webSecurityCustomizer():WebSecurityCustomizer
    {
        return WebSecurityCustomizer { web: WebSecurity ->
            web.ignoring().antMatchers("/css/**", "/image/**")
        }
    }
}