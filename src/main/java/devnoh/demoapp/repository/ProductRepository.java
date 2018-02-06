package devnoh.demoapp.repository;

import devnoh.demoapp.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByCode(String code);

    List<Product> findByName(String name);

    List<Product> findByActiveIsTrue();

}
