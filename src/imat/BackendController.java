package imat;

import se.chalmers.cse.dat216.project.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BackendController implements ProductCardObservable, FavouriteObserver, ShoppingItemObserver, SearchObserver {
    private final IMatDataHandler dataHandler;
    private final ShoppingCart shoppingCart;
    private final List<ProductCardObserver> productCardObservers = new ArrayList<ProductCardObserver>();
    private List<Product> products;
    public final HashMap<Product, ProductCard> productCards = new HashMap<Product, ProductCard>();
    private SortOrder sortOrder;
    private MainViewController mainViewController;

    public BackendController(IMatDataHandler dataHandler, MainViewController viewController) {
        this.dataHandler = dataHandler;
        this.mainViewController = viewController;
        shoppingCart = dataHandler.getShoppingCart();
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }



    public void start(ProductCardObserver productCardObserver) {
        for(Product product : dataHandler.getProducts()) {
            ProductCard productCard = new ProductCard(product, dataHandler.getFXImage(product), mainViewController);
            productCard.addFavouriteObserver(this);
            productCard.addShoppingItemObserver(this);
            shoppingCart.addShoppingCartListener(productCard);
            productCards.put(product, productCard);
        }
        addProductCardObserver(productCardObserver);
        setSortOrder(SortOrder.PRICELOWHIGH);
        this.products = dataHandler.favorites();
        notifyProductCardObservers();
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

    private List<Product> getSortedProducts(List<Product> products, SortOrder sortOrder) {
        switch (sortOrder) {
            case ALPHA -> {
                products.sort((a, b) -> a.getName().compareTo(b.getName()));
            }
            case REVERSEALPHA -> {
                products.sort((a, b) -> b.getName().compareTo(a.getName()));
            }
            case PRICELOWHIGH -> {
                products.sort((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
            }
            case PRICEHIGHLOW -> {
                products.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
            }
        }
        return products;
    }

    public void newSearch(Search search) {
        this.products = getSortedProducts(getFilteredProducts(search), search.getSortOrder());
        notifyProductCardObservers();
    }

    public String getIMatDirectory() {
        return dataHandler.imatDirectory();
    }

    @Override
    public void addProductCardObserver(ProductCardObserver observer) {
        productCardObservers.add(observer);
    }

    @Override
    public void removeProductCardObserver(ProductCardObserver observer) {
        if (productCardObservers.contains(observer)) {
            productCardObservers.remove(observer);
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
    public void notifyProductCardObservers() {
        for (ProductCardObserver observer : productCardObservers) {
            observer.update(getCardsFromProducts());
        }
    }

    @Override
    public void updateFavouriteObserver(Product product, Boolean isFavourite) {
        if (!isFavourite) {
            dataHandler.removeFavorite(product);
        } else {
            dataHandler.addFavorite(product);
        }
    }

    @Override
    public void updateShoppingItemObserver(Product product, boolean isAdd, boolean isDelete) {
        if(isAdd) {
            shoppingCart.addItem(new ShoppingItem(product, 1), true);
        } else {
            if(isDelete) {
                shoppingCart.fireShoppingCartChanged(new ShoppingItem(product, 0), false);
                shoppingCart.removeProduct(product);
            } else {
                shoppingCart.addItem(new ShoppingItem(product, -1), true);
            }
        }
    }

    @Override
    public void updateListSearchObserver(List<ProductCategory> products) {
        if(products.isEmpty()) {
            this.products = getSortedProducts(dataHandler.favorites(), getSortOrder()); // Or any default sort order
            notifyProductCardObservers();
        } else {
            newSearch(new Search(products, getSortOrder())); // Or pass the desired sort order
        }
    }
}
