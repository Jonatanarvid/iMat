package imat;

public interface ShoppingItemObservable {
    public void addShoppingItemObservable(ShoppingItemObserver shoppingItemObserver);
    public void removeShoppingItemObserver(ShoppingItemObserver shoppingItemObserver);
    public void notifyShoppingItemObservers();
}
