package Rodri.demo.Demo.servicios;

import Rodri.demo.Demo.entidades.Foto;
import Rodri.demo.Demo.entidades.Producto;
import Rodri.demo.Demo.errores.ErrorServicio;
import Rodri.demo.Demo.repositorios.ProductoRepositorio;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ProductoServicios {

    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private FotoServicio fotoServicio;
    @Transactional
    public void agregarProducto(MultipartFile archivo, String id, String nombre, String caracteristicas, int stock) throws ErrorServicio {
        validar(id, nombre, caracteristicas, stock);
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setCaracteristicas(caracteristicas);
        producto.setStock(stock);
        
        Foto foto = fotoServicio.guardar(archivo);
        producto.setFoto(foto);

        productoRepositorio.save(producto);
    }

    private void validar(String id, String nombre, String caracteristicas, int stock) throws ErrorServicio {
        if(id == null|| id.isEmpty()){
            throw new ErrorServicio("El ID no puede ser nulo");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del usuario no puede ser nulo");
        }
        if (caracteristicas == null) {
            throw new ErrorServicio("El campo caracteristicas debe estar ingresado");
        }
        if (stock > 0) {
            throw new ErrorServicio("El Stock no puede contener negativo");
        }
    }
    @Transactional
    public void modificar(MultipartFile archivo, String id, String nombre, String caracteristicas, int stock) throws ErrorServicio {
        validar (id, nombre, caracteristicas, stock);
        Optional <Producto> respuesta = productoRepositorio.findById(id);
        if (respuesta.isPresent()){
            Producto producto = new Producto();
            producto.setId(id);
            producto.setNombre(nombre);
            producto.setCaracteristicas(caracteristicas);
            producto.setStock(stock);
            
            String idFoto = null;
            if(producto.getFoto() != null){
                idFoto = producto.getFoto().getId();
            }
            
            Foto foto = fotoServicio.actualizar(idFoto, archivo);
            producto.setFoto(foto);
            
            productoRepositorio.save(producto);
        } else {
            throw new ErrorServicio("No se encontro el producto solicitado.");
        }
    }
    @Transactional
    public void eliminarProducto(String id)throws ErrorServicio{
        Optional <Producto> respuesta = productoRepositorio.findById(id);
        if(respuesta.isPresent()){
            Producto producto = respuesta.get();
            producto.setBaja(new Date());
            productoRepositorio.save(producto);
        }else{
            throw new ErrorServicio("No se encontro el producto solicitado.");
        }
    }
}
