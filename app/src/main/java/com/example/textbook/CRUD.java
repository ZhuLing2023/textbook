package com.example.textbook;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CRUD {
    SQLiteOpenHelper dbHandler;
    SQLiteDatabase db;
    SQLiteDatabase delete;

    private static final String[] columns={
            NoteDatabase.ID,
            NoteDatabase.CONTENT,
            NoteDatabase.CREATE_TIME,
            NoteDatabase.UPDATE_TIME,
            NoteDatabase.MODE
    };
    //新建一个数据库处理器
    public CRUD(Context context){
        dbHandler=new NoteDatabase(context);
    }
    //打开数据库处理器
    public void open(){
        db=dbHandler.getWritableDatabase();
    }
    //关闭数据库处理器
    public void close(){
        dbHandler.close();
    }
    //把note加入到数据库
    public Note addNote(Note note){
        ContentValues contentValues=new ContentValues();
        contentValues.put(NoteDatabase.CONTENT, note.getContent());
        contentValues.put(NoteDatabase.CREATE_TIME,note.getCreate_time());
        contentValues.put(NoteDatabase.UPDATE_TIME,note.getUpdate_time());
        contentValues.put(NoteDatabase.MODE,note.getTag());
        long insertId = db.insert(NoteDatabase.TABLE_NAME, null, contentValues);
        note.setId(insertId);
        return note;
    }
    public Note getNote(long id) {
        Cursor cursor = db.query(NoteDatabase.TABLE_NAME, columns, NoteDatabase.ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        Note e = new Note(cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4));
        return e;
    }

    @SuppressLint("Range")//这个注释是为了忽略getColumnIndex的警告，或者可以用getColumnIndexOrThrow()替代
    public List<Note> getAllNotes(){
        Cursor cursor=db.query(NoteDatabase.TABLE_NAME,columns,null,null,null,null,null);
        List<Note> notes=new ArrayList<>();
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                Note note=new Note();
                note.setId(cursor.getLong(cursor.getColumnIndexOrThrow(NoteDatabase.ID)));
                note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.CONTENT)));
                note.setCreate_time(cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.CREATE_TIME)));
                note.setUpdate_time(cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.UPDATE_TIME)));
                note.setTag(cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.MODE)));
                notes.add(note);
            }

        }
        return notes;
    }
    public int updateNote(Note note){
        ContentValues values=new ContentValues();
        values.put(NoteDatabase.CONTENT,note.getContent());
        values.put(NoteDatabase.CREATE_TIME,note.getCreate_time());
        values.put(NoteDatabase.UPDATE_TIME,note.getUpdate_time());
        values.put(NoteDatabase.MODE,note.getTag());
        return db.update(NoteDatabase.TABLE_NAME,values,
                NoteDatabase.ID+"=?",new String[]{String.valueOf(note.getId())});

    }

    public void removeNote(Note note){
        db.delete(NoteDatabase.TABLE_NAME,NoteDatabase.ID+"="+note.getId(),null);
    }
    public void deleteNote(long deleteId){
        delete.delete(NoteDatabase.TABLE_NAME,NoteDatabase.ID+"="+deleteId,null);
        //delete.execSQL("delete from user where _id = " + deleteId );
    }

}


