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

    //public List<Product> getSortedProducts(ProductCategory category) {}

   // public List<Product> getSortedProducts() {}

    public String getIMatDirectory() {
        return dataHandler.imatDirectory();
    }
}