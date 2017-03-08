package ecnu.chinesecharactercottage.ModelsBackground;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.phalapi.sdk.PhalApiClient;
import net.phalapi.sdk.PhalApiClientResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Shensheng on 2017/2/13.
 * 数据管理器
 */

public final class DataManager extends SQLiteOpenHelper{
    private static final String LOCAL_DATABASE="local_character.db";
    private static final int VERSION=1;//数据库的版本，如果数据要更新，改成更大的数字
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
                    .withParams("id",String.format(Locale.ENGLISH,"%d",id))
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
                    .withParams("id",String.format(Locale.ENGLISH,"%d",id))
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
        if(getC!=null){
            String rid=getC.getRadicalId();
            getC.setRadical(getRadicalById(Integer.parseInt(rid)));
        }
        return getC;
    }
    public static final String TOF="tof";
    public static final String HEAR_TOF="hear_tof";
    public static final String FILL="fill";
    public static final String HEAR_CHOICE="hear_choice";
    private TestItem getTestItemFromLocal(String id,String type){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query("test_"+type,null,"ID = "+id,null,null,null,null);
        TestItem testItem=null;
        if(cursor.moveToFirst()){
            switch (type){
                case TOF:
                    testItem=new TestTOFItem(cursor);
                    break;
                case HEAR_TOF:
                    testItem=new TestHearTOFItem(cursor);
                    break;
                case FILL:
                    testItem=new TestFillItem(cursor);
                    break;
                case HEAR_CHOICE:
                    testItem=new TestHearChoiceItem(cursor);
                    break;
                default:
                    testItem=null;
            }
        }
        cursor.close();
        return testItem;
    }
    public CharItem getCharItemByShape(String shape){
        PhalApiClientResponse response=PhalApiClient.create()
                .withHost(HOST)
                .withService("Character.GetShapeId")
                .withTimeout(500)
                .withParams("shape",shape)
                .request();
        if(response.getRet()==200){
            return getCharItemById(Integer.parseInt(response.getData()));
        }else{
            Log.d("DataManager","shape not found");
            return null;
        }
    }
    private void putTestItemToLocal(TestItem testItem,String type){
        ContentValues values=testItem.toContentValue();
        values.put("date",System.currentTimeMillis());
        SQLiteDatabase db=this.getWritableDatabase();
        db.insert("test_"+type,null,values);
        db.close();
    }
    public ComponentItem getComponentById(String id){
        PhalApiClientResponse response = PhalApiClient.create()
                .withHost(HOST)
                .withService("Component.GetComponentInfo")
                .withTimeout(500)
                .withParams("id",id)
                .request();
        if(response.getRet()==200){
            try{
                JSONObject json=new JSONObject(response.getData());
                ComponentItem item=new ComponentItem(json.getString("shape"),
                        json.getString("characters").split("/"),
                        json.getString("explanation"),
                        json.getString("voice_or_shape"),
                        json.getString("ID"));
                return item;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }
    public ComponentItem[] getAllComponents(boolean isVoice){
        String t=isVoice?"v":"s";
        ArrayList<ComponentItem> items=new ArrayList<>();
        PhalApiClientResponse response = PhalApiClient.create()
                .withHost(HOST)
                .withService("Component.GetComponentInfo")
                .withTimeout(500)
                .withParams("valueName","voice_or_shape")
                .withParams("valueIs",t)
                .request();
        if(response.getRet()==200){
            try {
                JSONArray ja = new JSONArray(response.getData());
                for(int i=0;i<ja.length();i++){
                    JSONObject json=ja.getJSONObject(i);
                    ComponentItem item=new ComponentItem(json.getString("shape"),
                            json.getString("characters").split("/"),
                            json.getString("explanation"),
                            json.getString("voice_or_shape"),
                            json.getString("ID"));
                    items.add(item);
                }
                return items.toArray(new ComponentItem[items.size()]);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }
    public TestItem getTestItemById(String id,String type){
        TestItem testItem=getTestItemFromLocal(id,type);
        if(testItem!=null){
            return testItem;
        }
        PhalApiClientResponse response=PhalApiClient.create()
                .withHost(HOST)
                .withService("TestItem.GetTestItem")
                .withParams("testtype",type)
                .withParams("id",id)
                .withTimeout(500)
                .request();
        if(response.getRet()==200){
            try{
                JSONObject json=new JSONObject(response.getData());
                switch (type){
                    case TOF:
                        testItem=new TestTOFItem(json);
                        break;
                    case HEAR_TOF:
                        testItem=new TestHearTOFItem(json);
                        break;
                    case HEAR_CHOICE:
                        testItem=new TestHearChoiceItem(json);
                        break;
                    case FILL:
                        testItem=new TestFillItem(json);
                        break;
                    default:
                        testItem=null;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return testItem;
    }
    //数据库建表操作
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
        final String CREATE_RADICAL="create table radical ("
                +"ID integer primary key, "
                +"radical_shape text, "
                +"characters text, "
                +"radical_name text "
                + ")";
        final String CREATE_TEST_FILL_ITEM="create table test_fill ("
                +"ID integer primary key, "
                +"correct_order text, "
                +"choice_1 text, "
                +"choice_2 text,"
                +"choice_3 text,"
                +"choice_4 text,"
                +"choice_5 text,"
                +"sentence1 text,"
                +"sentence2 text,"
                +"sentence3 text,"
                +"sentence4 text,"
                +"sentence5 text," +
                "date text )";
        final String CREATE_TEST_TOF="create table test_tof ("
                +"ID integer primary key, "
                +"tof text," +
                "relation_character_id text," +
                "picture text," +
                "character_shape text," +
                "date text)";
        final String CREATE_TEST_HEAR_TOF="create table test_hear_tof (" +
                "ID integer primary key, " +
                "tof text," +
                "relation_character_id text," +
                "pronunciation text," +
                "picture text," +
                "date text)";
        final String CREATE_TEST_HEAR_CHOICE="create table test_hear_choice (" +
                "ID integer primary key, " +
                "correct_choice text," +
                "pronunciation text," +
                "relation_character_id text," +
                "picture_a text," +
                "picture_b text," +
                "picture_c text," +
                "picture_d text," +
                "date text)";
        db.execSQL(CREATE_CHAR_ITEM);
        db.execSQL(CREATE_RADICAL);
        db.execSQL(CREATE_TEST_HEAR_TOF);
        db.execSQL(CREATE_TEST_TOF);
        db.execSQL(CREATE_TEST_FILL_ITEM);
        db.execSQL(CREATE_TEST_HEAR_CHOICE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        db.execSQL("drop table if exists char_item");
        db.execSQL("drop table if exists radical");
        db.execSQL("drop table if exists test_fill");
        db.execSQL("drop table if exists test_tof");
        db.execSQL("drop table if exists test_hear_tof");
        db.execSQL("drop table if exists test_hear_choice");
        onCreate(db);
    }
}
