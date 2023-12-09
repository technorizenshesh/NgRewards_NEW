package main.com.ngrewards.bottumtab;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import main.com.ngrewards.R;
import main.com.ngrewards.constant.CustomViewPager;
import main.com.ngrewards.draweractivity.BaseActivity;

public class TrasActivityPrevious extends BaseActivity {
    FrameLayout contentFrameLayout;
    CustomViewPager activity_pager;
    ActivityPagerAdp activityPagerAdp;
    private RelativeLayout previouslay, nextlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        contentFrameLayout = (FrameLayout) findViewById(R.id.contentFrame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_tras, contentFrameLayout);
        idinits();
        clickevent();
    }

    private void clickevent() {
        nextlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity_pager.getCurrentItem() < 2) {

                    activity_pager.setCurrentItem(activity_pager.getCurrentItem() + 1);

                }

            }
        });
        previouslay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity_pager.getCurrentItem() > 0) {

                    activity_pager.setCurrentItem(activity_pager.getCurrentItem() - 1);

                }
            }
        });
    }

    private void idinits() {
        activity_pager = findViewById(R.id.activity_pager);
        nextlay = findViewById(R.id.nextlay);
        previouslay = findViewById(R.id.previouslay);
        activity_pager.setPagingEnabled(false);
        activityPagerAdp = new ActivityPagerAdp(TrasActivityPrevious.this);
        activity_pager.setAdapter(activityPagerAdp);
        activity_pager.setOffscreenPageLimit(3);


    }

    public class ActivityPagerAdp extends PagerAdapter {
        Context context;
        ListView activity_list;
        LayoutInflater inflater;

        public ActivityPagerAdp(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 3;
            // return yearlyaomuntbeanArrayList == null ? 0 : yearlyaomuntbeanArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.custom_activity, container,
                    false);
            activity_list = itemView.findViewById(R.id.activity_list);
            CustomListItemAdp customListItemAdp = new CustomListItemAdp(context);
            activity_list.setAdapter(customListItemAdp);
            ((ViewPager) container).addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Remove viewpager_item.xml from ViewPager
            ((ViewPager) container).removeView((LinearLayout) object);

        }
    }

    public class CustomListItemAdp extends BaseAdapter {
        Context context;
        private LayoutInflater inflater = null;


        public CustomListItemAdp(Context contexts) {
            this.context = contexts;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 5;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder;
            holder = new Holder();
            View rowView;


            rowView = inflater.inflate(R.layout.custom_activity_item_lay, null);
            ImageView img_plus = rowView.findViewById(R.id.img_plus);
            final ImageView img_minus = rowView.findViewById(R.id.img_minus);
            final LinearLayout lesslay = rowView.findViewById(R.id.lesslay);
            LinearLayout click_plus_minus = rowView.findViewById(R.id.click_plus_minus);
            click_plus_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lesslay.setVisibility(View.VISIBLE);
                }
            });
            return rowView;
        }

        public class Holder {

        }

    }

}
