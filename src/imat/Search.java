package imat;

import se.chalmers.cse.dat216.project.ProductCategory;

import java.util.Optional;

public class Search {
    private Optional<String> textSearch;
    private Optional<Categories> category;
    private SortOrder sortOrder;

public Search(Optional<String> textSearch, Optional<Categories> category, SortOrder sort) {
        this.textSearch = textSearch;
        this.category = category;
        this.sortOrder = sort;
    }

    public Optional<String> getTextSearch() {
        return textSearch;
    }

    public Optional<Categories> getCategory() {
        return category;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }
}
