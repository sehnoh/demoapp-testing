package devnoh.demoapp.service;

import devnoh.demoapp.domain.Product;
import devnoh.demoapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findProductsByName(String name) {
        return productRepository.findByName(name);
    }

}
