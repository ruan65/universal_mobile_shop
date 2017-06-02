package org.premiumapp.universalmobileshop.biz_logic.http;

import org.premiumapp.universalmobileshop.biz_logic.model.Product;
import org.premiumapp.universalmobileshop.di.DI;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by a on 01.06.17.
 */

public interface ProductBackendApi {

    @GET("/sockeqwe/mosby/"
                 + DI.BASE_URL_BRANCH
                 + "/sample-mvi/server/api/products{pagination}.json")
    Observable<List<Product>> getProducts(@Path("pagination") int pagination);
}
