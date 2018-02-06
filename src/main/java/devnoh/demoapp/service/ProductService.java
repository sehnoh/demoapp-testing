package devnoh.demoapp.service;

import devnoh.demoapp.domain.Product;
import devnoh.demoapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product findProductById(Long id) {
        return productRepository.findOne(id);
    }

    public Product findProductByCode(String code) {
        return productRepository.findByCode(code);
    }

    public List<Product> findProductsByName(String name) {
        return productRepository.findByName(name);
    }

    public Page<Product> findActiveProducts(Pageable pageable) {
        return productRepository.findByActiveIsTrue(pageable);
    }

}
