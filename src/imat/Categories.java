package imat;

import se.chalmers.cse.dat216.project.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public enum Categories {
    FISH_ETC,
    MEAT_ETC,
    DRINKS,
    DAIRY_AND_EGG,
    FRUIT_AND_VEG,
    BAKERY,
    SHELF
    ;

    public List<ProductCategory> convertToListOfProductCategory() {
        ArrayList<ProductCategory> productCategories = new ArrayList<ProductCategory>();
        switch (this) {
            case FISH_ETC -> {
                productCategories.add(ProductCategory.FISH);
            }
            case MEAT_ETC -> {
                productCategories.add(ProductCategory.MEAT);
            }
            case DRINKS -> {
                productCategories.add(ProductCategory.COLD_DRINKS);
                productCategories.add(ProductCategory.HOT_DRINKS);
            }
            case DAIRY_AND_EGG -> {
                productCategories.add(ProductCategory.DAIRIES);
            }
            case FRUIT_AND_VEG -> {
                productCategories.add(ProductCategory.POD);
                productCategories.add(ProductCategory.BERRY);
                productCategories.add(ProductCategory.CITRUS_FRUIT);
                productCategories.add(ProductCategory.EXOTIC_FRUIT);
                productCategories.add(ProductCategory.VEGETABLE_FRUIT);
                productCategories.add(ProductCategory.CABBAGE);
                productCategories.add(ProductCategory.MELONS);
                productCategories.add(ProductCategory.ROOT_VEGETABLE);
                productCategories.add(ProductCategory.FRUIT);
                productCategories.add(ProductCategory.HERB);
            }
            case BAKERY -> {
                productCategories.add(ProductCategory.BREAD);
                productCategories.add(ProductCategory.SWEET);
            }
            case SHELF -> {
                productCategories.add(ProductCategory.POTATO_RICE);
                productCategories.add(ProductCategory.PASTA);
                productCategories.add(ProductCategory.NUTS_AND_SEEDS);
                productCategories.add(ProductCategory.FLOUR_SUGAR_SALT);
            }
        }
        return productCategories;
    }
}

