package com.sakazoo.admin.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import javax.sql.DataSource

@EnableWebSecurity
class SecurityConfig(
    private val dataSource: DataSource,
    @Qualifier("UseDetailServiceImpl") private val userDetailsService: UserDetailsService,
    @Qualifier("SuccessHandler") private  val successHandler: AuthenticationSuccessHandler,
    private val passwordEncoder: PasswordEncoder
) : WebSecurityConfigurerAdapter() {
    companion object {
        private const val LOGIN_URL = "/login"
        private const val LOGIN_FAILURE_URL = "$LOGIN_URL?error"
        private const val LOGIN_SUCCESS_URL = "/home"
        private const val LOGOUT_URL = "/logout"

        // jdbcAuthentication use these SQL
        private const val USER_SQL = """
            SELECT
              user_id,
              password,
              enabled
            FROM
              m_user
            WHERE
              user_id = ?
        """
        private const val ROLE_SQL = """
            SELECT
              m_user.user_id,
              role.role_name
            FROM
              m_user
            INNER JOIN
              t_user_role AS user_role
            ON
              m_user.user_id = user_role.user_id
            INNER JOIN
              m_role AS role
            ON
              user_role.role_id = role.role_id
            WHERE
              m_user.user_id = ?
        """
    }



    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(
            "/images/**",
            "/css/**",
            "/javascript/**"
        )
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers(LOGIN_URL).permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginProcessingUrl(LOGIN_URL).loginPage(LOGIN_URL)
            .failureUrl(LOGIN_FAILURE_URL)
            .usernameParameter("userId")  // mapping with login html
            .passwordParameter("password")  // mapping with login html
            .defaultSuccessUrl(LOGIN_SUCCESS_URL, true)
            .successHandler(successHandler)
            .and()
            .logout()
            .logoutRequestMatcher(AntPathRequestMatcher(LOGOUT_URL))
            .logoutUrl(LOGOUT_URL)
            .logoutSuccessUrl(LOGIN_URL)
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
//        auth.jdbcAuthentication()
//            .dataSource(dataSource)
//            .usersByUsernameQuery(USER_SQL)
//            .authoritiesByUsernameQuery(ROLE_SQL)
//            .passwordEncoder(passwordEncoder())
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
    }
}