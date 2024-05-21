package imat;

import se.chalmers.cse.dat216.project.ProductCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Search {
    private String textSearch = null;
    private List<ProductCategory> categories = null;
    private final SortOrder sortOrder;

    public Search(String textSearch, SortOrder sort) {
        this.textSearch = textSearch;
        this.sortOrder = sort;
    }

    public Search(List<ProductCategory> categories, SortOrder sort) {
        this.categories = categories;
        this.sortOrder = sort;
    }

    public String getTextSearch() {
        return textSearch;
    }

    public List<ProductCategory> getCategory() {
        return categories;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }
}