package com.example.textbook;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends BaseAdapter implements Filterable {
    private Context mContext;

    private List<Note> backList;
    private List<Note>  noteList;
    private MyFilter mFilter;

    public NoteAdapter(Context mContext,List<Note> noteList){
        this.mContext=mContext;
        this.noteList=noteList;
        backList=noteList;
    }

    @Override
    public int getCount() {return noteList.size();}

    @Override
    public Object getItem(int position){return noteList.get(position);}

    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //SharePreferences sharedPreferences= PreferenceManager.getDefaultShardPreferences(mContext);
        //mContext.setTheme((sharedPreferences.getBoolean("nightMode",false)?R.style.NightTheme:R.style.DayTheme));
        View v=View.inflate(mContext,R.layout.note_layout,null);
        TextView  tv_content =(TextView) v.findViewById(R.id.tv_content);
        TextView  tv_ctime=(TextView) v.findViewById(R.id.tv_ctime);
        TextView  tv_utime=(TextView) v.findViewById(R.id.tv_utime);

        String allText=noteList.get(position).getContent();
        tv_content.setText(allText);
        tv_ctime.setText("创建时间："+noteList.get(position).getCreate_time());
        tv_utime.setText("最后更新："+noteList.get(position).getUpdate_time());
        v.setTag(noteList.get(position).getId());
        return v;
    }

    class MyFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence  charSequence){
            FilterResults result=new FilterResults();
            List<Note> list;
            if(TextUtils.isEmpty(charSequence)){
                list=backList;
            } else {
                list=new ArrayList<>();
                for (Note note:backList){
                    if (note.getContent().contains(charSequence)){
                        list.add(note);
                    }
                }
            }
            result.values=list;//将得到的集合保存到FilterResults的value变量中
            result.count=list.size();//将集合的大小保存到FilterResults的count变量中
            return result;
        }
        //在publishResults方法中告诉适配器更新界面
        @Override
        protected void publishResults(CharSequence charSequence,FilterResults filterResults){
            noteList = (List<Note>) filterResults.values;
            if (filterResults.count>0){
                notifyDataSetChanged();//通知数据发生了改变
            }else {
                notifyDataSetInvalidated();//通知数据失效
            }
        }
    }
    @Override
    public Filter getFilter(){
        if (mFilter==null){
            mFilter=new MyFilter();
        }
        return mFilter;
    }

}
