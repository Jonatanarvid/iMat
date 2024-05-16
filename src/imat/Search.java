package imat;

import se.chalmers.cse.dat216.project.ProductCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Search {
    private final Optional<String> textSearch;
    private final List<ProductCategory> categories;
    private final SortOrder sortOrder;

    public Search(Optional<String> textSearch, Optional<Categories> category, SortOrder sort) {
        this.textSearch = textSearch;
        this.categories = category.map(Categories::convertToListOfProductCategory).orElseGet(ArrayList::new);
        this.sortOrder = sort;
    }

    public Optional<String> getTextSearch() {
        return textSearch;
    }

    public List<ProductCategory> getCategory() {
        return categories;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }
}
