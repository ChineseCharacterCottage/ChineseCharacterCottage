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
        final String CREATE_RADIACL_RELATION ="create table radical_relationship ("
                +"ID integer primary key autoincrement, "
                +"radical_ID1 text, "
                +"radical_ID2 text)";
        /*final String CREATE_COMPONENT="create table component ("
                +"ID integer primary key autoincrement, "
                +"";*/
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
        /*
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open("char_database.sql")));
            StringBuffer buffer=new StringBuffer();
            String line;
            while((line=reader.readLine())!=null){
                buffer.append(line);
                buffer.append("\n");
            }
            String[] a=buffer.toString().split(";");
            for (String s :a){
                try {
                    db.execSQL(s);
                    Log.d("SQL",s);
                }catch (Exception e){
                    Log.d("execSQL",e.toString());
                    Log.d("execSQL","SQL: "+s);
                   // e.printStackTrace();
                }
            }
        }catch (Exception e){
            Log.d("DatabaseHelper",e.toString());
        }*/
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("drop table if exists char_item");
        db.execSQL("drop table if exists radical_learning");
        onCreate(db);
    }
}
