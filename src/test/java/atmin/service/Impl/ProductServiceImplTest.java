package atmin.service.Impl;

import atmin.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(); // Tự khởi động lại mảng dữ liệu sạch trước mỗi test case
    }

    // STT 1: getAll – có dữ liệu -> Trả về list không rỗng
    @Test
    void getAll_WhenDataExists_ShouldReturnNonEmptyList() {
        List<Product> list = productService.findAll();
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    // STT 2: getById – tìm thấy -> Trả về đúng Product
    @Test
    void getById_WhenFound_ShouldReturnCorrectProduct() {
        Product product = productService.findById(1L);
        assertNotNull(product);
        assertEquals("Keyboard", product.getName());
    }

    // STT 3: getById – không tìm thấy -> Ném RuntimeException
    @Test
    void getById_WhenNotFound_ShouldThrowRuntimeException() {
        assertThrows(RuntimeException.class, () -> productService.findById(99L));
    }

    // STT 4: addProduct – thành công -> ID được gán tự động
    @Test
    void addProduct_Success_ShouldGenerateIdAutomatically() {
        Product newProduct = new Product(null, "Mouse", 50.0, 5);
        Product saved = productService.save(newProduct);
        assertNotNull(saved.getId());
        assertEquals(2L, saved.getId());
    }

    // STT 5: deleteProduct – thành công -> Size giảm 1
    @Test
    void deleteProduct_Success_ShouldDecreaseSize() {
        int initialSize = productService.findAll().size();
        boolean result = productService.delete(1L);
        assertTrue(result);
        assertEquals(initialSize - 1, productService.findAll().size());
    }
}