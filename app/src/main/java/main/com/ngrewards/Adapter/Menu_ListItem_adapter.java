package main.com.ngrewards.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import main.com.ngrewards.Models.MenuModal;
import main.com.ngrewards.R;
import main.com.ngrewards.RecyclerViewClickListener1;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.draweractivity.AddMenuPublish;

/**
 * Created by admin4 on 11/5/2016.
 */
public class Menu_ListItem_adapter extends RecyclerView.Adapter<Menu_ListItem_adapter.MyViewHolder> {

    private final ArrayList<MenuModal> all_category_subcategory;
    private final Context activity;
    private View itemView;
    private char first_char;
    private String dish_title_string;
    private String discription_title_string;
    private String Item_Price;
    private String fghu;
    private String list;
    private String id_item;
    private LayoutInflater li;
    private View promptsView;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private String editTextString;
    private RecyclerViewClickListener1 mListener;
    private String h;
    private String name_item;
    private String image_menu;
    private MySession mySession;

    public Menu_ListItem_adapter(Context a, ArrayList<MenuModal> all_category_subcategory) {
        this.activity = a;
        this.all_category_subcategory = all_category_subcategory;
    }

    @Override
    public Menu_ListItem_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list_item_adapter, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int i) {
        mySession = new MySession(activity);
        dish_title_string = all_category_subcategory.get(i).getTitle();
        discription_title_string = all_category_subcategory.get(i).getTitleDiscription();
        Item_Price = all_category_subcategory.get(i).getPrice();
        list = all_category_subcategory.get(i).getList();
        image_menu = all_category_subcategory.get(i).getImage();

        if (list == null) {
            holder.InearLinearLayoutCards.setVisibility(View.GONE);
        }

        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(activity, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        switch (id) {
                            case R.id.nav_home:
                                fghu = all_category_subcategory.get(i).getId();

                                Intent intent = new Intent(activity, AddMenuPublish.class);
                                intent.putExtra("id", id_item);
                                intent.putExtra("update", "update");
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);

                                APIStatusEdit();
                                Toast.makeText(activity, fghu, Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.nav_friends:
                                APIStatusHide();
                                Toast.makeText(activity, "clicked Friend", Toast.LENGTH_SHORT).show();

                                return true;
                            default:
                                return false;
                        }
                    }
                });
                // CustomimagePopup();
            }
        });

        /*  Log.e("desage_name_string",desage_name_string);*/
        holder.dish_name.setText(dish_title_string);
        holder.discription.setText(activity.getString(R.string.description_colen) + discription_title_string);
        holder.price_tv.setText(mySession.getValueOf(MySession.CurrencySign) + " " + Item_Price);

        list = all_category_subcategory.get(i).getTitle();

        if (list == null) {
            holder.InearLinearLayoutCards.setVisibility(View.GONE);
        } else {
            holder.InearLinearLayoutCards.setVisibility(View.VISIBLE);
            Toast.makeText(activity, list, Toast.LENGTH_SHORT).show();
        }

        Glide.with(activity).load(BaseUrl.image_baseurl + image_menu).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontTransform()
                .dontAnimate().into(holder.image_menu_item);

    }

    private void APIStatusHide() {

   /*     AndroidNetworking.get("https://myngrewards.com/wp-content/plugins/webservice/menu_list.php?merchant_id=" + user_id)
                .addPathParameter("pageNumber", "0")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            boolean status = response.getString("status").contains("1");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
*/

    }

    private void APIStatusEdit() {

    }

    @Override
    public int getItemCount() {

        return all_category_subcategory.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView dish_name;
        private final LinearLayout InearLinearLayoutCards;
        private final TextView discription;
        private final TextView price_tv;
        private final ImageButton mImageButton;
        private final ImageView image_menu_item;


        public MyViewHolder(View itemView) {
            super(itemView);
            dish_name = itemView.findViewById(R.id.dish_name);
            InearLinearLayoutCards = itemView.findViewById(R.id.InearLinearLayoutCards);
            discription = itemView.findViewById(R.id.discription);
            price_tv = itemView.findViewById(R.id.price_tv);
            mImageButton = itemView.findViewById(R.id.mImageButton);
            image_menu_item = itemView.findViewById(R.id.image_menu_item);

        }
    }

}

