package devnoh.demoapp.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import devnoh.demoapp.domain.Product;
import devnoh.demoapp.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerMockitoTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    private Product product;

    @Before
    public void setUp() throws Exception {
        // MockitoAnnotations.initMocks(this); // Not needed with @RunWith(MockitoJUnitRunner.class)
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        product = new Product();
        product.setId(1L);
        product.setCode("P001");
        product.setName("Product 1");
        product.setDescription("This is a cool product.");
        product.setActive(true);
    }

    @Test
    public void saveProduct() throws Exception {
        when(productService.saveProduct(any(Product.class))).thenReturn(product);

        String requestBody = new ObjectMapper().writeValueAsString(product);

        mockMvc.perform(post("/products").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("P001")))
                .andExpect(jsonPath("$.name", is("Product 1")))
                .andExpect(jsonPath("$.description", is("This is a cool product.")))
                .andExpect(jsonPath("$.active", is(true)));

        verify(productService, times(1)).saveProduct(any(Product.class));
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void findProductById() throws Exception {
        when(productService.findProductById(anyLong())).thenReturn(product);

        mockMvc.perform(get("/products/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("P001")))
                .andExpect(jsonPath("$.name", is("Product 1")))
                .andExpect(jsonPath("$.description", is("This is a cool product.")))
                .andExpect(jsonPath("$.active", is(true)));

        verify(productService, times(1)).findProductById(anyLong());
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void findProductByCode() {
    }

    @Test
    public void findProductsByName() {
    }

    @Test
    public void findActiveProducts() {
    }
}
