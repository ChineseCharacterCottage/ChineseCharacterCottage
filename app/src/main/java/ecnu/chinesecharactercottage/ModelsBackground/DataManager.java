package ecnu.chinesecharactercottage.ModelsBackground;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.phalapi.sdk.PhalApiClient;
import net.phalapi.sdk.PhalApiClientResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Shensheng on 2017/2/13.
 */

public final class DataManager extends SQLiteOpenHelper{
    private static final String LOCAL_DATABASE="local_character.db";
    private static final int VERSION=1;
    private static final String HOST="http://115.159.147.198/hzw/PhalApi/public/hzw/";
    private static DataManager sManager=null;

    public static DataManager getInstance(Context context){
        if(sManager==null){
            sManager=new DataManager(context,LOCAL_DATABASE,null,VERSION);
        }
        return sManager;
    }

    private DataManager(Context context, String name , SQLiteDatabase.CursorFactory factory , int version){
        super(context,name,factory,version);
    }
    private JSONObject cursorToJSON(Cursor c) throws JSONException{
        JSONObject json = new JSONObject();
        for (int i = 0; i < c.getColumnCount(); i++) {
            json.put(c.getColumnName(i), c.getString(i));
        }
        return json;
    }
    public RadicalItem[] getRadicalByIds(int[] id){
        ArrayList<RadicalItem> array=new ArrayList<>();
        for(int i:id){
            array.add(getRadicalById(i));
        }
        return array.toArray(new RadicalItem[array.size()]);
    }
    public RadicalItem getRadicalById(int id){
        RadicalItem r=getRadicalItemFromLocal(id);
        if(r==null){
            PhalApiClientResponse response=PhalApiClient.create()
                    .withHost(HOST)
                    .withService("Radical.GetRadicalInfo")
                    .withTimeout(300)
                    .withParams("ID",String.format(Locale.ENGLISH,"%d",id))
                    .request();
            if(response.getRet()==200){
                try {
                    JSONObject json = new JSONObject(response.getData());
                    String shape=json.getString("radical_shape");
                    String[] characters=json.getString("characters").split("/");
                    String name=json.getString("name");
                    r=new RadicalItem(characters,shape,String.format(Locale.ENGLISH,"%d",id),name);
                    putRadicalItemToLocal(r);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }else{
                Log.d("DataManager","null radical");
            }
        }
        return r;
    }
    private RadicalItem getRadicalItemFromLocal(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query("radical", null, "ID=" + id, null, null, null, null);
        String shape;
        String[] chars;
        String name;
        if(cursor.moveToFirst()){
            shape=cursor.getString(cursor.getColumnIndex("radical_shape"));
            chars=cursor.getString(cursor.getColumnIndex("characters")).split("/");
            name=cursor.getString(cursor.getColumnIndex("radical_name"));
        }else {
            cursor.close();
            return null;
        }
        cursor.close();
        return new RadicalItem(chars,shape,String.valueOf(id),name);
    }
    private void putRadicalItemToLocal(RadicalItem radical){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        try {
            values.put("ID",radical.getId());
            values.put("radical_shape", radical.getRadical());
            values.put("radical_name",radical.getName());
            String[] ex=radical.getExamples();
            String f="";
            for(int i=0;i<ex.length;i++){
                if(i!=0){
                    f+="/";
                }
                f+=ex[i];
            }
            values.put("characters",f);
            db.insert("radical",null,values);
            db.close();
        }catch (Exception e){
            e.printStackTrace();
            db.close();
        }
    }
    private CharItem getCharItemFromLocal(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.query("char_item", null, "ID =" + id, null, null, null, null);
        if(cursor.moveToFirst()){
            try {
                cursor.close();
                return new CharItem(cursorToJSON(cursor));
            }catch (JSONException e){
                e.printStackTrace();
                cursor.close();
                return null;
            }
        }else{
            cursor.close();
            return null;
        }
    }
    private boolean putCharItemToLocal(CharItem c){
        SQLiteDatabase db=this.getWritableDatabase();
        JSONObject json=c.toJSON();
        ContentValues values=new ContentValues();
        try {
            values.put("ID",json.getString("ID"));
            values.put("character_shape", json.getString("character_shape"));
            values.put("pinyin", json.getString("pinyin"));
            values.put("words", json.getString("words"));
            values.put("sentence", json.getString("sentence"));
            values.put("explanation",json.getString("explanation"));
            values.put("radical_id", json.getString("radical_id"));
            values.put("date",String.valueOf(new Date().getTime()));
            db.insert("char_item",null,values);
            db.close();
            return true;
        }catch (JSONException e){
            e.printStackTrace();
            db.close();
            return false;
        }
    }
    public CharItem[] getCharItemByIds(int[] ids){
        ArrayList<CharItem> charItems=new ArrayList<>();
        for(int id :ids){
            charItems.add(getCharItemById(id));
        }
        return charItems.toArray(new CharItem[charItems.size()]);
    }

    public CharItem getCharItemById(int id){
        CharItem getC=getCharItemFromLocal(id);
        if(getC==null){
            PhalApiClientResponse response=PhalApiClient.create()
                    .withHost(HOST)
                    .withService("Character.GetCharInfo")
                    .withTimeout(300)
                    .withParams("ID",String.format(Locale.ENGLISH,"%d",id))
                    .request();
            if(response.getRet()==200){
                try {
                    JSONObject json = new JSONObject(response.getData());
                    if(json.getString("status").equals("success")){
                        CharItem c=new CharItem(json);
                        putCharItemToLocal(c);
                        return c;
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }else{
                Log.e("DataManager","ErrorCode: "+response.getRet());
            }
        }
        return getC;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_CHAR_ITEM="create table char_item ("
                +"ID integer primary key, "
                +"words text, "
                +"character_shape text, "
                +"pinyin text, "
                +"sentence text, "
                +"explanation text, "
                +"radical_id text, "
                +"date text "
                + ")";
        final String CREATE_RADICAL="create table radical("
                +"ID integer primary key, "
                +"radical_shape text, "
                +"characters text, "
                +"radical_name text "
                + ")";
        db.execSQL(CREATE_CHAR_ITEM);
        db.execSQL(CREATE_RADICAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        db.execSQL("drop table if exists char_item");
        db.execSQL("drop table if exists radical");
    }
}