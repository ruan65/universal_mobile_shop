package org.premiumapp.universalmobileshop.biz_logic.http;

import org.premiumapp.universalmobileshop.biz_logic.model.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function4;
import io.reactivex.functions.Predicate;

/**
 * Created by a on 01.06.17.
 */

public class ProductBackendApiDecorator {

    private final ProductBackendApi api;

    public ProductBackendApiDecorator(ProductBackendApi api) {
        this.api = api;
    }

    public Observable<List<Product>> getProducts(int pagination) {
        return api.getProducts(pagination);
    }

    /**
     * Get all Products
     */

    public Observable<List<Product>> getAllProducts() {
        return Observable.zip(getProducts(0), getProducts(1), getProducts(2), getProducts(3), new Function4<List<Product>, List<Product>, List<Product>, List<Product>, List<Product>>() {
            @Override
            public List<Product> apply(List<Product> p0, List<Product> p1, List<Product> p2, List<Product> p3) throws Exception {
                List<Product> productList = new ArrayList<>();

                productList.addAll(p0);
                productList.addAll(p1);
                productList.addAll(p2);
                productList.addAll(p3);

                return productList;
            }
        });
    }

    public Observable<List<Product>> getAllProductsOfCategory(final String categoryName) {

        Observable<Product> objectObservable = getAllProducts().flatMap(new Function<List<Product>, ObservableSource<? extends Product>>() {
            @Override
            public ObservableSource<Product> apply(List<Product> source) throws Exception {
                return Observable.fromIterable(source);
            }
        });

        return objectObservable
                .filter(new Predicate<Product>() {
                    @Override
                    public boolean test(Product product) throws Exception {
                        return product.getCategory().equals(categoryName);
                    }
                })
                .toList()
                .toObservable();
    }

    public Observable<List<String>> getAllCategories() {

        return getAllProducts().map(new Function<List<Product>, List<String>>() {
            @Override
            public List<String> apply(List<Product> products) throws Exception {

                Set<String> categories = new HashSet<>();

                for (Product p : products) {
                    categories.add(p.getCategory());
                }
                List<String> result = new ArrayList<>(categories.size());

                result.addAll(categories);

                return result;
            }
        });
    }

    public Observable<Product> getProduct(final int id) {
        return getAllProducts()
                .flatMap(new Function<List<Product>, ObservableSource<? extends Product>>() {
                    @Override
                    public ObservableSource<? extends Product> apply(List<Product> products) throws Exception {
                        return Observable.fromIterable(products);
                    }
                })
                .filter(new Predicate<Product>() {
                    @Override
                    public boolean test(Product product) throws Exception {
                        return id == product.getId();
                    }
                }).take(1);
    }
}
