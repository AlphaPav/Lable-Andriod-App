package com.example.alphapav.lableapplication;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alphapav.lableapplication.util.Relation;
import com.example.alphapav.lableapplication.util.RelationHistory;
import com.example.alphapav.lableapplication.util.SharedHelper;

import co.lujun.androidtagview.ColorFactory;
import co.lujun.androidtagview.TagContainerLayout;

public class History_relation_detail extends AppCompatActivity {

    private ListView listView;
    Relation chosendRelation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_relation_detail);
        int choosed = getIntent().getIntExtra("CHOOSE_ID", 0);
        // get relation
        SharedHelper sharedHelper = new SharedHelper(getApplicationContext());
        RelationHistory relationHistory = Relation.getRelationHistory(sharedHelper.readUser().get("username"), getApplication());
        chosendRelation = relationHistory.all_history_relation.get(choosed);

        TextView title = (TextView)findViewById(R.id.title);
        title.setText(chosendRelation.title);

        TextView content = (TextView)findViewById(R.id.content);
        content.setText(chosendRelation.sent_ctx);

        MyAdapter myAdapter = new MyAdapter(this);
        listView = (ListView) findViewById(R.id.relation_list);
        listView.setAdapter(myAdapter);
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public MyAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return chosendRelation.triples.size();
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
            convertView = layoutInflater.inflate(R.layout.relation_one, null);

            TagContainerLayout a_relation = (TagContainerLayout) convertView.findViewById(R.id.one_relation);
            if(chosendRelation.triples.get(position).status == -1){
                a_relation.setBorderColor(Color.rgb(255,255,255));
                a_relation.setTheme(ColorFactory.SHARP727272);
                a_relation.setBackgroundColor(Color.rgb(230,230,230));
            }

            a_relation.addTag(chosendRelation.triples.get(position).left_entity);
            if(chosendRelation.triples.get(position).relation_id == 0){
                a_relation.addTag("任职");
            }
            else{
                a_relation.addTag("亲属");
            }

            a_relation.addTag(chosendRelation.triples.get(position).right_entity);

            return convertView;
        }
    }
}
