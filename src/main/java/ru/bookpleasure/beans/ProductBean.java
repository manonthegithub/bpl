package ru.bookpleasure.beans;


import ru.bookpleasure.db.PersistenceManager;
import ru.bookpleasure.db.entities.Product;
import ru.bookpleasure.db.entities.Product_;
import ru.bookpleasure.models.ProductView;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Винда on 22.02.2016.
 */
public class ProductBean{



    public ProductView getProductById(UUID id){
        EntityManager em = PersistenceManager.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> productRoot = query.from(Product.class);
        query.where(
            cb.equal(
                productRoot.get(Product_.id),
                id
            )
        );

        TypedQuery<Product> preparedQuery = em.createQuery(query);
        Product r = preparedQuery.getSingleResult();
        em.close();
        return convertToView(r);
    }

    public List<ProductView> getProductByCategory(String category){
        EntityManager em = PersistenceManager.getEntityManager();
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
        em.close();
        return result;
    }

    public List<ProductView> getEnabledProductByCategory(String category){
        EntityManager em = PersistenceManager.getEntityManager();
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
        em.close();
        return result;
    }

    public void saveProduct(ProductView productView){
        Product product = convertToProduct(productView);
        PersistenceManager.saveEntity(product);
    }

    private List<ProductView> convertListToView(List<Product> found ){
        return found.
            stream().
            map(new Function<Product, ProductView>() {
                @Override
                public ProductView apply(Product o) {
                    return convertToView(o);
                }
            }).
            collect(Collectors.<ProductView>toList());
    }

    private List<Product> convertListToProduct(List<ProductView> found ){
        return found.
            stream().
            map(new Function<ProductView, Product>() {
                @Override
                public Product apply(ProductView o) {
                    return convertToProduct(o);
                }
            }).
            collect(Collectors.<Product>toList());
    }

    private ProductView convertToView(Product found){
        ProductView view = new ProductView();
        view.setId(found.id);
        view.setName(found.name);
        view.setAvailableNumber(found.availableNumber);
        view.setCategory(found.category.toString());
        view.setQuantity(found.quantity);
        view.setDescription(found.description);
        view.setImageLink(found.imageFilename);
        view.setPrice(found.price);
        return view;
    }

    private Product convertToProduct(ProductView productView){
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
