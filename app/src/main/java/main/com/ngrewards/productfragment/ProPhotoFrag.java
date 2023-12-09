package main.com.ngrewards.productfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.ProduCtDetailAct;
import main.com.ngrewards.beanclasses.ProductImage;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.ExpandableHeightGridView;

/**
 * Created by technorizen on 13/6/18.
 */

public class ProPhotoFrag extends Fragment {
    View v;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ExpandableHeightGridView product_photo;
    private RecyclerView product_photo_rec;
    private ProductPhotoAdp productPhotoAdp;

    public ProPhotoFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.pro_photofrag_lay, container, false);
        idint();

        return v;
    }

    private void idint() {
        product_photo = v.findViewById(R.id.product_photo);
        product_photo_rec = v.findViewById(R.id.product_photo_rec);
        recyclerViewLayoutManager = new GridLayoutManager(getActivity(), 3);
        product_photo_rec.setLayoutManager(recyclerViewLayoutManager);
        product_photo.setExpanded(true);

    }

    @Override
    public void onResume() {
        super.onResume();
       /* customPhotoAdp = new CustomPhotoAdp(getActivity());
        product_photo.setAdapter(customPhotoAdp);*/
        if (ProduCtDetailAct.productDetailArrayList != null && !ProduCtDetailAct.productDetailArrayList.isEmpty()) {
            productPhotoAdp = new ProductPhotoAdp(ProduCtDetailAct.productDetailArrayList.get(0).getProductImages());
            product_photo_rec.setAdapter(productPhotoAdp);
            productPhotoAdp.notifyDataSetChanged();
        }

    }

    class ProductPhotoAdp extends RecyclerView.Adapter<ProductPhotoAdp.MyViewHolder> {
        List<ProductImage> productImages;

        public ProductPhotoAdp(List<ProductImage> productImages) {
            this.productImages = productImages;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_photo_lay, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            String product_img = productImages.get(listPosition).getProductImage();
            if (product_img == null || product_img.equalsIgnoreCase("") || product_img.equalsIgnoreCase(BaseUrl.image_baseurl)) {

            } else {
                Glide.with(getActivity())
                        .load(product_img)
                        .thumbnail(0.5f)
                        .override(400, 150)
                        .centerCrop()

                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)

                        .into(holder.product_photos);

            }
        }

        @Override
        public int getItemCount() {
            // return 4;
            return productImages == null ? 0 : productImages.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView product_photos;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.product_photos = itemView.findViewById(R.id.product_photos);
            }
        }
    }


}
