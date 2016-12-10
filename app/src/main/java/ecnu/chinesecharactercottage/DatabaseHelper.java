package ecnu.chinesecharactercottage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Shensheng on 2016/11/20.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_LOCATION="char_date.db";
    public static final int VERSION=1;
    private static DatabaseHelper sHelper;
    public static DatabaseHelper getDateBaseInstance(Context context, String name , SQLiteDatabase.CursorFactory factory ,int version){
        if(sHelper==null){
            sHelper=new DatabaseHelper(context,name,factory,version);
        }
        return sHelper;
    }
    private Context mContext;
    private DatabaseHelper(Context context, String name , SQLiteDatabase.CursorFactory factory ,int version){
        super(context.getApplicationContext(),name,factory,version);
        mContext=context.getApplicationContext();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_CHAR_ITEM="create table char_item ("
                +"ID integer primary key autoincrement, "
                +"words text, "
                +"character text, "
                +"pinyin text, "
                +"sentence text, "
                +"explanation text, "
                +"radical_id text)";
        final String CREATE_RADICAL="create table radical_learning ("
                +"ID integer primary key autoincrement, "
                +"radical_shape text, "
                +"characters text, "
                +"radical_name text)";
        final String CREATE_RADICAL_RELATION ="create table radical_relationship ("
                +"ID integer primary key autoincrement, "
                +"radical_ID1 text, "
                +"radical_ID2 text)";
        final String CREATE_COMPONENT_VOICE="create table component_voice ("
                +"KeyID integer primary key autoincrement, "
                +"ID text,"
                +"shape text,"
                +"characters text,"
                +"explanation text)";
        final String CREATE_COMPONENT_SHAPE="create table component_shape ("
                +"KeyID integer primary key autoincrement, "
                +"ID text,"
                +"shape text,"
                +"characters text,"
                +"explanation text)";
        db.execSQL(CREATE_CHAR_ITEM);
        try {
            InputStreamReader in=new InputStreamReader(mContext.getAssets().open("char_item.txt"));
            BufferedReader reader=new BufferedReader(in);
            String line;
            while((line=reader.readLine())!=null){
                ContentValues values=new ContentValues();
                String[] datas=line.split(";");
                values.put("character",datas[1]);
                values.put("pinyin",datas[2]);
                values.put("words",datas[3]);
                values.put("sentence",datas[4]);
                values.put("explanation",datas[5]);
                values.put("radical_id",datas[6]);
                db.insert("char_item",null,values);
                Log.d("DateBaseHelper",values.toString());
            }
            reader.close();
        }catch (Exception e){
            Log.d("DateBaseHelper",e.toString());
        }
        db.execSQL(CREATE_RADICAL);
        try {
            InputStreamReader in=new InputStreamReader(mContext.getAssets().open("radical_learning.txt"));
            BufferedReader reader=new BufferedReader(in);
            String line;
            while((line=reader.readLine())!=null){
                ContentValues values=new ContentValues();
                String[] datas=line.split(";");
                values.put("radical_shape",datas[1]);
                values.put("characters",datas[2]);
                values.put("radical_name",datas[3]);
                db.insert("radical_learning",null,values);
                Log.d("DateBaseHelper",values.toString());
            }
            reader.close();
        }catch (Exception e){
            Log.d("DateBaseHelper",e.toString());
        }
        db.execSQL(CREATE_RADICAL_RELATION);
        try{
            InputStreamReader in = new InputStreamReader(mContext.getAssets().open("radical_relationship.txt"));
            BufferedReader reader=new BufferedReader(in);
            String line;
            while((line=reader.readLine())!=null){
                ContentValues values=new ContentValues();
                String[] datas=line.split(";");
                values.put("radical_ID1",datas[1]);
                values.put("radical_ID2",datas[2]);
                db.insert("radical_relationship",null,values);
                Log.d("DateBaseHelper",values.toString());
            }
            reader.close();
        }catch (Exception e){
            Log.d("DateBaseHelper",e.toString());
        }
        db.execSQL(CREATE_COMPONENT_SHAPE);
        try{
            InputStreamReader in = new InputStreamReader(mContext.getAssets().open("component_shape.txt"));
            BufferedReader reader=new BufferedReader(in);
            String line;
            while((line=reader.readLine())!=null){
                ContentValues values=new ContentValues();
                String[] datas=line.split(";");
                values.put("ID",datas[0]);
                values.put("shape",datas[1]);
                values.put("characters",datas[2]);
                values.put("explanation",datas[4]);
                db.insert("component_shape",null,values);
                Log.d("DateBaseHelper",values.toString());
            }
            reader.close();
        }catch (Exception e){
            Log.d("DateBaseHelper",e.toString());
        }
        db.execSQL(CREATE_COMPONENT_VOICE);
        try{
            InputStreamReader in = new InputStreamReader(mContext.getAssets().open("component_voice.txt"));
            BufferedReader reader=new BufferedReader(in);
            String line;
            while((line=reader.readLine())!=null){
                ContentValues values=new ContentValues();
                String[] datas=line.split(";");
                values.put("ID",datas[0]);
                values.put("shape",datas[1]);
                values.put("characters",datas[2]);
                values.put("explanation",datas[4]);
                db.insert("component_voice",null,values);
                Log.d("DateBaseHelper",values.toString());
            }
            reader.close();
        }catch (Exception e){
            Log.d("DateBaseHelper",e.toString());
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("drop table if exists char_item");
        db.execSQL("drop table if exists radical_learning");
        db.execSQL("drop table if exists radical_relationship");
        db.execSQL("drop table if exists component_voice");
        db.execSQL("drop table if exists component_shape");
        onCreate(db);
    }
}
