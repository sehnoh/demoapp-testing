package devnoh.demoapp.repository;

import devnoh.demoapp.domain.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void setUp() throws Exception {
        Product product1 = new Product();
        product1.setCode("P001");
        product1.setName("iPhone 7");
        product1.setDescription("This is an Apple phone.");
        product1.setActive(true);

        entityManager.persist(product1);

        Product product2 = new Product();
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
        assertEquals("P001", product.getCode());
        assertEquals("iPhone 7", product.getName());
        assertEquals("This is an Apple phone.", product.getDescription());
        assertEquals(true, product.isActive());
    }

    @Test
    public void findByCode_NotFound() {
        Product product = productRepository.findByCode("P003");
        assertNull(product);
    }

    @Test
    public void findByName() {
    }

    @Test
    public void findByActiveIsTrue() {
    }

    @Test
    public void testCRUD() {
        Product product = new Product();
        product.setCode("P001");
        product.setName("iPhone 7");
        product.setDescription("This is an Apple phone.");
        product.setActive(true);

        productRepository.save(product);

        Product found = productRepository.getOne(product.getId());
        assertNotNull(found);
        assertEquals(product, found);
    }
}
