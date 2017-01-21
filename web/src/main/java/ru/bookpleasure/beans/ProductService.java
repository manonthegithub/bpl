package ru.bookpleasure.beans;

import net.iharder.Base64;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.bookpleasure.db.entities.Product;
import ru.bookpleasure.db.entities.Product_;
import ru.bookpleasure.db.entities.ResourceFile;
import ru.bookpleasure.models.ProductView;
import ru.bookpleasure.repos.ProductsRepo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Винда on 22.02.2016.
 */
@Component
@Lazy
public class ProductService {


    @Autowired
    Mapper mapper;

    @Autowired
    ProductsRepo productsRepo;

    @Autowired
    FilesService filesService;


    public ProductView getProductById(UUID id){
        Product r = productsRepo.findOne(id);
        return convertToView(r);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void removeProduct(UUID id) {
        Product product = productsRepo.findOne(id);
        List<ResourceFile> resourceFiles = filesService.findByFilenamePrefix(product.getImageFilename());
        filesService.delete(resourceFiles);
        productsRepo.delete(product);
    }

    public List<ProductView> getProductByCategory(String category, Sort sort) {
        List<Product> products = productsRepo.findByCategory(Product.ProductCategory.valueOf(category), sort);
        return convertListToView(products);
    }

    public List<ProductView> getEnabledProductByCategory(String category, Optional<Sort> sortOptional) {
        Sort sort = sortOptional.orElse(new Sort(Sort.Direction.DESC, Product_.createdAt.getName()));
        List<Product> products = productsRepo.findByCategoryAndEnabledTrue(Product.ProductCategory.valueOf(category), sort);
        return convertListToView(products);
    }

    public List<ProductView> getAllProducts() {
        Collection<Product> products = productsRepo.findAll(new Sort(Sort.Direction.DESC, Product_.createdAt.getName()));
        return convertListToView(products);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public ProductView saveProduct(ProductView productView) {
        if (productView.getEnabled() == null) {
            productView.setEnabled(false);
        }
        if (productView.getId() == null) {
            productView.setId(UUID.randomUUID());
        }

        if (productView.getImageLink() != null && productView.getBase64ImageFile() != null) {
            String fileName = UUID.randomUUID().toString() +
                    productView.getImageLink().substring(productView.getImageLink().lastIndexOf("."));
            productView.setImageLink(fileName);

            try {
                byte[] fileBytes = Base64.decode(productView.getBase64ImageFile());
                filesService.save(fileName, fileBytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Product product = convertToProduct(productView);
        return convertToView(productsRepo.saveAndFlush(product));
    }

    private List<ProductView> convertListToView(Collection<Product> found) {
        return found
                .stream()
                .map(this::convertToView)
                .collect(Collectors.toList());
    }

    private List<Product> convertListToProduct(Collection<ProductView> found) {
        return found
                .stream()
                .map(this::convertToProduct)
                .collect(Collectors.toList());
    }

    private ProductView convertToView(Product found) {
        return mapper.map(found, ProductView.class);
    }

    private Product convertToProduct(ProductView productView) {
        return mapper.map(productView, Product.class);
    }
}
