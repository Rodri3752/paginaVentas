
package Rodri.demo.Demo.repositorios;

import Rodri.demo.Demo.entidades.Foto;
import Rodri.demo.Demo.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FotoRepositorio extends JpaRepository<Foto, String> {
    
}
