package main.com.ngrewards.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import main.com.ngrewards.Models.ModalMenuList;
import main.com.ngrewards.R;
import main.com.ngrewards.RecyclerViewClickListener1;
import main.com.ngrewards.activity.IMethodCaller;
import main.com.ngrewards.activity.MerchantMenuSetting;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.draweractivity.AddMenuPublish;

/**
 * Created by admin4 on 11/5/2016.
 */
public class    AdapterMenu extends RecyclerView.Adapter<AdapterMenu.MyViewHolder> {

    private final ArrayList<ModalMenuList> all_category_subcategory;
    private final IMethodCaller IMethode;
    private View itemView;
    private String desage_name_string;
    private char first_char;
    private final Context activity;
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
    private final RecyclerViewClickListener1 mListener;
    private String h;
    private String name_item;
    private String image_menu;
    private Menu_ListItem_adapter adapterMenu;
    private String status;
    private int current_offer_pos;
    private String discription_item;
    private String title_item;
    private String price_item;
    private String image_item;
    private String menu_id;
    private  MySession mySession;

    public AdapterMenu(Context a, ArrayList<ModalMenuList> all_category_subcategory, RecyclerViewClickListener1 listener, IMethodCaller IMethode) {
        this.activity = a;
        this.all_category_subcategory = all_category_subcategory;
        this.mListener = listener;
        this.IMethode = IMethode;
    }

