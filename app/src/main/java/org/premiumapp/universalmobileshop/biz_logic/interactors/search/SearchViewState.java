package org.premiumapp.universalmobileshop.biz_logic.interactors.search;

import org.premiumapp.universalmobileshop.biz_logic.model.Product;

import java.util.List;

/**
 * Created by a on 01.06.17.
 */

public interface SearchViewState {

    final class SearchNotStartedYet implements SearchViewState {

    }

    final class Loading implements SearchViewState {

    }

    final class EmptyResult implements SearchViewState {

        private final String searchQueryText;

        public EmptyResult(String searchQueryText) {
            this.searchQueryText = searchQueryText;
        }

        public String getSearchQueryText() {
            return searchQueryText;
        }
    }

    final class SearchResult implements SearchViewState {

        private final String searchQueryText;
        private final List<Product> result;


        public SearchResult(String searchQueryText, List<Product> result) {
            this.searchQueryText = searchQueryText;
            this.result = result;
        }

        public String getSearchQueryText() {
            return searchQueryText;
        }

        public List<Product> getResult() {
            return result;
        }

        @Override
        public String toString() {
            return "SearchResult{" +
                    "searchQueryText='" + searchQueryText + '\'' +
                    ", result=" + result +
                    '}';
        }
    }

    final class Error implements SearchViewState {
        private final String searchQueryText;
        private final Throwable error;

        public Error(String searchQueryText, Throwable error) {
            this.searchQueryText = searchQueryText;
            this.error = error;
        }

        public String getSearchQueryText() {
            return searchQueryText;
        }

        public Throwable getError() {
            return error;
        }

        @Override public String toString() {
            return "Error{" +
                    "searchQueryText='" + searchQueryText + '\'' +
                    ", error=" + error +
                    '}';
        }
    }
}
