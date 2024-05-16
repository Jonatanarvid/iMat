package imat;

import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductDetail;

import java.util.List;

public class DetailedProduct {
    private Product product;
    private ProductDetail productDetail;
    private List<Categories> categories;

    public DetailedProduct(Product product, ProductDetail productDetail, List<Categories> categories) {
        this.product = product;
        this.productDetail = productDetail;
        this.categories = categories;
    }

    public Product getProduct() {
        return product;
    }

    public ProductDetail getDetails() {
        return productDetail;
    }
}
