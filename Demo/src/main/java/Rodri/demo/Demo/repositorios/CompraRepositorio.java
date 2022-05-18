
package Rodri.demo.Demo.repositorios;

import Rodri.demo.Demo.entidades.Compra;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepositorio extends JpaRepository<Compra, String>{
    
  @Query("SELECT c FROM Compra c JOIN Producto p WHERE p.id = :id ORDER BY c.fechacompra")
    public List<Compra> buscarCompra(@Param("id")String id);
}
