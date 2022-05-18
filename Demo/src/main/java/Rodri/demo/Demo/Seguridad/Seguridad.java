package Rodri.demo.Demo.Seguridad;

import Rodri.demo.Demo.servicios.UsuarioServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Seguridad {

    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public class SeguridadConfiguracion extends WebSecurityConfigurerAdapter {

        @Autowired
        public UsuarioServicios usuarioServicio;

        @Autowired
        public void globalConfigure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(usuarioServicio)
                    .passwordEncoder(new BCryptPasswordEncoder());
        }

        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/admin/*").hasRole("ADMIN") // Puedo dar acceso a un controlador completo, con rol especifoc
                    .antMatchers("/css/*", "/js/*", "/img/*",
                            "/**").permitAll()
                    .and().
                    formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/logincheck")
                    .usernameParameter("email")
                    .passwordParameter("clave")
                    .defaultSuccessUrl("/")
                    .permitAll()
                    .and().logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll().
                    and().csrf().disable();
        }
    }

}
