package imat;

import se.chalmers.cse.dat216.project.ProductCategory;

import java.util.List;

public interface SearchObserver {

    void updateListSearchObserver(List<ProductCategory> categories);
}
