package com.example.textbook;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class NoteDatabase extends SQLiteOpenHelper {

    public static final String TABLE_NAME="notes";
    public static final String CONTENT="content";
    public static final String ID="_id";
    public static final String CREATE_TIME="create_time";
    public static final String UPDATE_TIME="update_time";
    public static final String MODE="mode";

    public NoteDatabase(Context context){
        super(context,TABLE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+TABLE_NAME
            +"("
                + ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CONTENT +" TEXT,"
                + CREATE_TIME +" TEXT,"
                + UPDATE_TIME +" TEXT NOT NULL,"
                + MODE +" TEXT NOT NULL"+")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
