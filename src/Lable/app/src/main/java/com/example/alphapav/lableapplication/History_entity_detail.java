package com.example.alphapav.lableapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.alphapav.lableapplication.util.Entity;
import com.example.alphapav.lableapplication.util.EntityHistory;
import com.example.alphapav.lableapplication.util.EntityTag;
import com.example.alphapav.lableapplication.util.SharedHelper;

import java.util.ArrayList;

import co.lujun.androidtagview.TagContainerLayout;

public class History_entity_detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_relation_detail);

        int choosed = getIntent().getIntExtra("CHOOSE_ID", 0);

        SharedHelper sh;
        ArrayList<Entity> all_entity_history;
        sh= new SharedHelper(getApplicationContext());
        // get the data of relation history
        EntityHistory entityHistory = Entity.getEntityHistory(sh.readUser().get("username"), getApplication());
        all_entity_history = entityHistory.all_history_entity;
        Entity chosedEntity = all_entity_history.get(choosed);

        TextView title = (TextView)findViewById(R.id.title);
        title.setText(chosedEntity.title);

        TextView content = (TextView)findViewById(R.id.content);
        content.setText(chosedEntity.content);

        TagContainerLayout all_person = (TagContainerLayout) findViewById(R.id.all_person);
        TagContainerLayout all_title = (TagContainerLayout) findViewById(R.id.all_title);

        for(int i = 0; i < chosedEntity.entities.size(); i++){
            EntityTag entityTag = chosedEntity.entities.get(i);
            if(entityTag.NerTag.equals(EntityTag.PERSON)){
                all_person.addTag(entityTag.EntityName);
            }
            else{
                all_title.addTag(entityTag.EntityName);
            }
        }
    }

}
