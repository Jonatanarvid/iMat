package imat;

public interface FavouriteObservable {
    public void addObserver(FavouriteObserver observer);
    public void removeObserver(FavouriteObserver observer);
    public void notifyObservers();
}
