package com.example.alphapav.lableapplication.util;

public class EntityTag {
    public String EntityName;
    public int Start;
    public int End;
    public String NerTag;

    public static String PERSON="PERSON";
    public static String TITLE="TITLE";

    public EntityTag(){}
    public EntityTag(String _EntityName,int _start,int _end, String _NerTag)
    {
        EntityName = _EntityName;
        Start= _start;
        End = _end;
        NerTag= _NerTag;
    }

}
