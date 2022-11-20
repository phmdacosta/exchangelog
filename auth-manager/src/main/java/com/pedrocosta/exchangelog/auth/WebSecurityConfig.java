package com.pedrocosta.exchangelog.auth;

import com.pedrocosta.exchangelog.auth.token.access.JwtHandler;
import com.pedrocosta.exchangelog.auth.token.access.filter.AccessTokenEntryPoint;
import com.pedrocosta.exchangelog.auth.token.access.filter.AccessTokenFilter;
import com.pedrocosta.exchangelog.auth.user.service.UserService;
import com.pedrocosta.exchangelog.auth.utils.Route;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${token.access.enabled}")
    private boolean accessTokenEnabled;
    @Value("${route.auth.login}")
    private String routeLogin;
    private final ApplicationContext context;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AccessTokenEntryPoint accessTokenEntryPoint;

    public WebSecurityConfig(ApplicationContext context, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, AccessTokenEntryPoint accessTokenEntryPoint) {
        this.context = context;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accessTokenEntryPoint = accessTokenEntryPoint;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessTokenFilter accessTokenFilter(JwtHandler jwtHandler) {
        return new AccessTokenFilter(jwtHandler, userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().formLogin(formLoginCustomizer());
        configureFeatureAccess(http);
        if (accessTokenEnabled) {
            configureAccessToken(http);
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    protected Customizer<FormLoginConfigurer<HttpSecurity>> formLoginCustomizer() {
        return httpSecurityFormLoginConfigurer -> {};
    }

    protected void configureFeatureAccess(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(getPermitAllRoutes()).permitAll()
                .anyRequest()
                .access("@roleAuthorityValidator.accept(authentication,request)");
    }

    protected void configureAccessToken(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(accessTokenEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        JwtHandler jwtHandler = context.getBean(JwtHandler.class);
        http.addFilterBefore(accessTokenFilter(jwtHandler), UsernamePasswordAuthenticationFilter.class);
    }

    protected String[] getPermitAllRoutes() {
        Set<String> permitAllRoutes = new HashSet<>();
        permitAllRoutes.add(Route.API + Route.REGISTRATION + "/**");
        if (accessTokenEnabled) {
            permitAllRoutes.add(routeLogin + "/**");
        }
        return permitAllRoutes.toArray(new String[0]);
    }
}
