package main.com.ngrewards.showzoomableimages;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.viewpagerindicator.CirclePageIndicator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.FragItemDetails;
import main.com.ngrewards.beanclasses.ProductImage;

public class FullScreenImagesActivity extends AppCompatActivity {
    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;
    ViewPager viewPager;
    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    int mode = NONE;
    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    String mstatus = "1";
    private ImageView finish_page;
    private ZoomableImageView zoom;
    private RelativeLayout backlay;
    private CirclePageIndicator indicator;

    //popup method
    public static void setForceShowIcon(PopupMenu popupMenu) {
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        //  viewPager = (ExtendedViewPager) findViewById(R.id.pager);
        viewPager = (ViewPager) findViewById(R.id.pager);
        finish_page = (ImageView) findViewById(R.id.finish_page);
        backlay = (RelativeLayout) findViewById(R.id.backlay);
        //  zoom = (ZoomableImageView) findViewById(R.id.zoom);
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        List<ProductImage> imagePaths = null;
        Bundle extra = getIntent().getExtras();
        if (FragItemDetails.productDetailArrayList != null) {
            imagePaths = FragItemDetails.productDetailArrayList.get(0).getProductImages();

        }
        int position = extra.getInt("position");
        String status = extra.getString("status");


        TouchImageAdapter adapter = new TouchImageAdapter(this, imagePaths);


        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

        indicator = (CirclePageIndicator) findViewById(R.id.fullscreen_indecator);

        indicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);

    }

    static class TouchImageAdapter extends PagerAdapter {
        private final Activity _activity;
        private final List<ProductImage> _imagePaths;
        private LayoutInflater inflater;

        //     private static int[] images = { R.drawable.img1, R.drawable.img2, R.drawable.img3 };


        public TouchImageAdapter(Activity activity,
                                 List<ProductImage> imagePaths) {
            this._activity = activity;
            this._imagePaths = imagePaths;
        }

        @Override
        public int getCount() {
            return _imagePaths == null ? 0 : _imagePaths.size();
            //return _imagePaths.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {


            TouchImageView img = new TouchImageView(container.getContext());
            //img.setImageResource(_imagePaths.get(position));

            if (_imagePaths.get(position).getProductImage() == null || _imagePaths.get(position).getProductImage().equalsIgnoreCase("")) {

            } else {
                Glide.with(_activity).load(_imagePaths.get(position).getProductImage()).into(img);
            }


            container.addView(img, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }


}





