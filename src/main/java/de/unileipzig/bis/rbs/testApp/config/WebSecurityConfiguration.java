package de.unileipzig.bis.rbs.testApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

/**
 * Simple security configuration for the login app
 *
 * @author Stephan Kemper
 * @author Lukas Werner
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * The DataSource
     */
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/css/**", "/fonts/**", "/js/**", "/register").permitAll()
                .antMatchers("/manage/role").access("hasAuthority('admin')")
                .antMatchers("/manage/user").access("hasAuthority('admin')")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/logout")
                .permitAll()
                .and()
            .exceptionHandling()
                .accessDeniedPage("/error/403");

        http.csrf().disable();
    }

    /**
     * Build the authentication
     *
     * @param auth the builder
     * @throws Exception if something goes wrong
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(getUserQuery())
                .authoritiesByUsernameQuery(getAuthorityQuery());
    }

    /**
     * The query for getting users by username
     *
     * @return the query
     */
    private String getUserQuery() {
        return "SELECT username, password, true " +
                "FROM rbs_users " +
                "WHERE username = ?";
    }

    /**
     * The query for getting authorities by username
     *
     * @return the query
     */
    private String getAuthorityQuery() {
        return "SELECT U.username, R.name AS authority " +
                "FROM rbs_users U " +
                "INNER JOIN rbs_users_roles UR " +
                "ON U.id = UR.user_id " +
                "INNER JOIN rbs_roles R " +
                "ON UR.role_id = R.id " +
                "WHERE U.username = ?";
    }

}