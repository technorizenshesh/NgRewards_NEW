package main.com.ngrewards.beanclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by technorizen on 19/7/18.
 */

public class CategoryBeanList {
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("sub_catagories_count")
    @Expose
    private Integer subCatagoriesCount;
    @SerializedName("product_list_count")
    @Expose
    private Integer productListCount;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getSubCatagoriesCount() {
        return subCatagoriesCount;
    }

    public void setSubCatagoriesCount(Integer subCatagoriesCount) {
        this.subCatagoriesCount = subCatagoriesCount;
    }

    public Integer getProductListCount() {
        return productListCount;
    }

    public void setProductListCount(Integer productListCount) {
        this.productListCount = productListCount;
    }
}
