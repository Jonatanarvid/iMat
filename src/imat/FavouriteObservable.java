package imat;

public interface FavouriteObservable {
    public void addFavouriteObserver(FavouriteObserver observer);
    public void removeFavouriteObserver(FavouriteObserver observer);
    public void notifyFavouriteObservers();

}
