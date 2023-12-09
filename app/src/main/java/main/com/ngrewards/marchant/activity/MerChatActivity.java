package main.com.ngrewards.marchant.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import main.com.ngrewards.R;

public class MerChatActivity extends AppCompatActivity {
    ConversessionAdapter conversessionAdapter;
    private ListView chat_list;
    private RelativeLayout backlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mer_chat);
        idint();
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

    private void idint() {
        backlay = findViewById(R.id.backlay);
        chat_list = findViewById(R.id.chat_list);
        conversessionAdapter = new ConversessionAdapter(MerChatActivity.this);
        chat_list.setAdapter(conversessionAdapter);
        conversessionAdapter.notifyDataSetChanged();
    }

    public class ConversessionAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater inflater = null;

        public ConversessionAdapter(MerChatActivity chatActivity) {
            // TODO Auto-generated constructor stub
            context = chatActivity;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 4;
            // return qbChatMessages == null ? 0 : qbChatMessages.size();
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
            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.chat_item_newlay, null);

            return rowView;
        }

        public class Holder {
            TextView receivermessage, sendermessage;
            ImageView prof_mess_img;
            LinearLayout layout1, layout2;
        }

    }

}
