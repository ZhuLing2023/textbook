package com.example.textbook;

public class Note {
    private long id;
    private String content;
    private String create_time;
    private String update_time;
    private String tag;

    public Note(){

    }
    public Note(String content,String c_time,String u_time,String tag){
        this.content=content;
        this.create_time=c_time;
        this.update_time=u_time;
        this.tag=tag;
    }
    //让取值更加规范化
    public long getId(){return id;}
    public String getContent(){return content;}
    public String getCreate_time(){return create_time;}
    public String getUpdate_time(){return update_time;}
    public String getTag(){return tag;}

    public void setId(long id){this.id=id;}
    public void setContent(String content){this.content=content;}
    public void setCreate_time(String create_time){this.create_time=create_time;}
    public void setUpdate_time(String update_time){this.update_time=update_time;}
    public void setTag(String tag){this.tag=tag;}

    @Override
    public String toString(){return content+"\n"+create_time.substring(5,16)+" "+id;}
}
