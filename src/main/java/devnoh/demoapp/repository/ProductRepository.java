package devnoh.demoapp.repository;

import devnoh.demoapp.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByCode(String code);

    List<Product> findByName(String name);

    Page<Product> findByActiveIsTrue(Pageable pageable);

}
