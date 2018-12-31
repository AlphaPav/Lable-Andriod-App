package com.example.alphapav.lableapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alphapav.lableapplication.util.Entity;
import com.example.alphapav.lableapplication.util.EntityHistory;
import com.example.alphapav.lableapplication.util.SharedHelper;

import java.util.ArrayList;

public class History_Entity extends AppCompatActivity {

    private SharedHelper sh;
    private ArrayList<Entity> all_entity_history;
    private ListView listView;
    private MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history__enitity);

        sh= new SharedHelper(getApplicationContext());
        // get the data of relation history
        EntityHistory entityHistory = Entity.getEntityHistory(sh.readUser().get("username"), getApplication());
        all_entity_history = entityHistory.all_history_entity;
        listView = (ListView)findViewById(R.id.listView);

        // set them
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);
    }


    private class MyAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public MyAdapter(Context context){
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return all_entity_history.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.history_item_layout, null);
            // set title
            TextView doc = (TextView)convertView.findViewById(R.id.doc_id);
            doc.setText(all_entity_history.get(position).title);

            //set onClick Event
            RelativeLayout item_bar = (RelativeLayout) convertView.findViewById(R.id.item_bar_layout);
            item_bar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), History_entity_detail.class);
                    intent.putExtra("CHOOSE_ID", position); // int
                    startActivity(intent);
                }
            });
            return convertView;
        }

    }
}
