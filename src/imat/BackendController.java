package imat;

import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public class BackendController {
    private final IMatDataHandler dataHandler;
    private List<Product> produts;
    private ProductCategory[] categories;

    public BackendController() {
        dataHandler = IMatDataHandler.getInstance();
        produts = dataHandler.getProducts();
        categories = ProductCategory.values();
    }

    public List<Product> getSortedProducts(Search search) {
        List<Product> newSearch = new ArrayList<Product>();

        if(search.textSearch.isPresent()) {
            newSearch = dataHandler.findProducts(search.textSearch.get());
        }
        else if (search.category.isPresent()) {
            newSearch = dataHandler.getProducts(search.category.get());
        }
        else {
            newSearch = dataHandler.getProducts();
        }

        switch (search.sort) {
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