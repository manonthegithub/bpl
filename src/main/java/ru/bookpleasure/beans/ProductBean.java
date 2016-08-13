package ru.bookpleasure.beans;

import net.iharder.Base64;
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

/**
 * Created by Винда on 22.02.2016.
 */
@Component
@Lazy
public class ProductBean{

    @Autowired
    ProductsRepo productsRepo;

    @Autowired
    FilesBean filesBean;


    public ProductView getProductById(UUID id){
        Product r = productsRepo.findOne(id);
        return convertToView(r);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void removeProduct(UUID id) {
        Product product = productsRepo.findOne(id);
        List<ResourceFile> resourceFiles = filesBean.findByFilenamePrefix(product.getImageFilename());
        filesBean.delete(resourceFiles);
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
        Iterable<Product> products = productsRepo.findAll(new Sort(Sort.Direction.DESC, Product_.createdAt.getName()));
        return convertListToView(products);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public ProductView saveProduct(ProductView productView) {
        if (productView.getAvailableNumber() == null) {
            productView.setAvailableNumber(productView.getQuantity());
        }
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
                filesBean.save(fileName, fileBytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Product product = convertToProduct(productView);
        return convertToView(productsRepo.saveAndFlush(product));
    }

    private static List<ProductView> convertListToView(Iterable<Product> found) {
        List<ProductView> pv = new ArrayList<ProductView>();
        for (Product p : found) {
            pv.add(convertToView(p));
        }
        return pv;
    }

    private static List<Product> convertListToProduct(Iterable<ProductView> found) {
        List<Product> p = new ArrayList<Product>();
        for (ProductView pv : found) {
            p.add(convertToProduct(pv));
        }
        return p;
    }

    private static ProductView convertToView(Product found) {
        ProductView view = new ProductView();
        view.setId(found.getId());
        view.setEnabled(found.isEnabled());
        view.setName(found.getName());
        view.setActive(found.getActive());
        view.setAvailableNumber(found.getQuantity());
        view.setCategory(found.getCategory().toString());
        view.setQuantity(found.getQuantity());
        view.setDescription(found.getDescription());
        view.setImageLink(found.getImageFilename());
        view.setPrice(found.getPrice().intValue());
        return view;
    }

    private static Product convertToProduct(ProductView productView) {
        Product product = new Product();
        product.setId(productView.getId());
        product.setName(productView.getName());
        product.setActive(productView.getActive());
        product.setQuantity(productView.getQuantity());
        product.setQuantity(productView.getAvailableNumber());
        product.setCategory(Product.ProductCategory.valueOf(productView.getCategory()));
        product.setDescription(productView.getDescription());
        product.setImageFilename(productView.getImageLink());
        product.setEnabled(productView.getEnabled());
        product.setPrice(new BigDecimal(productView.getPrice()));
        return product;
    }
}
