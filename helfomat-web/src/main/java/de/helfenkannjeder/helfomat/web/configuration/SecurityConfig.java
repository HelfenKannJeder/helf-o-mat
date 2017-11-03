package de.helfenkannjeder.helfomat.web.configuration;

import de.helfenkannjeder.helfomat.api.HelfomatConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Valentin Zickner
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
                .antMatchers("/admin/**").authenticated()
                .and()
            .httpBasic()
                .and()
            .csrf().disable();
        // @formatter:on
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, HelfomatConfiguration helfomatConfiguration) throws Exception {
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> authenticationManagerBuilder = auth.inMemoryAuthentication();

        for (HelfomatConfiguration.User user : helfomatConfiguration.getAdminUsers()) {
            authenticationManagerBuilder
                .withUser(user.getUsername())
                .password(user.getPassword())
                .roles("USER", "ADMIN");
            if (user.isPrintPassword()) {
                LOGGER.warn("Please authenticate with the following credentials against the admin " +
                    "endpoints: [username='" + user.getUsername() + "', password='" + user.getPassword() + "'");
            }
        }

    }

}
