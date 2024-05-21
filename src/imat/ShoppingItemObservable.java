package imat;

public interface ShoppingItemObservable {
    public void addShoppingItemObserver(ShoppingItemObserver shoppingItemObserver);
    public void removeShoppingItemObserver(ShoppingItemObserver shoppingItemObserver);
    public void notifyShoppingItemObservers();
}
