package org.premiumapp.universalmobileshop.biz_logic.interactors.search;

import org.premiumapp.universalmobileshop.biz_logic.model.Product;
import org.premiumapp.universalmobileshop.biz_logic.search_engine.SearchEngine;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by a on 01.06.17.
 */

public class SearchInteractor {

    private final SearchEngine searchEngine;

    public SearchInteractor(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    public Observable<SearchViewState> search(final String query) {

        if (query.isEmpty()) {

            SearchViewState state = new SearchViewState.SearchNotStartedYet();

            return Observable.just(state);
        }

        return searchEngine.searchFor(query)
                .map(new Function<List<Product>, SearchViewState>() {
                    @Override
                    public SearchViewState apply(List<Product> products) throws Exception {

                        if (products.isEmpty()) {
                            return new SearchViewState.EmptyResult(query);
                        } else {
                            return new SearchViewState.SearchResult(query, products);
                        }
                    }
                })
                .startWith(new SearchViewState.Loading())
                .onErrorReturn(new Function<Throwable, SearchViewState>() {
                    @Override
                    public SearchViewState apply(Throwable throwable) throws Exception {
                        return new SearchViewState.Error(query, throwable);
                    }
                });
    }
}
