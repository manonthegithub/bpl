package ru.bookpleasure.beans;


import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ru.bookpleasure.db.entities.Product;
import ru.bookpleasure.db.entities.Product_;
import ru.bookpleasure.db.entities.ResourceFile;
import ru.bookpleasure.db.entities.ResourceFile_;
import ru.bookpleasure.models.ProductView;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Created by Винда on 22.02.2016.
 */
@Component
@Lazy
@Scope(
        value = WebApplicationContext.SCOPE_REQUEST,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProductBean{

    @PersistenceContext
    EntityManager em;


    public ProductView getProductById(UUID id){
        Product r = em.find(Product.class, id);
        return convertToView(r);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void removeProduct(UUID id) {

        Product product = em.find(Product.class, id);

        //todo вынести в бин работы с файлами
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ResourceFile> query = cb.createQuery(ResourceFile.class);
        Root<ResourceFile> fileRoot = query.from(ResourceFile.class);
        query.where(
                cb.like(
                        fileRoot.get(ResourceFile_.name),
                        "%" + product.imageFilename
                )
        );

        TypedQuery<ResourceFile> preparedQuery = em.createQuery(query);
        ResourceFile r = preparedQuery.getSingleResult();
        em.remove(product);
        em.remove(r);
    }

    public List<ProductView> getProductByCategory(String category){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> productRoot = query.from(Product.class);
        query.where(
            cb.equal(
                productRoot.get(Product_.category),
                Product.ProductCategory.valueOf(category)
            )
        );
        TypedQuery<Product> preparedQuery = em.createQuery(query);
        List<ProductView> result =  convertListToView(preparedQuery.getResultList());
        return result;
    }

    public List<ProductView> getEnabledProductByCategory(String category){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> productRoot = query.from(Product.class);
        query.where(
            cb.equal(
                productRoot.get(Product_.category),
                Product.ProductCategory.valueOf(category)
            ),
            cb.equal(productRoot.get(Product_.enabled), true)
        );
        TypedQuery<Product> preparedQuery = em.createQuery(query);
        List<ProductView> result =  convertListToView(preparedQuery.getResultList());
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ProductView saveProduct(ProductView productView) {
        Product product = convertToProduct(productView);
        return convertToView(em.merge(product));
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
