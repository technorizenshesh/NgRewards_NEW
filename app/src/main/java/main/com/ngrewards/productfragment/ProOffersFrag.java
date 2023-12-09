package main.com.ngrewards.productfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import main.com.ngrewards.R;
import main.com.ngrewards.constant.ExpandableHeightListView;

/**
 * Created by technorizen on 13/6/18.
 */

public class ProOffersFrag extends Fragment {

    ArrayList<Boolean> selecteded;
    View v;
    private ExpandableHeightListView product_offer;
    private OffersAdpter offersAdpter;
    private RecyclerView offers_product_rec;

    public ProOffersFrag() {
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
        v = inflater.inflate(R.layout.pro_offerfrag_lay, container, false);
        selecteded = new ArrayList<>();
        selecteded.add(false);
        selecteded.add(false);
        idnit();
        return v;
    }

    private void idnit() {
        //product_offer = v.findViewById(R.id.product_offer);
        offers_product_rec = v.findViewById(R.id.offers_product_rec);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        offers_product_rec.setLayoutManager(horizontalLayoutManagaer);
        //product_offer.setExpanded(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        offersAdpter = new OffersAdpter(selecteded);
        offers_product_rec.setAdapter(offersAdpter);
        offersAdpter.notifyDataSetChanged();
    }

    class OffersAdpter extends RecyclerView.Adapter<OffersAdpter.MyViewHolder> {
        ArrayList<Boolean> selected;

        public OffersAdpter(ArrayList<Boolean> selected) {
            this.selected = selected;
        }

        @Override
        public OffersAdpter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_pro_offers_lay, parent, false);
            MyViewHolder myViewHolder = new OffersAdpter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final OffersAdpter.MyViewHolder holder, final int listPosition) {
            if (selected.get(listPosition)) {
                holder.likeimg.setImageResource(R.drawable.filled_like);
                holder.liketv.setText("" + getResources().getString(R.string.dislike));
            } else {
                holder.likeimg.setImageResource(R.drawable.ic_like);
                holder.liketv.setText("" + getResources().getString(R.string.like));
            }
            holder.sharelay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            });
            holder.likebut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selected.get(listPosition)) {
                        selecteded.set(listPosition, false);
                        offersAdpter = new OffersAdpter(selecteded);
                        offers_product_rec.setAdapter(offersAdpter);
                        offersAdpter.notifyDataSetChanged();
                    } else {
                        selecteded.set(listPosition, true);
                        offersAdpter = new OffersAdpter(selecteded);
                        offers_product_rec.setAdapter(offersAdpter);
                        offersAdpter.notifyDataSetChanged();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return 2;
            // return myCarBeanArrayList == null ? 0 : myCarBeanArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView liketv;
            ImageView likeimg;
            LinearLayout sharelay, likebut;


            public MyViewHolder(View itemView) {
                super(itemView);
                // this.viewLine = (View) itemView.findViewById(R.id.viewLine);
                this.sharelay = itemView.findViewById(R.id.sharelay);
                this.likebut = itemView.findViewById(R.id.likebut);
                this.likeimg = itemView.findViewById(R.id.likeimg);
                this.liketv = itemView.findViewById(R.id.liketv);

            }
        }
    }

}
