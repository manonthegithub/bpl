package ru.bookpleasure.beans;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.bookpleasure.db.entities.Product;
import ru.bookpleasure.db.entities.ResourceFile;
import ru.bookpleasure.models.ProductView;
import ru.bookpleasure.repos.FilesRepo;
import ru.bookpleasure.repos.ProductsRepo;

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
    FilesRepo filesRepo;


    public ProductView getProductById(UUID id){
        Product r = productsRepo.findOne(id);
        return convertToView(r);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void removeProduct(UUID id) {

        Product product = productsRepo.findOne(id);
        List<ResourceFile> resourceFile = filesRepo.findByNameEndingWith(product.imageFilename);

        filesRepo.delete(resourceFile);
        productsRepo.delete(product);

    }

    public List<ProductView> getProductByCategory(String category){
        List<Product> products = productsRepo.findByCategory(Product.ProductCategory.valueOf(category));
        return convertListToView(products);
    }

    public List<ProductView> getEnabledProductByCategory(String category){
        List<Product> products = productsRepo.findByCategoryAndEnabledTrue(Product.ProductCategory.valueOf(category));
        return convertListToView(products);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ProductView saveProduct(ProductView productView) {
        Product product = convertToProduct(productView);
        return convertToView(productsRepo.save(product));
    }

    private static List<ProductView> convertListToView(List<Product> found) {
        List<ProductView> pv = new ArrayList<ProductView>();
        for (Product p : found) {
            pv.add(convertToView(p));
        }
        return pv;
    }

    private static List<Product> convertListToProduct(List<ProductView> found) {
        List<Product> p = new ArrayList<Product>();
        for (ProductView pv : found) {
            p.add(convertToProduct(pv));
        }
        return p;
    }

    private static ProductView convertToView(Product found) {
        ProductView view = new ProductView();
        view.setId(found.id);
        view.setEnabled(found.enabled);
        view.setName(found.name);
        view.setAvailableNumber(found.availableNumber);
        view.setCategory(found.category.toString());
        view.setQuantity(found.quantity);
        view.setDescription(found.description);
        view.setImageLink(found.imageFilename);
        view.setPrice(found.price);
        return view;
    }

    private static Product convertToProduct(ProductView productView) {
        Product product = new Product();
        product.id = productView.getId();
        product.name = productView.getName();
        product.quantity = productView.getQuantity();
        product.availableNumber = productView.getAvailableNumber();
        product.category = Product.ProductCategory.valueOf(productView.getCategory());
        product.description = productView.getDescription();
        product.imageFilename = productView.getImageLink();
        product.enabled = productView.getEnabled();
        product.price = productView.getPrice();
        return product;
    }
}