    @Override
    public AdapterMenu.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_adapter, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {
mySession = new MySession(activity);
        desage_name_string = all_category_subcategory.get(i).getName();
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
                MenuItem bedMenuItem = popup.getMenu().findItem(R.id.nav_messages);
                status = all_category_subcategory.get(i).getStatus();

                if (status.equalsIgnoreCase("publish")) {
                    // APIStatusHide(fghu);
                    bedMenuItem.setTitle(activity.getString(R.string.hide));

                } else {
                    //APIStatusPublish(fghu);
                    bedMenuItem.setTitle(activity.getString(R.string.publish));
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        switch (id) {

                            case R.id.nav_home:

                                fghu = all_category_subcategory.get(i).getItem_id();
                                discription_item = all_category_subcategory.get(i).getTitleDiscription();
                                title_item = all_category_subcategory.get(i).getTitle();
                                price_item = all_category_subcategory.get(i).getPrice();
                                image_item = all_category_subcategory.get(i).getImage();
                                menu_id = all_category_subcategory.get(i).getMenu_id();

                                Intent intent = new Intent(activity, AddMenuPublish.class);
                                intent.putExtra("id_item", fghu);
                                intent.putExtra("name_item", name_item);
                                intent.putExtra("publish", "edit");
                                intent.putExtra("discription", discription_item);
                                intent.putExtra("title_item", title_item);
                                intent.putExtra("price_item", price_item);
                                intent.putExtra("image_item", image_item);
                                intent.putExtra("menu_id", menu_id);

                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);

                                IMethode.yourDesiredMethod();


                                //  APIStatusEdit(fghu);

                                return true;

                            case R.id.nav_messages:

                                Toast.makeText(activity, status, Toast.LENGTH_SHORT).show();

                                fghu = all_category_subcategory.get(i).getItem_id();
                                status = all_category_subcategory.get(i).getStatus();

                                if (status.equalsIgnoreCase("publish")) {

                                    APIStatusHide(fghu);

                                } else {

                                    APIStatusPublish(fghu);
                                }
                                return true;

                            case R.id.nav_friends:

                                Toast.makeText(activity, fghu, Toast.LENGTH_SHORT).show();

                                fghu = all_category_subcategory.get(i).getItem_id();

                                current_offer_pos = i;

                                APIStatusHide1(i, fghu);

                                //   APIStatusHide1(fghu);

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
        holder.name_menu.setText(desage_name_string);
        holder.dish_name.setText(dish_title_string);
        holder.discription.setText("Discription : " + discription_title_string);
        holder.price_tv.setText(mySession.getValueOf(MySession.CurrencySign)+" "  + Item_Price);

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_item = all_category_subcategory.get(i).getId();

                mListener.onClick1(id_item);
                // UpdateCategory(id_item);
            }
        });


        holder.add_menu_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id_item = all_category_subcategory.get(i).getId();
                name_item = all_category_subcategory.get(i).getName();

                Intent intent = new Intent(activity, AddMenuPublish.class);
                intent.putExtra("id_item", id_item);
                intent.putExtra("name_item", name_item);
                intent.putExtra("publish", "add_menu");
                intent.putExtra("discription", "");
                intent.putExtra("title_item", "");
                intent.putExtra("price_item", "");
                intent.putExtra("image_item", "");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);

            }
        });

        holder.linearFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent intent = new Intent(activity, Die_TakeOut.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);*/

            }
        });

        list = all_category_subcategory.get(i).getTitle();

        if (status == null) {
            holder.InearLinearLayoutCards.setVisibility(View.GONE);
        } else {
            //  holder.menu_list_recycler.setVisibility(View.VISIBLE);
            holder.InearLinearLayoutCards.setVisibility(View.VISIBLE);
            Toast.makeText(activity, list, Toast.LENGTH_SHORT).show();
        }

        if (list == null) {
            status = all_category_subcategory.get(i).getStatus();
            // holder.menu_list_recycler.setVisibility(View.GONE);
            holder.InearLinearLayoutCards.setVisibility(View.GONE);

        } else {
            //  holder.menu_list_recycler.setVisibility(View.VISIBLE);
            holder.InearLinearLayoutCards.setVisibility(View.VISIBLE);
            Toast.makeText(activity, list, Toast.LENGTH_SHORT).show();

        }

        Picasso.with(activity).load(BaseUrl.image_baseurl + image_menu).into(holder.image_menu_item);

    }

    private void APIStatusPublish(String id_item) {

        AndroidNetworking.get("https://myngrewards.com/wp-content/plugins/webservice/update_item_status.php?id=" + id_item + "&status=publish")
                .addPathParameter("pageNumber", "0")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String status = response.getString("status");
                            String message = response.getString("message");


                            if (status.equals("1")) {

                                Intent intent = new Intent(activity, AddMenuPublish.class);
                                intent.putExtra("id", fghu);
                                intent.putExtra("update", "update");
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

    }

    private void APIStatusHide(String id_item) {

        Log.e("id_item", id_item);
        AndroidNetworking.get("https://myngrewards.com/wp-content/plugins/webservice/update_item_status.php?id=" + id_item + "&status=hide")
                .addPathParameter("pageNumber", "0")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String status = response.getString("status");
                            String message = response.getString("message");


                            if (status.equals("1")) {

                                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

    }

    private void APIStatusHide1(int i, String id_item) {
        AndroidNetworking.get("https://myngrewards.com/wp-content/plugins/webservice/delete_item.php?id=" + id_item)
                .addPathParameter("pageNumber", "0")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String status = response.getString("status");
                            String message = response.getString("message");

                            if (status.equals("1")) {

                                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(activity, MerchantMenuSetting.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

    }

    private void APIStatusEdit(String fghu) {

        APIStatusPublish(fghu);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView name_menu, dish_name;
        private final LinearLayout InearLinearLayoutCards;
        private final TextView discription;
        private final TextView price_tv;
        private final LinearLayout linearFour;
        private final ImageButton mImageButton;
        private final ImageButton add_menu_img_btn;
        private final TextView edit_btn;
        private final ImageView image_menu_item;
        private final RecyclerView menu_list_recycler;


        public MyViewHolder(View itemView) {

            super(itemView);

            name_menu = itemView.findViewById(R.id.name_menu);
            dish_name = itemView.findViewById(R.id.dish_name);
            InearLinearLayoutCards = itemView.findViewById(R.id.InearLinearLayoutCards);
            linearFour = itemView.findViewById(R.id.linearFour);
            discription = itemView.findViewById(R.id.discription);
            price_tv = itemView.findViewById(R.id.price_tv);
            mImageButton = itemView.findViewById(R.id.mImageButton);
            edit_btn = itemView.findViewById(R.id.edit_btn);
            add_menu_img_btn = itemView.findViewById(R.id.add_menu_img_btn);
            image_menu_item = itemView.findViewById(R.id.image_menu_item);
            menu_list_recycler = itemView.findViewById(R.id.menu_list_recycler);

        }
    }


    @Override
    public int getItemCount() {

        return all_category_subcategory.size();

    }

}

