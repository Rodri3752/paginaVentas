package Rodri.demo.Demo.servicios;

import Rodri.demo.Demo.entidades.Compra;
import Rodri.demo.Demo.entidades.Producto;
import Rodri.demo.Demo.entidades.Usuario;
import Rodri.demo.Demo.errores.ErrorServicio;
import Rodri.demo.Demo.repositorios.CompraRepositorio;
import Rodri.demo.Demo.repositorios.ProductoRepositorio;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompraServicios {

    private int cantidadcompra;
    
    //@Autowired
    private Usuario usuario;
    
    @Autowired
    private NotificacionServicios notificacionServicios;

    @Autowired
    private CompraRepositorio compraRepositorio;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    public void compra(String idUsuario, String idProducto) throws ErrorServicio {
        Compra compra = new Compra();
        compra.setFecha(new Date());

        Optional<Producto> respuesta = productoRepositorio.findById(idProducto);
        if (respuesta.isPresent()) {
            Producto producto = respuesta.get();
            if (producto.getStock() < 0) {
                Scanner leer = new Scanner(System.in);
                cantidadcompra = leer.nextInt();
                producto.setStock(producto.getStock() - cantidadcompra);
                notificacionServicios.enviar( "Producto vendido ", "pagina de ventas", usuario.getMail());
                //"Producto vendido " producto.getId() " cantidad: " producto.getStock()
            } else {
                throw new ErrorServicio("No hay stock disponible para la compra");
            }
        } else {
            throw new ErrorServicio("No existe un producto con ese ID");
        }
        compraRepositorio.save(compra);
    }

    /**
     * @return the cantidadcompra
     */
    public int getCantidadcompra() {
        return cantidadcompra;
    }

    /**
     * @param cantidadcompra the cantidadcompra to set
     */
    public void setCantidadcompra(int cantidadcompra) {
        this.cantidadcompra = cantidadcompra;
    }
}
