package devnoh.demoapp.repository;

import devnoh.demoapp.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@Slf4j
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    private Product product1;

    private Product product2;

    @Before
    public void setUp() throws Exception {
        product1 = new Product();
        product1.setCode("P001");
        product1.setName("iPhone 7");
        product1.setDescription("This is an Apple phone.");
        product1.setActive(true);

        entityManager.persist(product1);

        product2 = new Product();
        product2.setCode("P002");
        product2.setName("Pixel 2");
        product2.setDescription("This is a Google phone.");
        product2.setActive(false);

        entityManager.persist(product2);
    }

    @Test
    public void findByCode() {
        Product product = productRepository.findByCode("P001");
        assertNotNull(product);
        assertEquals(product1, product);
    }

    @Test
    public void findByCode_NotFound() {
        Product product = productRepository.findByCode("P003");
        assertNull(product);
    }

    @Test
    public void findByName() {
        List<Product> products = productRepository.findByName("Pixel 2");
        assertEquals(1, products.size());
        assertEquals(product2, products.get(0));
    }

    @Test
    public void findByActiveIsTrue() {
        Page<Product> products =
                productRepository.findByActiveIsTrue(new PageRequest(0, 5, Sort.Direction.ASC, "name"));
        log.debug("products={}", products);
        assertEquals(1, products.getTotalPages());
        assertEquals(1, products.getTotalElements());
        assertEquals(1, products.getContent().size());
        assertEquals(product1, products.getContent().get(0));
    }

    @Test
    public void testCRUD() {
        Product product = new Product();
        product.setCode("P003");
        product.setName("iPhone 8");
        product.setDescription("This is an Apple phone.");
        product.setActive(true);
        productRepository.save(product);

        Product found = productRepository.getOne(product.getId());
        assertNotNull(found);
        assertEquals(product, found);

        product.setName("iPhone X");
        productRepository.save(product);

        found = productRepository.getOne(product.getId());
        assertEquals("iPhone X", found.getName());

        productRepository.delete(product.getId());

        found = productRepository.findOne(product.getId());
        assertNull(found);

        try {
            found = productRepository.getOne(product.getId());
            fail();
        } catch (Exception e) {
            // pass
        }
    }
}
