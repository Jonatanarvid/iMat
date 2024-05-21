package imat;

public interface ProductCardObservable {
    public void addProductCardObserver(ProductCardObserver observer);
    public void removeProductCardObserver(ProductCardObserver observer);
    public void notifyProductCardObservers();
}
