package devnoh.demoapp.rest;

import devnoh.demoapp.domain.Product;
import devnoh.demoapp.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product saveProduct(@Valid @RequestBody Product product) {
        log.debug("product={}", product);
        return productService.saveProduct(product);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product findProductById(@PathVariable Long id) {
        log.debug("id={}", id);
        return productService.findProductById(id);
    }

    @GetMapping(value = "/code/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product findProductByCode(@PathVariable String code) {
        log.debug("code={}", code);
        return productService.findProductByCode(code);
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> findProductsByName(@RequestParam(defaultValue = "") String name) {
        log.debug("name={}", name);
        return productService.findProductsByName(name);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Product> findActiveProducts(
            @PageableDefault(sort = {"name"}, direction = Sort.Direction.ASC, size = 5) Pageable pageable) {
        log.debug("pagable={}", pageable);
        return productService.findActiveProducts(pageable);
    }

}
