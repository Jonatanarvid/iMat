package imat;

import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.util.List;

public interface SearchObservable {
    public void addSearchObserver(SearchObserver observer);
    public void removeSearchObserver(SearchObserver observer);
    public void notifySearchObservers(List<ProductCategory> products);
}
