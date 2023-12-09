package main.com.ngrewards.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.constant.ExpandableHeightListView;

public class NetworkActFirst extends AppCompatActivity {

    private RelativeLayout backlay;
    private ExpandableHeightListView networklist;
    private CustomNetworkList customNetworkList;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_network);
        idinti();
        clickevent();
    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void idinti() {
        backlay = findViewById(R.id.backlay);
        networklist = findViewById(R.id.networklist);
        networklist.setExpanded(true);
        customNetworkList = new CustomNetworkList(this);
        networklist.setAdapter(customNetworkList);
    }

    public class CustomNetworkList extends BaseAdapter {
        Context context;
        private LayoutInflater inflater = null;


        public CustomNetworkList(Context contexts) {
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

            rowView = inflater.inflate(R.layout.custom_network_item, null);
            TextView paybill = rowView.findViewById(R.id.paybill);

            return rowView;
        }

        public class Holder {

        }

    }


}
