package imat;

public interface ProductCardObservable {
    public void addObserver(ProductCardObserver observer);
    public void removeObserver(ProductCardObserver observer);
    public void notifyObservers();
}
