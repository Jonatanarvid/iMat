package imat;

import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public class BackendController {
    private final IMatDataHandler dataHandler;

    public BackendController() {
        dataHandler = IMatDataHandler.getInstance();
    }

    private List<Product> getFilteredProducts(Search search) {
        List<Product> products = new ArrayList<Product>();

        if(search.getTextSearch() != null) {
            products = dataHandler.findProducts(search.getTextSearch());
        }
        else {
            List<ProductCategory> categories = search.getCategory();
            if(search.getCategory().isEmpty()) {
                products = dataHandler.getProducts();
            }
            else {
                for (ProductCategory category : categories) {
                    products.addAll(dataHandler.getProducts(category));
                }
            }
        }
        return products;
    }

    private List<Product> getSortedProducts(Search search) {
        List<Product> newSearch = getFilteredProducts(search);

        switch (search.getSortOrder()) {
            case ALPHA -> {
                newSearch.sort((a, b) -> {return a.getName().compareTo(b.getName());});
            }
            case REVERSEALPHA -> {
                newSearch.sort((a, b) -> {return -1*a.getName().compareTo(b.getName());});
            }
            case PRICELOWHIGH -> {
                newSearch.sort((a, b) -> {return (int) (a.getPrice() - b.getPrice());});
            }
            case PRICEHIGHLOW -> {
                newSearch.sort((a, b) -> {return (int) (-1*(a.getPrice() - b.getPrice()));});
            }
        }
        return newSearch;
    }

    public List<Product> getSearchResults(Search search) {
        return getSortedProducts(search);
    }

    public String getIMatDirectory() {
        return dataHandler.imatDirectory();
    }
}