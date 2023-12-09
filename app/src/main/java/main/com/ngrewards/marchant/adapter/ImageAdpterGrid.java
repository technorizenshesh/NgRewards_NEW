package main.com.ngrewards.marchant.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import main.com.ngrewards.R;
import main.com.ngrewards.SquareImageView;

/**
 * Created by technorizen on 15/11/17.
 */

public class ImageAdpterGrid extends BaseAdapter {
    private final Context mContext;
    private final SparseBooleanArray mSparseBooleanArray;
    private final SparseBooleanArray mSparseBooleanArray_new;
    LayoutInflater inflater;
    private ArrayList<String> mImagesList;

    public ImageAdpterGrid(Context context, ArrayList<String> imageList) {
        mContext = context;
        mSparseBooleanArray = new SparseBooleanArray();
        mSparseBooleanArray_new = new SparseBooleanArray();
        mImagesList = new ArrayList<String>();
        this.mImagesList = imageList;
        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public ArrayList<String> getCheckedItems() {
        ArrayList<String> mTempArry = new ArrayList<String>();

        for (int i = 0; i < mImagesList.size(); i++) {
            if (mSparseBooleanArray_new.get(i)) {
                mTempArry.add(mImagesList.get(i));
            }
        }

        return mTempArry;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub

        return mImagesList == null ? 0 : mImagesList.size();

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
        View rowView;
        CheckBox checkBox;
        final SquareImageView imageView;
        final ImageView selectedview, blur_back;

        rowView = inflater.inflate(R.layout.row_multiphoto_item, null);


        checkBox = (CheckBox) rowView.findViewById(R.id.checkBox1);
        imageView = (SquareImageView) rowView.findViewById(R.id.imageView1);
        selectedview = (ImageView) rowView.findViewById(R.id.selectedview);
        blur_back = (SquareImageView) rowView.findViewById(R.id.blur_back);
        if (mSparseBooleanArray_new.get(position)) {
            blur_back.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.last_trans));
            selectedview.setVisibility(View.VISIBLE);

        } else {

            blur_back.setBackgroundColor(mContext.getResources().getColor(R.color.trans));
            selectedview.setVisibility(View.GONE);

        }
        String imageUrl = mImagesList.get(position);
        Log.e("Come Bind", "");
        Glide.with(mContext)
                .load("file://" + imageUrl)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.unselectitem)
                .into(imageView);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSparseBooleanArray_new.get(position)) {
                    mSparseBooleanArray_new.put(position, false);
                    blur_back.setBackgroundColor(mContext.getResources().getColor(R.color.trans));
                    selectedview.setVisibility(View.GONE);
                } else {
                    mSparseBooleanArray_new.put(position, true);
                    blur_back.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.last_trans));
                    selectedview.setVisibility(View.VISIBLE);

                }
            }
        });
        return rowView;
    }

    public class Holder {

    }

}
