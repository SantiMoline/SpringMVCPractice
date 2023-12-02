package com.egg.biblioteca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.egg.biblioteca.servicios.UsuarioServicio;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SeguridadWeb {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioServicio) //una vez que se registra un usario, se valida con nuestro método existente en UsuarioService.
            .passwordEncoder(new BCryptPasswordEncoder()); //Luego de validado el usuario, se va a hashear (encriptar) la contraseña.
    }

    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorizeHttpRequests) -> 
                authorizeHttpRequests
                    .requestMatchers("/admin/*")
                    .hasAnyRole("ADMIN")
                    .requestMatchers("/css/*", "/js/*", "/img/*", "/**")
                    .permitAll())
            .formLogin(formLogin -> 
                formLogin
                    .loginPage("/login")
                    .loginProcessingUrl("/logincheck")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/inicio")
                    .permitAll())
            .logout(logOut -> 
                logOut
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .permitAll())
            .csrf(csrf -> csrf.disable());
            
    return http.build();
    }

}
