package com.example.textbook;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    //private Integer num;
    private NoteDatabase dbHelper;
    //private PlanDatabase planDatabase;
    private Context context=this;
    private ArrayAdapter arrayAdapter;
    FloatingActionButton btn;//左下角的新增框
   // final String TAG="test";//测试用
    private TextView tv;
    private ListView lv;//笔记列表窗
    private NoteAdapter adapter;
    private List<Note> noteList=new ArrayList<>();
    private Toolbar myToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private AnimationDrawable mAnimationDrawable;
    private int returnMode=0;//是否是修改原有笔记，是则为1
    private ListView lvLeftMenu;
    private PopupWindow popupWindow;
    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn= findViewById(R.id.createbutten1);
        tv=findViewById(R.id.tv);
        lv=findViewById(R.id.lv);
        myToolbar=(Toolbar)findViewById(R.id.myToolbar);

        adapter = new NoteAdapter(getApplicationContext(),noteList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //showPopupWindow();
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, myToolbar, R.string.menu_choice1, R.string.menu_choice2) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mAnimationDrawable.stop();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mAnimationDrawable.start();
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getId()) {
                    case R.id.lv:
                        System.out.println("已删除");
                        AlertDialog.Builder Alert=new AlertDialog.Builder(MainActivity.this);
                        Alert.setTitle("是否删除");
                        Alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Note curNote=(Note) parent.getItemAtPosition(position);
                                long deleteId=curNote.getId();
                                dbHelper = new NoteDatabase(context);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                db.execSQL("delete from notes where _id = " + deleteId );
                                db.execSQL("update sqlite_sequence set seq=0 where name='notes'");
                                refreshListView();
                            }
                        });
                        Alert.setNegativeButton("取消",null);
                        Alert.create().show();
                }
                refreshListView();
                return true;
            }
        });
        refreshListView();
        btn.setOnClickListener(v -> {
            final String[] arr={"文字","绘图"};   //常量定义数组

            AlertDialog.Builder noteType=new AlertDialog.Builder(MainActivity.this);
            noteType.setTitle("笔记类型");
            noteType.setSingleChoiceItems(arr,-1,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which==0){
                        Intent intent=new Intent(MainActivity.this,EditActivity.class);
                        intent.putExtra("openMode", 4);
                        startActivityForResult(intent,0);
                        dialog.dismiss();
                    }

                }

            });
            noteType.create().show();

            //noteType.create().show();
            //Intent intent=new Intent(MainActivity.this,EditActivity.class);
            //intent.putExtra("openMode", 4);
            //startActivityForResult(intent,0);
        });

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);//获取笔记返回的数据
        String content=data.getStringExtra("content");
        String create_time=data.getStringExtra("create_time");
        String update_time=data.getStringExtra("update_time");

        int returnMode=data.getExtras().getInt("returnMode",-1);
        long note_Id = data.getLongExtra("id", -1);


        Note note=new Note(content,create_time,update_time,"1");
        CRUD op=new CRUD(context);
        if (returnMode==1)  //如果是编辑已有笔记，则returnMode为1，使用update方法
        {
            note.setId(note_Id);
            op.open();
            op.updateNote(note);
        }
        else if(returnMode==-1){}
        else { //新增笔记使用add方法

            op.open();
            op.addNote(note);
        }
        op.close();
        refreshListView();
    }

    public void refreshListView(){
        //tv.setText(" ");
        CRUD op=new CRUD(context);
        op.open();
        int num= op.getAllNotes().size();
        if (noteList.size()>0) noteList.clear();
        noteList.addAll(op.getAllNotes());
        if (num>0) {tv.setText(" ");}else {tv.setText("点击按键创建新笔记");}
        op.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        onMenuOpened(1,menu);

       MenuItem mSearch=menu.findItem(R.id.action_search);
       SearchView mSearchView=(SearchView) mSearch.getActionView();
       mSearchView.setQueryHint("请输入内容……");
       mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               adapter.getFilter().filter(newText);
               return false;
           }
       });

        return super.onCreateOptionsMenu(menu);

    }




    /* 利用反射机制调用MenuBuilder的setOptionalIconsVisible方法设置mOptionalIconsVisible为true，给菜单设置图标时才可见 
     * 让菜单同时显示图标和文字*/
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv:
                Note curNote = (Note) parent.getItemAtPosition(position);
                Intent intent=new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("content", curNote.getContent());
                intent.putExtra("id", curNote.getId());
                intent.putExtra("update_time", curNote.getUpdate_time());
                intent.putExtra("create_time", curNote.getCreate_time());
                intent.putExtra("openMode", 3);
                startActivityForResult(intent, 1);
        }
    }


    public void onItemLongClick(AdapterView<?> parent,View view,int position,long id){
        switch (parent.getId()) {
            case R.id.lv:
                AlertDialog.Builder Alert=new AlertDialog.Builder(this);
                Alert.setTitle("是否删除");
                //Note curNote=(Note) parent.getItemAtPosition(position);
                Alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Note curNote=(Note) parent.getItemAtPosition(position);
                        long deleteId=curNote.getId();
                        CRUD op=new CRUD(context);
                        op.deleteNote(deleteId);
                    }
                });


        }

    }

    public void showPopupWindow(){
        View view = LayoutInflater.from(this).inflate(R.layout.layout_simple_popupwindow,null);
        ListView listView = view.findViewById(R.id.listview);
        final String[] datas = {"tag1", "tag2", "tag3", "tag4", "tag5", "tag6", "tag7"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, datas[position], Toast.LENGTH_SHORT).show();
                dismissPopupWindow();
            }
        });


        popupWindow = new PopupWindow(view, 600,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setFocusable(true);
        //popupWindow.setWidth(600);
        popupWindow.setAnimationStyle(R.style.left_animation);
        popupWindow.showAtLocation(view, Gravity.TOP,-600,0);


    }
    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }


}