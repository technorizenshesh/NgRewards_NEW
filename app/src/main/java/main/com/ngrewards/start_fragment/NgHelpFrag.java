package main.com.ngrewards.start_fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import main.com.ngrewards.R;
import main.com.ngrewards.constant.CountryBean;

/**
 * Created by technorizen on 5/7/18.
 */

public class NgHelpFrag extends Fragment {
    ArrayList<CountryBean> countryBeanArrayList;
    View v;
    CountryListAdapter countryListAdapter;
    private Spinner country_spn;
    private ProgressBar progresbar;

    public NgHelpFrag() {
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
        v = inflater.inflate(R.layout.nghelp_lay, container, false);
        idinti();
        return v;
    }

    private void idinti() {
        country_spn = v.findViewById(R.id.country_spn);
        progresbar = v.findViewById(R.id.progresbar);
        // new GetCountryList().execute();
/*
        country_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (countryBeanArrayList!=null&&!countryBeanArrayList.isEmpty()){
                    StartSliderAct.country_str=countryBeanArrayList.get(position).getName();
                    StartSliderAct.country_id=countryBeanArrayList.get(position).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
    }


    /*
        public class CountryListAdapter extends BaseAdapter {
            Context context;

            LayoutInflater inflter;
            private ArrayList<CountryBean> values;

            public CountryListAdapter(Context applicationContext, ArrayList<CountryBean> values) {
                this.context = applicationContext;
                this.values = values;

                inflter = (LayoutInflater.from(applicationContext));
            }

            @Override
            public int getCount() {

                return values == null ? 0 : values.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                view = inflter.inflate(R.layout.country_item_lay_flag, null);

                TextView names = (TextView) view.findViewById(R.id.name_tv);
                ImageView country_flag = (ImageView) view.findViewById(R.id.country_flag);
                //  TextView countryname = (TextView) view.findViewById(R.id.countryname);
                if (values.get(i).getFlag_url() == null || values.get(i).getFlag_url().equalsIgnoreCase("")) {

                } else {
                    Glide.with(getActivity())
                            .load(values.get(i).getFlag_url())
                            .thumbnail(0.5f)
                            .override(50, 50)
                            .centerCrop()

                            .diskCacheStrategy(DiskCacheStrategy.ALL)

                            .into(country_flag);
                }


                names.setText(values.get(i).getName());


                return view;
            }
        }
    */
    public class CountryListAdapter extends ArrayAdapter<CountryBean> {
        private final ArrayList<CountryBean> items;
        Context context;
        Activity activity;

        public CountryListAdapter(Context context, int resourceId, ArrayList<CountryBean> aritems) {
            super(context, resourceId, aritems);
            this.context = context;

            this.items = aritems;
        }

        @Override
        public int getCount() {
            return items == null ? 0 : items.size();

            //return items.size();
        }

        @Override
        public CountryBean getItem(int position) {
//		Log.v("", "items.get("+position+")= "+items.get(position));
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
//		final BloodGroupPojo mytempojo = getItem(position);
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.country_header, null);
                holder = new ViewHolder();
                holder.headername = (TextView) convertView.findViewById(R.id.name_tv);
                holder.country_flag = (ImageView) convertView.findViewById(R.id.country_flag);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.headername.setText(items.get(position).getName());
            if (items.get(position).getFlag_url() == null || items.get(position).getFlag_url().equalsIgnoreCase("")) {

            } else {
                Glide.with(getActivity()).load(items.get(position).getFlag_url()).into(holder.country_flag);
            }
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
//			final BloodGroupPojo mytempojo = getItem(position);
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.country_item_lay_flag, null);
                holder = new ViewHolder();
                holder.cartype = (TextView) convertView.findViewById(R.id.name_tv);
                holder.country_flag = (ImageView) convertView.findViewById(R.id.country_flag);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.cartype.setText(items.get(position).getName());
          /*  if (items.get(position).getFlag_url() == null || items.get(position).getFlag_url().equalsIgnoreCase("")) {

            } else {
                Glide.with(getActivity()).load(items.get(position).getFlag_url()).into(holder.country_flag);
            }*/
//			Log.v("", "in custome spinner"+items.get(position).getItemName());


            return convertView;
        }

        private class ViewHolder {
            TextView headername;
            TextView cartype;
            ImageView country_flag;
        }
    }

}