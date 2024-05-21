package imat;

import se.chalmers.cse.dat216.project.Product;

public interface ShoppingItemObserver {
    public void updateShoppingItemObserver(Product product, boolean isAdd);
}
