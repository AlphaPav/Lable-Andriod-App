package com.example.alphapav.lableapplication.util;

import java.util.Random;

public class RelationTag {
    public String id;
    public int left_e_start;
    public int left_e_end;
    public int right_e_start;
    public int right_e_end;
    public int relation_start;
    public int relation_end;
    public String left_entity;
    public String right_entity;
    public int relation_id;
    public int status;

    public static int OFFICE_RELATION=0;
    public static int KINSFOLK_RELATION=1;

    public RelationTag(){}
    public RelationTag(int _left_e_start,int _left_e_end, int _right_e_start, int _right_e_end,
                       int _relation_start, int _relation_end, String _left_entity, String _right_entity,
                       int _relation_id, int _status)
    {
        id=randomID();
        left_e_start= _left_e_start;
        left_e_end= _left_e_end;
        right_e_start= _right_e_start;
        right_e_end= _right_e_end;
        relation_start=_relation_start;
        relation_end= _relation_end;
        left_entity=_left_entity;
        right_entity= _right_entity;
        relation_id= _relation_id;
        status=_status;
    }
    public void setRelationTagStatus(int _status)
    {
        status= _status;
    }
    public String randomID()
    {
        String sources = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        Random rand = new Random();
        StringBuffer flag = new StringBuffer();
        for (int j = 0; j < 20; j++)
        {
            flag.append(sources.charAt(rand.nextInt(sources.length())));
        }
        System.out.println(flag.toString());
        return flag.toString();

    }


}
