package com.example.alphapav.lableapplication.fragment;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphapav.lableapplication.LoginActivity;
import com.example.alphapav.lableapplication.R;
import com.example.alphapav.lableapplication.base.BaseFragment;
import com.example.alphapav.lableapplication.util.Entity;
import com.example.alphapav.lableapplication.util.GetData;
import com.example.alphapav.lableapplication.util.Relation;
import com.example.alphapav.lableapplication.util.RelationHistory;
import com.example.alphapav.lableapplication.util.RelationTag;
import com.example.alphapav.lableapplication.util.SharedHelper;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

import static android.support.constraint.ConstraintLayout.LayoutParams.HORIZONTAL;
import static android.view.Gravity.CENTER;

public class LablerelationFragment extends BaseFragment {

    private Button btn_next;
    private Button btn_ok;
    private Button btnAddTag;
    private Button btnAddRelation;
    private EditText edit_text;
    private TextView text_title;
    private TextView text_sent_ctx;
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int MP = ViewGroup.LayoutParams.MATCH_PARENT;

    View view;
    private SharedHelper sh;
    private Context mContext;
    private TableLayout tableLayout;
    Relation relation;
    List<String> listAll = new ArrayList<String>();
    List<String> listSelect = new ArrayList<String>();
    private TagContainerLayout mTagContainerLayoutAll, mTagContainerLayoutSelect;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lablerelation, container, false);
        mContext= Objects.requireNonNull(getActivity()).getApplicationContext();
        sh= new SharedHelper(mContext);
        bindViews();
        InitPost();
        return view ;
    }
    public static  LablerelationFragment newInstance() {
        return new  LablerelationFragment();
    }


    public void addTableRow(int index)
    {
        if (index<0)
        {
            System.out.println("error add table row: index <0");
            return;
        }
        TableRow new_tablerow= new TableRow(view.getContext());
        new_tablerow.setPadding(0,3,0,3);

        TextView text_left_entity = (TextView)new TextView(view.getContext());
        TextView text_right_entity = (TextView)new TextView(view.getContext());
        TextView text_relation = (TextView)new TextView(view.getContext());
        RadioButton btn_true= (RadioButton)new RadioButton(view.getContext());
        RadioButton btn_false= (RadioButton)new RadioButton(view.getContext());
        RadioGroup radioGroup=(RadioGroup) new RadioGroup(view.getContext());
        //LinearLayout tempLiearLayout= (LinearLayout) new LinearLayout(view.getContext()) ;
        text_left_entity.setBackgroundResource(R.drawable.table_shape);
        text_left_entity.setText(relation.triples.get(index).left_entity);
        text_left_entity.setGravity(CENTER);
        new_tablerow.addView(text_left_entity, 0);

        TextView text_temp1 = (TextView)new TextView(view.getContext());
        text_temp1.setWidth(3);
        new_tablerow.addView(text_temp1, 1);

        text_right_entity.setBackgroundResource(R.drawable.table_shape);
        text_right_entity.setText(relation.triples.get(index).right_entity);
        text_right_entity.setGravity(CENTER);
        new_tablerow.addView(text_right_entity,2);

        TextView text_temp2= (TextView)new TextView(view.getContext());
        text_temp2.setWidth(3);
        new_tablerow.addView(text_temp2, 3);

        text_relation.setBackgroundResource(R.drawable.table_shape_relation);
        text_relation.setGravity(CENTER);
        String relation_type="";
        if(relation.triples.get(index).relation_id==RelationTag.OFFICE_RELATION)
        {
            relation_type="任职";
        }else if(relation.triples.get(index).relation_id==RelationTag.KINSFOLK_RELATION)
        {
            relation_type="亲属";
        }
        text_relation.setText(relation_type);
        text_relation.setTextColor(Color.WHITE);
        new_tablerow.addView(text_relation,4);
        TextView text_temp3= (TextView)new TextView(view.getContext());
        text_temp2.setWidth(3);
        new_tablerow.addView(text_temp3, 5);

        radioGroup.setOrientation(HORIZONTAL);
        btn_true.setText("true");
        radioGroup.addView(btn_true);
        btn_false.setText("false");
        try {

            int status= relation.triples.get(index).status;
            if(status==1)
            {
                btn_true.setChecked(true);
            }else if(status==-1)
            {
                btn_false.setChecked(true);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        radioGroup.addView(btn_false);
        radioGroup.setOnCheckedChangeListener(new myListener(index));//添加监听事件
        new_tablerow.addView(radioGroup,6,new TableRow.LayoutParams(WC, WC));
        tableLayout.addView(new_tablerow,new TableLayout.LayoutParams(WC, WC));

    }
    public void buildTable()
    {
        if(relation.triples.size()>0)
        {
            tableLayout.removeAllViewsInLayout();
            for(int i=0; i< relation.triples.size();i++)
            {
                addTableRow(i);
            }
        }

    }
    private class myListener implements RadioGroup.OnCheckedChangeListener{

        int m=0;
        public myListener(int i)
        {
            super();
            this.m=i;
        }
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton tempbutton= (RadioButton) view.findViewById(checkedId);
            System.out.println(checkedId);
            System.out.println(tempbutton.getText().toString());
            if(tempbutton.getText().toString().equals("true"))
            {
                relation.triples.get(m).setRelationTagStatus(1);
                System.out.println(relation.triples.get(m).status);
            }else if(tempbutton.getText().toString().equals("false"))
            {
                relation.triples.get(m).setRelationTagStatus(-1);
                System.out.println(relation.triples.get(m).status);
            }else {
                System.out.println("tempbutton text error");
            }
        }
    }

    //用于刷新界面和跳转页面
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x001:
                    text_title.setText(relation.title);
                    text_sent_ctx.setText(relation.sent_ctx);
                    listAll.clear();
                    listSelect.clear();
                    mTagContainerLayoutAll.removeAllTags();
                    mTagContainerLayoutSelect.removeAllTags();
                    edit_text.setText("");
                    buildTable();
                    Toast.makeText(getActivity(), "成功获取Relation", Toast.LENGTH_SHORT).show();

                    sh.recordLastType("relation");
                    sh.saveRelation(relation);
                    break;
                case 0x00A:
                    text_title.setText(relation.title);
                    text_sent_ctx.setText(relation.sent_ctx);
                    listAll.clear();
                    listSelect.clear();
                    edit_text.setText("");
                    buildTable();
                    Toast.makeText(getActivity(), "成功从本地恢复上次工作", Toast.LENGTH_SHORT).show();
                    break;
                case 0x00B:
                    Toast.makeText(getActivity(), "从本地恢复上次工作失败", Toast.LENGTH_SHORT).show();
                    sh.recordLastType("");
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
                    text_sent_ctx.setText(""); //把上面content的内容设为空
                    break;
                case 0x005:
                    Toast.makeText(getActivity(),"成功上传实体标注结果", Toast.LENGTH_SHORT).show();
                    sh.recordLastType("");
                    sh.saveRelation(relation);
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
                case 0x011:
                    addTableRow(relation.triples.size()-1);
                    listSelect.clear();
                    mTagContainerLayoutSelect.removeAllTags();
                    edit_text.setText("");
                    Toast.makeText(getActivity(),"成功添加一组triple", Toast.LENGTH_SHORT).show();
                    break;
                case 0x012:
                    Toast.makeText(getActivity(),"未选择entity", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getActivity(), "Invalid msg.what", Toast.LENGTH_SHORT).show();
                    break;

            }
        };
    };


    private void bindViews()
    {
        tableLayout= (TableLayout) view.findViewById(R.id.labelrelation_table);
        text_title= (TextView) view.findViewById(R.id.labelrelation_text_title);
        text_sent_ctx=(TextView)view.findViewById(R.id.labelrelation_text_sent_ctx);
        text_sent_ctx.setTextSize(TypedValue.COMPLEX_UNIT_SP,sh.readFontsize());
        btn_ok= (Button)view.findViewById(R.id.labelrelation_btn_ok);
        btn_next= (Button)view.findViewById(R.id.labelrelation_btn_next);
        mTagContainerLayoutAll = (TagContainerLayout) view.findViewById(R.id.relation_tagcontainerLayoutAll);
        mTagContainerLayoutSelect = (TagContainerLayout) view.findViewById(R.id.relation_tagcontainerLayoutSelect);
        mTagContainerLayoutAll.setTags(listAll);
        mTagContainerLayoutSelect.setTag(listSelect);
        edit_text = (EditText) view.findViewById(R.id.relation_text_tag);
        btnAddTag = (Button) view.findViewById(R.id.relation_btn_add_tag);
        btnAddRelation=(Button) view.findViewById(R.id.relation_btn_add_relation);

        text_sent_ctx.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("LOGTAG","onLongClick");
                if(relation.sent_ctx!=null)
                {
                    new Thread() {
                        public void run() {
                            int msg_what=0x003;
                            //begin request
                            try {
                                String[] splitresult=null;
                                splitresult= GetData.WordSplit(relation.sent_ctx).clone();
                                if(splitresult!=null)
                                {
                                    List<String> templist= Arrays.asList(splitresult);
                                    listAll= new ArrayList<>(templist);
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
                if(listSelect.size()>=2) {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Warning")
                            .setMessage("You have already choose two tags. Please add a relation.")
                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                }
                else
                {
                    mTagContainerLayoutSelect.addTag(text);
                    listSelect.add(text);
                }
            }

            @Override
            public void onTagLongClick(int position, String text) {
            }

            @Override
            public void onTagCrossClick(int position) {
            }
        });


        mTagContainerLayoutSelect.setOnTagClickListener(new TagView.OnTagClickListener() {
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
                                if (position < mTagContainerLayoutSelect.getChildCount()) {
                                    mTagContainerLayoutSelect.removeTag(position);
                                    listSelect.remove(text);
                                    System.out.println(listSelect);
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
                if(listSelect.size()>=2) {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Warning")
                            .setMessage("You have already choose two tags. Please add a relation.")
                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                }
                else
                {
                    mTagContainerLayoutSelect.addTag(edit_text.getText().toString());
                    listSelect.add(edit_text.getText().toString());
                }
            }
        });

        btnAddRelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(relation==null)
                {
                    handler.sendEmptyMessage(0x007);
                    return;
                }
                if(listSelect.size()==0)
                {
                    handler.sendEmptyMessage(0x012);
                    return;
                }
                boolean flag=true;
                //System.out.println(entity.content);
                final String left_entity= listSelect.get(0);
                final String right_entity= listSelect.get(1);
                final int left_start= relation.sent_ctx.indexOf(left_entity);
                final int right_start= relation.sent_ctx.indexOf(right_entity);
                int left_end=-1;
                int right_end=-1;
                if(left_start>=0&& right_start>=0)
                {
                    left_end= left_start+ left_entity.length();
                    right_end= right_start+right_entity.length();
                }else
                {
                    flag=false;
                }

                final boolean finalFlag = flag;
                final int finalLeft_end = left_end;
                final int finalRight_end = right_end;
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("click")
                        .setMessage("please choose the relation type.")
                        .setPositiveButton("任职关系", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(finalFlag)
                                {
                                    RelationTag relationTag= new RelationTag(left_start, finalLeft_end,right_start, finalRight_end,
                                            -1,-1,left_entity,right_entity,RelationTag.OFFICE_RELATION,1);
                                    relation.addRelationTag(relationTag);
                                    //在handle内部调用addTableRow
                                    handler.sendEmptyMessage(0x011);

                                    sh.recordLastType("relation");
                                    sh.saveRelation(relation);

                                }else
                                {
                                    handler.sendEmptyMessage(0x006);//没有此tag
                                }
                            }
                        })
                        .setNegativeButton("亲属关系", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(finalFlag)
                                {
                                    RelationTag relationTag= new RelationTag(left_start, finalLeft_end,right_start, finalRight_end,
                                            -1,-1,left_entity,right_entity,RelationTag.KINSFOLK_RELATION,1);
                                    relation.addRelationTag(relationTag);
                                    //在handle内部调用addTableRow
                                    handler.sendEmptyMessage(0x011);

                                    sh.recordLastType("relation");
                                    sh.saveRelation(relation);

                                }else
                                {
                                    handler.sendEmptyMessage(0x006);//没有此tag
                                }
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();

            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LOGTAG","onclick get relation");
                new Thread() {
                    public void run() {
                        try {
                            relation = Relation.getRelationFromServer(sh);
                            handler.sendEmptyMessage(0x001);
                        }catch (Exception e){
                            String msg = e.getMessage();
                            if(msg.equals("network error")){
                                handler.sendEmptyMessage(0x010);
                            }else if(msg.equals("尚未登陆")){
                                handler.sendEmptyMessage(0x002);
                            }
                            else{
                                handler.sendEmptyMessage(0x003);
                            }
                        }
                    }
                }.start();

            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelationHistory relationHistory = Relation.getRelationHistory(sh.readUser().get("username"), getActivity().getApplication());
                if(relation != null){
                    relationHistory.updateRelation(relation);
                    try {
                        relationHistory.save2Local(getActivity().getApplication());
                    }catch (Exception e){
                        System.out.println("Save to history failed!");
                    }
                }
                new Thread() {
                    public void run() {
                        int msg_what=0x003;
                        if(relation!=null)
                        {
                            //begin request
                            try {
                                JSONObject jsStr= JSONObject.fromObject(relation.upload2Server(sh));
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

    }

    private void InitPost()
    {
        if(sh.getLastType().equals("relation")){
            relation = sh.getLastRelation();
            if(relation == null){
                handler.sendEmptyMessage(0x00B);
            }
            else{
                handler.sendEmptyMessage(0x00A);
            }
        }
        else{
            new Thread() {
                public void run() {
                    try {
                        relation = Relation.getRelationFromServer(sh);
                        handler.sendEmptyMessage(0x001);
                    }catch (Exception e){
                        String msg = e.getMessage();
                        if(msg.equals("network error")){
                            handler.sendEmptyMessage(0x010);
                        }else if(msg.equals("尚未登陆")){
                            handler.sendEmptyMessage(0x002);
                        }
                        else{
                            handler.sendEmptyMessage(0x003);
                        }
                    }
                }
            }.start();
        }
    }
}
