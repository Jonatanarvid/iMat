package imat;

import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;

import java.util.ArrayList;
import java.util.List;

public class BackendController {
    private final IMatDataHandler dataHandler;

    public BackendController() {
        dataHandler = IMatDataHandler.getInstance();
    }

    private List<Product> getFilteredProducts(Search search) {
        List<Product> products;

        if(search.getTextSearch().isPresent()) {
            products = dataHandler.findProducts(search.getTextSearch().get());
        }
        else if (search.getCategory().isPresent()) {
            products = CategoryHandler.getProducts(search.getCategory().get(), dataHandler);
        }
        else {
            products = dataHandler.getProducts();
        }
        return products;
    }

    public List<Product> getSortedProducts(Search search) {
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

    public String getIMatDirectory() {
        return dataHandler.imatDirectory();
    }
}