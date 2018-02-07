package devnoh.demoapp.service;

import devnoh.demoapp.domain.Product;
import devnoh.demoapp.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceMockitoTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Product product;

    @Before
    public void setUp() throws Exception {
        // MockitoAnnotations.initMocks(this); // Not needed with @RunWith(MockitoJUnitRunner.class)

        product = new Product();
        product.setId(1L);
        product.setCode("P001");
        product.setName("Product 1");
        product.setDescription("Test is a cool product.");
        product.setActive(true);
    }

    @Test
    public void saveProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product saved = productService.saveProduct(new Product());
        assertThat(saved).isEqualTo(product);

        verify(productRepository, times(1)).save(any(Product.class));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void findProductById() {
        when(productRepository.findOne(anyLong())).thenReturn(product);

        Product found = productService.findProductById(1L);
        assertThat(found).isEqualTo(product);

        verify(productRepository, times(1)).findOne(anyLong());
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void findProductByCode() {
        when(productRepository.findByCode(anyString())).thenReturn(product);

        Product found = productService.findProductByCode("P001");
        assertThat(found).isEqualTo(product);

        verify(productRepository, times(1)).findByCode(anyString());
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void findProductsByName() {
        when(productRepository.findByName(anyString())).thenReturn(Arrays.asList(product));

        List<Product> found = productService.findProductsByName("Product 1");
        assertThat(found).hasSize(1);
        assertThat(found).contains(product);

        verify(productRepository, times(1)).findByName(anyString());
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void findActiveProducts() {
        Page<Product> page = new PageImpl<Product>(Arrays.asList(product));

        when(productRepository.findByActiveIsTrue(any(Pageable.class))).thenReturn(page);

        Page<Product> found = productService.findActiveProducts(new PageRequest(1, 5, Sort.Direction.ASC, "name"));
        assertThat(found.getTotalElements()).isEqualTo(1);
        assertThat(found.getContent()).contains(product);

        verify(productRepository, times(1)).findByActiveIsTrue(any(Pageable.class));
        verifyNoMoreInteractions(productRepository);
    }
}
