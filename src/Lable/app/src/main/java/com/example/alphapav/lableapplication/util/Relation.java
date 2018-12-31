package com.example.alphapav.lableapplication.util;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class Relation {

    public String doc_id;
    public int sent_id;

    public String title;
    public String sent_ctx;

    public  ArrayList<RelationTag> triples = new ArrayList<RelationTag>();

    public static Relation getRelationFromServer(SharedHelper sh) throws Exception{

        // a new Relation
        Relation relation = new Relation();

        // to get data
        String url = "http://10.15.82.223:9090/app_get_data/app_get_triple";

        //map
        HashMap<String,String> map = new HashMap<>();
        map.put("token", sh.readToken());
        // the relation to return
        String relationStr = GetData.getFormbodyPostData(url, map);

        if(relationStr == null){
            throw new Exception("network error");
        }
        else{
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(relationStr).getAsJsonObject();
            if(jsonObject.has("msg")) {
                throw new Exception(jsonObject.get("msg").getAsString());
            }
        }

        // trans to Relation
        Gson gson = new Gson();
        relation = gson.fromJson(relationStr, Relation.class);
        for(int i = 0; i < relation.triples.size(); i++){
            relation.triples.get(i).status = 1;
        }
        return relation;
    }

    public String upload2Server(SharedHelper sh){
        String url = "http://10.15.82.223:9090/app_get_data/app_upload_triple";

        HashMap<String,String> map = new HashMap<>();
        map.put("token", sh.readToken());
        map.put("triples", new Gson().toJson(this));
        return GetData.getFormbodyPostData(url, map);
    }

    public boolean isEqual(Relation relation){
        if(relation == null)    return  false;
        return (doc_id.equals(relation.doc_id) && sent_id == relation.sent_id);
    }

    public void addRelationTag(RelationTag relationTag){
        triples.add(relationTag);
    }

    public void removeRelationTag(RelationTag relationTag){
        relationTag.status = -1;
    }

    static RelationHistory relationHistory = null;
    public static RelationHistory getRelationHistory(String userName, Application app){
        System.out.println(userName);
        // user name is null
        if(userName == null){
            return null;
        }

        // judge
        if(relationHistory != null && relationHistory.equalsUser(userName)){
            return relationHistory;
        }
        else{
            relationHistory = RelationHistory.getNewRelationHistory(userName, app);
        }
        return relationHistory;
        // if null, means I/O error
    }

}

