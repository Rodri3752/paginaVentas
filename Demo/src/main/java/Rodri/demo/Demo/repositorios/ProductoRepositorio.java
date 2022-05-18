
package Rodri.demo.Demo.repositorios;

import Rodri.demo.Demo.entidades.Producto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, String>{
    @Query("SELECT c FROM Producto c WHERE c.id = :id")
    public List<Producto> buscarProductoPorId(@Param("id") String id);

   //public Optional<Producto> findById(int id);
}
