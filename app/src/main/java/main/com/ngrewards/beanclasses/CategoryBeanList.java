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
    @SerializedName("category_name_spanish")
    @Expose
    private String category_name_spanish;
    @SerializedName("category_name_hindi")
    @Expose
    private String category_name_hindi;

    @SerializedName("sub_catagories_count")
    @Expose
    private Integer subCatagoriesCount;
    @SerializedName("product_list_count")
    @Expose
    private Integer productListCount;

    public String getCategory_name_spanish() {
        return category_name_spanish;
    }

    public void setCategory_name_spanish(String category_name_spanish) {
        this.category_name_spanish = category_name_spanish;
    }

    public String getCategory_name_hindi() {
        return category_name_hindi;
    }

    public void setCategory_name_hindi(String category_name_hindi) {
        this.category_name_hindi = category_name_hindi;
    }

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
