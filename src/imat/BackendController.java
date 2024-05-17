package imat;

import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BackendController implements ProductCardObservable, FavouriteObserver {
    private final IMatDataHandler dataHandler;
    private final List<ProductCardObserver> observers = new ArrayList<ProductCardObserver>();
    private List<Product> products;
    private final HashMap<Product, ProductCard> productCards = new HashMap<Product, ProductCard>();

    public BackendController() {
        dataHandler = IMatDataHandler.getInstance();
    }

    public void start() {
        for(Product product : dataHandler.getProducts()) {
            ProductCard productCard = new ProductCard(product, dataHandler.getFXImage(product));
            productCard.addObserver(this);
            productCards.put(product, productCard);
        }
    }
//weee
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

    public void newSearch(Search search) {
        this.products = getSortedProducts(search);
    }

    public String getIMatDirectory() {
        return dataHandler.imatDirectory();
    }

    @Override
    public void addObserver(ProductCardObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ProductCardObserver observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    private List<ProductCard> getCardsFromProducts() {
        List<ProductCard> productCards = new ArrayList<ProductCard>();
        for(Product product : products) {
            productCards.add(this.productCards.get(product));
        }
        return productCards;
    }
    @Override
    public void notifyObservers() {
        for (ProductCardObserver observer : observers) {
            observer.update(getCardsFromProducts());
        }
    }

    @Override
    public void update(Product product, Boolean isFavourite) {
        if (isFavourite) {
            dataHandler.removeFavorite(product);
        } else {
            dataHandler.addFavorite(product);
        }
    }
}