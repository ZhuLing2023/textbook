package com.example.textbook;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.view.KeyEvent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity{

    EditText et;
    //private String content;
    //private String time;

    private String old_content="";
    private String old_create_time="";
    private String old_update_time="";
    private String old_Tag="1";
    private long id=0;
    private int openMode=0;
    private String Tag="1";
    private int openMode1=0;
    private Toolbar myToolbar;
    Intent intent=new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        et=findViewById(R.id.et);
        myToolbar = findViewById(R.id.editToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //设置toolbar取代actionbar


        Intent getIntent=getIntent();
        int openMode=getIntent.getIntExtra("openMode",0);


        if(openMode==3){
            openMode1=1;
            //id=getIntent.getExtras().getLong("id", 0);
            id=getIntent.getLongExtra("id",0);
            old_content=getIntent.getStringExtra("content");
            old_create_time=getIntent.getStringExtra("create_time");
            old_update_time=getIntent.getStringExtra("update_time");
            old_Tag=getIntent.getStringExtra("tag");
            et.setText(old_content);
            et.setSelection(old_content.length());
        }

    }

    //点击返回键，并且保存记录的数据
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //返回笔记的数据
                sendMessageAuto();
                setResult(RESULT_OK,intent);
                onPause();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_HOME){
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_BACK){
            sendMessageAuto();
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    public void sendMessageAuto(){

        if (openMode1==1) {
            intent.putExtra("returnMode",1);//1代表是修改原有笔记，-1代表不新增，其他代表新增
            intent.putExtra("id",id);
            intent.putExtra(NoteDatabase.CONTENT, et.getText().toString());
            intent.putExtra(NoteDatabase.CREATE_TIME, old_create_time);
            intent.putExtra(NoteDatabase.UPDATE_TIME, dateToStr());
            intent.putExtra(NoteDatabase.MODE,Tag);
        }
        else {
            if((et.getText().toString().length() == 0)){
                intent.putExtra("returnMode",-1);
            }
            else {
                intent.putExtra("returnMode",2);
                intent.putExtra(NoteDatabase.CONTENT, et.getText().toString());
                intent.putExtra(NoteDatabase.CREATE_TIME, dateToStr());
                intent.putExtra(NoteDatabase.UPDATE_TIME, dateToStr());
                intent.putExtra(NoteDatabase.MODE, Tag);
            }
        }

    }


    public String dateToStr(){
        Date date=new Date();

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM-dd HH:mm");
        return simpleDateFormat.format(date);
    }
    //public String oldDateToStr(){
     //   Date date=new Date();
      //  if((old_create_time.substring(0,3))!=date.getYear())
      //  {
     //       SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
     //   }
   // }
}
