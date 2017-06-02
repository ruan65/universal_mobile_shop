package org.premiumapp.universalmobileshop.biz_logic.search_engine;

import android.support.annotation.NonNull;

import org.premiumapp.universalmobileshop.biz_logic.http.ProductBackendApiDecorator;
import org.premiumapp.universalmobileshop.biz_logic.model.Product;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by a on 01.06.17.
 */

public class SearchEngine {

    private final ProductBackendApiDecorator backend;

    public SearchEngine(ProductBackendApiDecorator backend) {
        this.backend = backend;
    }

    public Observable<List<Product>> searchFor(final String query) {

        if (null == query) {
            return Observable.error(new NullPointerException("Search query is null"));
        }

        if (query.isEmpty()) {
            return Observable.error(new IllegalArgumentException("Search query is blank."));
        }

        return backend.getAllProducts()
                .delay(1000, TimeUnit.MILLISECONDS)
                .flatMap(new Function<List<Product>, ObservableSource<? extends Product>>() {
                    @Override
                    public ObservableSource<? extends Product> apply(List<Product> products) throws Exception {
                        return Observable.fromIterable(products);
                    }})
                .filter(new Predicate<Product>() {
                    @Override
                    public boolean test(Product product) throws Exception {
                        return isProductMatchingSearchCriteria(product, query);
                    }})
                .toList()
                .toObservable();
    }

    private boolean isProductMatchingSearchCriteria(Product product, String searchQueryText) {
        String words[] = searchQueryText.split(" ");
        for (String w : words) {
            if (product.getName().contains(w)) return true;
            if (product.getDescription().contains(w)) return true;
            if (product.getCategory().contains(w)) return true;
        }
        return false;
    }
}
