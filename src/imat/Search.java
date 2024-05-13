package imat;

import se.chalmers.cse.dat216.project.ProductCategory;

import java.util.Optional;

public class Search {
    Optional<String> textSearch;
    Optional<ProductCategory> category;
    SortOrder sort;

    public Search(Optional<String> textSearch, Optional<ProductCategory> category, SortOrder sort) {
        this.textSearch = textSearch;
        this.category = category;
        this.sort = sort;
    }

    public Optional<String> getTextSearch() {
        return textSearch;
    }

    public Optional<ProductCategory> getCategory() {
        return category;
    }

    public SortOrder getSort() {
        return sort;
    }
}
