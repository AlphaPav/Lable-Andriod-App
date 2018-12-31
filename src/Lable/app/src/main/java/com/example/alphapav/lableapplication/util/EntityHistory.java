package com.example.alphapav.lableapplication.util;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class EntityHistory{
    // userName
    private String userName = "";
    private static final String  prefix = "entity_";

    // all history Entity
    public ArrayList<Entity> all_history_entity = new ArrayList<Entity>();

    EntityHistory(String userName){
        this.userName = userName;
    }

    public boolean equalsUser(String userName){
        return this.userName.equals(userName);
    }

    static EntityHistory getNewEntityHistory(String userName, Application app){
        // new an entity history
        EntityHistory entityHistory = new EntityHistory(userName);
        try {
            // read
            FileInputStream fileOutputStream = app.openFileInput(prefix+userName);
            byte[] buffer = new byte[fileOutputStream.available()];
            fileOutputStream.read(buffer);
            fileOutputStream.close();
            // get string
            String jsonStr = new String(buffer, "utf-8");
            entityHistory.all_history_entity = new Gson().fromJson(jsonStr, new TypeToken<ArrayList<Entity>>(){}.getType());
            return  entityHistory;
        }catch (FileNotFoundException e){
            entityHistory.all_history_entity = new ArrayList<Entity>();
            return entityHistory;
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        return null;
    }

    public void save2Local(Application app) throws Exception{
        // new an entity history
        String fcontent = new Gson().toJson(all_history_entity);
        // write
        FileOutputStream fileOutputStream = app.openFileOutput(prefix+userName, Context.MODE_PRIVATE);
        fileOutputStream.write(fcontent.getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void updateEntity(Entity entity){
        // travel
        Entity find_entity = null;
        for(int i = 0; i < all_history_entity.size(); i++){
            Entity old_entity = all_history_entity.get(i);
            if(old_entity.isEqual(entity)){
                find_entity = old_entity;
                break;
            }
        }
        // if find
        if(find_entity != null){
            all_history_entity.remove(find_entity);
        }
        // add new
        all_history_entity.add(entity);
    }

}
