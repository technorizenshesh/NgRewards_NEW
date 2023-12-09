package main.com.ngrewards.productfragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.fragment.app.Fragment;

import main.com.ngrewards.R;
import main.com.ngrewards.constant.ExpandableHeightListView;

/**
 * Created by technorizen on 13/6/18.
 */

public class ProReviewsFrag extends Fragment {
    View v;
    CustomReviewAdp customReviewAdp;
    private ExpandableHeightListView topreviewlist;

    public ProReviewsFrag() {
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
        // return inflater.inflate(R.layout.pro_reviewfrag_lay, container, false);
        v = inflater.inflate(R.layout.pro_reviewfrag_lay, container, false);
        idint();
        return v;
    }

    private void idint() {
        topreviewlist = v.findViewById(R.id.topreviewlist);
        topreviewlist.setExpanded(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        customReviewAdp = new CustomReviewAdp(getActivity());
        topreviewlist.setAdapter(customReviewAdp);
    }

    public class CustomReviewAdp extends BaseAdapter {
        Context context;
        private LayoutInflater inflater = null;


        public CustomReviewAdp(Context contexts) {
            this.context = contexts;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 2;
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

            rowView = inflater.inflate(R.layout.top_customer_review_lay, null);
            return rowView;
        }

        public class Holder {

        }

    }

}
