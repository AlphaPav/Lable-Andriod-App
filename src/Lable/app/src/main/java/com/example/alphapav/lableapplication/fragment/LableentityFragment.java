package com.example.alphapav.lableapplication.fragment;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphapav.lableapplication.LoginActivity;
import com.example.alphapav.lableapplication.MainActivity;
import com.example.alphapav.lableapplication.R;
import com.example.alphapav.lableapplication.base.BaseFragment;
import com.example.alphapav.lableapplication.util.Entity;
import com.example.alphapav.lableapplication.util.EntityTag;
import com.example.alphapav.lableapplication.util.GetData;
import com.example.alphapav.lableapplication.util.SharedHelper;
import net.sf.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;


public class LableentityFragment extends BaseFragment {
    private Button btn_next;
    private Button btn_ok;
    private Button btnAddTag;
    private EditText edit_text;
    private TextView text_title;
    private TextView text_content;
    View view;
    private SharedHelper sh;
    private Context mContext;
    Entity entity;

    List<String> listAll = new ArrayList<String>();
    List<String> listPerson = new ArrayList<String>();
    List<String> listTitle = new ArrayList<String>();
    private TagContainerLayout mTagContainerLayoutAll, mTagContainerLayoutPerson,mTagContainerLayoutTitle;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lableentity, container, false);
        mContext= Objects.requireNonNull(getActivity()).getApplicationContext();
        sh= new SharedHelper(mContext);
        bindViews();
        InitPost();
        return view ;
    }

    public static LableentityFragment newInstance() {
        return new LableentityFragment();
    }
    //用于刷新界面和跳转页面
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x001:
                    text_title.setText(entity.title);
                    text_content.setText(entity.content);
                    listAll.clear();
                    listPerson.clear();
                    listTitle.clear();
                    mTagContainerLayoutAll.removeAllTags();
                    mTagContainerLayoutPerson.removeAllTags();
                    mTagContainerLayoutTitle.removeAllTags();
                    edit_text.setText("");

                    sh.recordLastType("entity");
                    sh.saveEntityPersonList(listPerson);
                    sh.saveEntityTitleList(listTitle);
                    sh.saveEntity(entity);
                    Toast.makeText(getActivity(), "成功获取Entity", Toast.LENGTH_SHORT).show();
                    break;
                case 0x00A:
                    text_title.setText(entity.title);
                    text_content.setText(entity.content);
                    listAll.clear();
                    edit_text.setText("");
                    sh.recordLastType("entity");
                    Toast.makeText(getActivity(), "成功从本地恢复上次工作", Toast.LENGTH_SHORT).show();
                    break;
                case 0x00B:
                    sh.recordLastType("");
                    Toast.makeText(getActivity(), "从本地恢复上次工作失败", Toast.LENGTH_SHORT).show();
                    break;
                case 0x002:
                    Toast.makeText(getActivity(), "尚未登陆", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(mContext, LoginActivity.class));
                    break;
                case 0x003:
                    Toast.makeText(getActivity(),"Wrong response format", Toast.LENGTH_SHORT).show();
                    break;
                case 0x004:
                    Toast.makeText(getActivity(),"成功文本大爆炸", Toast.LENGTH_SHORT).show();
                    for(int j =0; j<listAll.size();j++)
                    {
                        mTagContainerLayoutAll.addTag(listAll.get(j));
                    }
                    text_content.setText(""); //把上面content的内容设为空
                    break;
                case 0x005:
                    edit_text.setText("");
                    Toast.makeText(getActivity(),"成功上传实体标注结果", Toast.LENGTH_SHORT).show();
                    sh.recordLastType("");
                    break;
                case 0x006:
                    Toast.makeText(getActivity(),"文章中不存在这个Tag", Toast.LENGTH_SHORT).show();
                    break;
                case 0x007:
                    Toast.makeText(getActivity(),"未获取Entity", Toast.LENGTH_SHORT).show();
                    break;
                case 0x008:
                    Toast.makeText(getActivity(),"数据不得为空", Toast.LENGTH_SHORT).show();
                    break;
                case 0x009:
                    Toast.makeText(getActivity(),"数据非Json格式", Toast.LENGTH_SHORT).show();
                    break;
                case 0x010:
                    Toast.makeText(getActivity(),"Network Error", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getActivity(), "Invalid msg.what", Toast.LENGTH_SHORT).show();
                    break;

            }
        };
    };


    private void bindViews()
    {
        text_title= (TextView) view.findViewById(R.id.labelentity_text_title);
        text_content=(TextView)view.findViewById(R.id.labelentity_text_content);
        text_content.setTextSize(TypedValue.COMPLEX_UNIT_SP,sh.readFontsize());
        btn_next= (Button)view.findViewById(R.id.labelentity_btn_next);
        btn_ok=(Button)view.findViewById(R.id.labelentity_btn_ok);
        mTagContainerLayoutAll = (TagContainerLayout) view.findViewById(R.id.entity_tagcontainerLayoutAll);
        mTagContainerLayoutPerson = (TagContainerLayout) view.findViewById(R.id.entity_tagcontainerLayoutPerson);
        mTagContainerLayoutTitle = (TagContainerLayout) view.findViewById(R.id.entity_tagcontainerLayoutTitle);
        mTagContainerLayoutAll.setTags(listAll);
        mTagContainerLayoutPerson.setTags(listPerson);
        mTagContainerLayoutTitle.setTag(listTitle);
        edit_text = (EditText) view.findViewById(R.id.entity_text_tag);
        btnAddTag = (Button) view.findViewById(R.id.entity_btn_add_tag);
        text_content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("LOGTAG","onLongClick");
                if(entity.content!=null)
                {
                    new Thread() {
                        public void run() {
                            int msg_what=0x003;
                            //begin request
                            try {
                                String[] splitresult=null;
                                splitresult= GetData.WordSplit(entity.content).clone();
                                //splitresult= GetData.WordJson2Array("").clone();
                                //System.out.print("\nGet response : " + splitresult);
                                if(splitresult!=null)
                                {
                                    List<String> templist= Arrays.asList(splitresult);
                                    listAll= new ArrayList<>(templist);
                                   // System.out.print("\nGet response : " + listAll);
                                    msg_what=0x004;
                                }
                            }catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(msg_what);
                        }
                    }.start();

                }

                return false;
            }
        });
        mTagContainerLayoutAll.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, final String text) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("click")
                        .setMessage("You will add a tag, please choose a type.")
                        .setPositiveButton("人名", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTagContainerLayoutPerson.addTag(text);
                                listPerson.add(text);
                                System.out.println(listPerson);
                                sh.recordLastType("entity");
                                sh.saveEntityPersonList(listPerson);
                                sh.saveEntityTitleList(listTitle);
                            }
                        })
                        .setNegativeButton("官衔", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTagContainerLayoutTitle.addTag(text);
                                listTitle.add(text);
                                System.out.println(listTitle);
                                sh.recordLastType("entity");
                                sh.saveEntityPersonList(listPerson);
                                sh.saveEntityTitleList(listTitle);
                            }
                        })
                        .create();
                dialog.show();
            }

            @Override
            public void onTagLongClick(int position, String text) {
            }

            @Override
            public void onTagCrossClick(int position) {
            }
        });

        // Set custom click listener
        mTagContainerLayoutPerson.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
            }

            @Override
            public void onTagLongClick(final int position, final String text) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("long click")
                        .setMessage("You will delete this tag!")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (position < mTagContainerLayoutPerson.getChildCount()) {
                                    mTagContainerLayoutPerson.removeTag(position);
                                    listPerson.remove(text);
                                    System.out.println(listPerson);
                                    sh.recordLastType("entity");
                                    sh.saveEntityPersonList(listPerson);
                                    sh.saveEntityTitleList(listTitle);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
        // Set custom click listener
        mTagContainerLayoutTitle.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
            }

            @Override
            public void onTagLongClick(final int position, final String text) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("long click")
                        .setMessage("You will delete this tag!")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (position < mTagContainerLayoutTitle.getChildCount()) {
                                    mTagContainerLayoutTitle.removeTag(position);
                                    listTitle.remove(text);
                                    System.out.println(listTitle);
                                    sh.recordLastType("entity");
                                    sh.saveEntityPersonList(listPerson);
                                    sh.saveEntityTitleList(listTitle);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });


        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("click")
                        .setMessage("You will add a tag, please choose a type.")
                        .setPositiveButton("人名", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTagContainerLayoutPerson.addTag(edit_text.getText().toString());
                                listPerson.add(edit_text.getText().toString());

                                sh.recordLastType("entity");
                                sh.saveEntityPersonList(listPerson);
                                sh.saveEntityTitleList(listTitle);
                            }
                        })
                        .setNegativeButton("官衔", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTagContainerLayoutTitle.addTag(edit_text.getText().toString());
                                listTitle.add(edit_text.getText().toString());

                                sh.recordLastType("entity");
                                sh.saveEntityPersonList(listPerson);
                                sh.saveEntityTitleList(listTitle);
                            }
                        })
                        .create();
                dialog.show();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LOGTAG","onclick upload");
                if(entity != null){
                    try {
                        Entity.getEntityHistory(sh.readUser().get("username"), getActivity().getApplication()).updateEntity(entity);
                        Entity.getEntityHistory(sh.readUser().get("username"), getActivity().getApplication()).save2Local(getActivity().getApplication());
                    }catch (Exception e){
                        System.out.println("Save to history failed!");
                    }
                }
                new Thread() {
                    public void run() {
                        int msg_what=0x003;
                        if(entity!=null)
                        {
                            //begin request
                            try {
                                boolean flag=true;
                                //System.out.println(entity.content);
                                for(int j = 0; j< listPerson.size();j++)
                                {
                                    String entity_name= listPerson.get(j);
                                    int start_pos= entity.content.indexOf(entity_name);
                                    if(start_pos>=0)
                                    {
                                        int end_pos= start_pos+ entity_name.length();
                                        EntityTag new_entity_tag= new EntityTag(entity_name, start_pos,end_pos, EntityTag.PERSON);
                                        entity.addEntityTag(new_entity_tag);
                                    }else
                                    {
                                        flag=false;
                                        break;
                                    }
                                }
                                if(flag)
                                {
                                    for(int j = 0; j< listTitle.size();j++)
                                    {
                                        String entity_name= listTitle.get(j);
                                        int start_pos= entity.content.indexOf(entity_name);
                                        if(start_pos>=0)
                                        {
                                            int end_pos= start_pos+ entity_name.length();
                                            EntityTag new_entity_tag= new EntityTag(entity_name, start_pos,end_pos, EntityTag.TITLE);
                                            entity.addEntityTag(new_entity_tag);
                                        }else
                                        {
                                            flag=false;
                                            break;
                                        }
                                    }
                                }
                                if(flag)
                                {
                                    JSONObject jsStr= JSONObject.fromObject(entity.upload2Server(sh));

                                    String res= jsStr.get("msg").toString();
                                    if(res.equals("尚未登陆"))
                                    {
                                        msg_what=0x002;
                                    }else if (res.equals("数据不得为空"))
                                    {
                                        msg_what=0x008;
                                    }else if (res.equals("数据非Json格式"))
                                    {
                                        msg_what=0x009;
                                    }else if (res.equals("上传成功"))
                                    {
                                        msg_what=0x005;
                                    }else {
                                        msg_what=0x003;//回复格式有误
                                    }
                                    System.out.println(res);
                                    System.out.println("flash test");
                                }else
                                {
                                    msg_what=0x006;//文章中不存在这个Tag
                                }

                            }catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                        }else
                        {
                            msg_what=0x007;  //不存在entity
                        }
                        handler.sendEmptyMessage(msg_what);
                    }
                }.start();

            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LOGTAG","onclick get entity");
                new Thread() {
                    public void run() {
                        //begin request
                        try {
                            entity = Entity.getEntityFromServer(sh);
                            handler.sendEmptyMessage(0x001);
                        }catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                            String msg = e.getMessage();
                            if(msg.equals("尚未登录"))
                            {
                                handler.sendEmptyMessage(0x002);
                            }else if(msg.equals("network error"))
                            {
                                handler.sendEmptyMessage(0x010);
                            }else{
                                handler.sendEmptyMessage(0x003);
                            }
                        }


                    }
                }.start();

            }
        });
    }

    private void InitPost()
    {

        if(sh.getLastType().equals("entity")){
            // from local
            entity = sh.getLastEntity();
            if(entity == null){
                handler.sendEmptyMessage(0x00B);
            }
            else {
                listPerson = sh.getLastEntityPersonList();
                listTitle = sh.getLastEntityTitleList();

                // add tag
                for(int i = 0; i < listPerson.size(); i++){
                    mTagContainerLayoutPerson.addTag(listPerson.get(i));
                }

                // add tag
                for(int i = 0; i < listTitle.size(); i++){
                    mTagContainerLayoutTitle.addTag(listTitle.get(i));
                }

                handler.sendEmptyMessage(0x00A);
            }
        }
        else{
            new Thread() {
                public void run() {
                    //begin request
                    try {
                        entity = Entity.getEntityFromServer(sh);
                        handler.sendEmptyMessage(0x001);
                    }catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        String msg = e.getMessage();
                        if(msg.equals("尚未登录"))
                        {
                            handler.sendEmptyMessage(0x002);
                        }else if(msg.equals("network error"))
                        {
                            handler.sendEmptyMessage(0x010);
                        }else{
                            handler.sendEmptyMessage(0x003);
                        }
                    }

                }
            }.start();
        }
    }

}
