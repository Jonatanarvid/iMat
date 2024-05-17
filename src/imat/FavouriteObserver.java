package imat;

import se.chalmers.cse.dat216.project.Product;

public interface FavouriteObserver {
    public void update(Product product, Boolean isFavourite);
}
