package atmin.service;

import atmin.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    boolean delete(Long id);
}