package main.com.ngrewards.beanclasses;

/**
 * Created by technorizen on 27/7/18.
 */

public class GalleryBean {
    String id;
    String merchant_id;
    String gallery_image;
    String created_date;
    String status_taken;
    String like_count;
    String like_status;


    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getLike_status() {
        return like_status;
    }

    public void setLike_status(String like_status) {
        this.like_status = like_status;
    }

    public String getStatus_taken() {
        return status_taken;
    }

    public void setStatus_taken(String status_taken) {
        this.status_taken = status_taken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getGallery_image() {
        return gallery_image;
    }

    public void setGallery_image(String gallery_image) {
        this.gallery_image = gallery_image;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
