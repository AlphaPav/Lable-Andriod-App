package com.example.alphapav.lableapplication.util;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;

public class Entity {
    // title and content
    public String title;
    public String content;

    //document position
    public int sent_id;
    public String doc_id;

    // labels
    public ArrayList<EntityTag> entities = new ArrayList<EntityTag>();

    public static Entity getEntityFromServer(SharedHelper sh)throws Exception{
        // url
        String url = "http://10.15.82.223:9090/app_get_data/app_get_entity";

        //map
        HashMap<String,String> map = new HashMap<>();
        map.put("token", sh.readToken());

        //get data
        String res = GetData.getFormbodyPostData(url, map);

        // error
        if(res == null){
            throw new Exception("network error");
        }
        else{
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(res).getAsJsonObject();
            if(jsonObject.has("msg")) {
                throw new Exception(jsonObject.get("msg").getAsString());
            }
        }
        //trans
        Gson gson = new Gson();
        Entity entity = gson.fromJson(res, Entity.class);

        //set value
        entity.entities = new ArrayList<EntityTag>();

        return entity;
    }

    public String upload2Server(SharedHelper sh){
        // url
        String url = "http://10.15.82.223:9090/app_get_data/app_upload_entity";

        //map
        HashMap<String,String> map = new HashMap<>();
        map.put("token", sh.readToken());
        map.put("entities", new Gson().toJson(this));
        return GetData.getFormbodyPostData(url, map);
    }

    public void addEntityTag(EntityTag entityTag){
        entities.add(entityTag);
    }

    public void removeEntityTag(EntityTag entityTag){
        entities.remove(entityTag);
    }

    public boolean isEqual(Entity entity){
        if(entity == null) {
            return false;
        }
        return (doc_id.equals(entity.doc_id) && sent_id == entity.sent_id);
    }

    static EntityHistory entityHistory = null;
    public static EntityHistory getEntityHistory(String userName, Application app){
        // user name is null
        if(userName == null){
            return null;
        }

        // judge
        if(entityHistory != null && entityHistory.equalsUser(userName)){
            return entityHistory;
        }
        else{
            entityHistory = EntityHistory.getNewEntityHistory(userName, app);
        }
        return entityHistory;
        // if null, means I/O error
    }
}

