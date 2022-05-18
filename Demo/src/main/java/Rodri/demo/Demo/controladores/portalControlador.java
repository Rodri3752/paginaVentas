/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rodri.demo.Demo.controladores;

import Rodri.demo.Demo.errores.ErrorServicio;
import Rodri.demo.Demo.servicios.UsuarioServicios;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author arter
 */
@Controller
@RequestMapping("/")
public class portalControlador {

    @Autowired
    private UsuarioServicios usuarioServicios;

    @GetMapping("/paginaVentas")
    public String index() {
        return "index.html";
    }

    @GetMapping("/crearUsuario")
    public String crearUsuario() {
        return "crearUsuario.html";
    }

    @GetMapping("/iniciarSecion")
    public String iniciarSecion() {
        return "iniciarSecion.html";
    }

    @GetMapping("/catalogo")
    public String catalogo() {
        return "catalogo.html";
    }

    @PostMapping("/registrar")
    public String registrar(ModelMap modelo, @RequestParam String nombre, @RequestParam String clave, @RequestParam String clave1, @RequestParam String mail, @RequestParam String nombreApellido) {
        try {
            usuarioServicios.registrar(nombre, nombreApellido, mail, clave, clave1);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("nombreApellido", nombreApellido);
            modelo.put("mail", mail);
            return "crearUsuario.html";
        }
        modelo.put("titulo", "Bienvenido a nombrePaginaVentas ");
        modelo.put("descripcion", "El usuario fue registrado con exito");
        return "Exito.html";
    }
}
