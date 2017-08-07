package ecnu.chinesecharactercottage.modelsBackground;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import net.phalapi.sdk.PhalApiClient;
import net.phalapi.sdk.PhalApiClientResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Shensheng on 2017/2/13.
 * 数据管理器
 */

public final class DataManager extends SQLiteOpenHelper{
    private static final String LOCAL_DATABASE="local_character.db";
    private static final int VERSION=7;//数据库的版本，如果数据要更新，改成更大的数字
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
                    String name=json.getString("radical_name");
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
                CharItem charItem=new CharItem(cursorToJSON(cursor));
                cursor.close();
                return charItem;
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
                        String rid=json.getString("radical_id");
                        CharItem c=new CharItem(json);
                        putCharItemToLocal(c);
                        c.setRadical(getRadicalById(Integer.parseInt(rid)));
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
            try{
                String rid=getC.toJSON().getString("radical_id");
                getC.setRadical(getRadicalById(Integer.parseInt(rid)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return getC;
    }
    public static final String TOF="tof";
    public static final String HEAR_TOF="hear_tof";
    public static final String FILL="fill";
    public static final String HEAR_CHOICE="hear_choice";
    public static final String COMPONENT="component";
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
                case COMPONENT:
                    testItem=new TestComponentItem(cursor);
                    break;
                default:
                    testItem=null;
            }
        }
        cursor.close();
        return testItem;
    }
    public ShapeCharItem getShapeCharItem(String id){
        PhalApiClientResponse response=PhalApiClient.create()
                .withHost(HOST)
                .withService("Character.GetShapeCharInfo")
                .withTimeout(300)
                .withParams("id",id)
                .request();
        if(response.getRet()==200){
            try {
                JSONObject json = new JSONObject(response.getData());
                if(json.getString("status").equals("success")){
                    String rid=json.getString("radical_id");
                    CharItem c=new CharItem(json);
                    putCharItemToLocal(c);
                    ShapeCharItem sc=new ShapeCharItem(c);
                    sc.mVideo=json.getString("video");
                    sc.mShapeId=json.getString("SID");
                    sc.setRadical(getRadicalById(Integer.parseInt(rid)));
                    return sc;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else{
            Log.e("DataManager","ErrorCode: "+response.getRet());
        }
        return null;
    }
    public CharItem getCharItemByShape(String shape){
        PhalApiClientResponse response=PhalApiClient.create()
                .withHost(HOST)
                .withService("Character.GetShapeId")
                .withTimeout(500)
                .withParams("shape", URLEncode(shape))
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
    public ComponentItem getComponentByOrder(int order){
        PhalApiClientResponse response = PhalApiClient.create()
                .withHost(HOST)
                .withService("Component.GetComponentInfoByOrder")
                .withTimeout(500)
                .withParams("order",String.valueOf(order))
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
                .withService("Component.SelectComponent")
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
    public boolean isInCollection(TestItem t){
        String type;
        switch (t.getClass().getSimpleName()){
            case "TestFillItem":
                type = FILL;
                break;
            case "TestHearChoiceItem":
                type = HEAR_CHOICE;
                break;
            case "TestTOFItem":
                type = TOF;
                break;
            case "TestHearTOFItem":
                type = HEAR_TOF;
                break;
            case "TestComponentItem":
                type = COMPONENT;
                break;
            default:
                type = null;
        }
        Cursor cursor = getReadableDatabase().query("collection_test",null,"ID = "+t.getTestId()+" and testtype = '" + type+"'",null,null,null,null);
        boolean ret = cursor.moveToFirst();
        cursor.close();
        return ret;
    }
    //参数为null返回所有
    public TestItem[] getTestItemsCollection(@Nullable String type){
        if(type == null){
            ArrayList<TestItem> items = new ArrayList<>();
            Cursor cursor = getReadableDatabase().query("collection_test",null,"1 = 1",null,null,null,null);
            if(!cursor.moveToFirst()){
                cursor.close();
                return new TestItem[]{};
            }
            do{
                items.add(getTestItemById(
                        cursor.getString(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("testtype"))
                ));
            }while (cursor.moveToNext());
            cursor.close();
            return items.toArray(new TestItem[items.size()]);
        }else{
            ArrayList<TestItem> items = new ArrayList<>();
            Cursor cursor = getReadableDatabase().query("collection_test",null,"testtype = '"+type+"'",null,null,null,null);
            if(!cursor.moveToFirst()){
                cursor.close();
                return new TestItem[]{};
            }
            do{
                items.add(getTestItemById(
                        cursor.getString(cursor.getColumnIndex("ID")),
                        type
                ));
            }while (cursor.moveToNext());
            cursor.close();
            return items.toArray(new TestItem[items.size()]);
        }
    }
    public TestHearChoiceItem getTestByCharShape(String shape){
        PhalApiClientResponse response=PhalApiClient.create()
                .withHost(HOST)
                .withService("TestItem.GetTestByCharShape")
                .withParams("shape",URLEncode(shape))
                .withTimeout(500)
                .request();
        try {
            if(response.getRet()==200){
                JSONObject json = new JSONObject(response.getData());
                return new TestHearChoiceItem(json);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }
    public TestComponentItem getTestComponentItemByCompId(String compId){
        PhalApiClientResponse response=PhalApiClient.create()
                .withHost(HOST)
                .withService("TestItem.GetComponentTestByCompId")
                .withParams("compid",compId)
                .withTimeout(500)
                .request();
        try {
            if(response.getRet()==200){
                JSONObject json = new JSONObject(response.getData());
                return new TestComponentItem(json);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
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
                    case COMPONENT:
                        testItem=new TestComponentItem(json);
                        break;
                    default:
                        testItem=null;
                }
                putTestItemToLocal(testItem,type);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return testItem;
    }
    public void putIntoCollection(TestItem testItem){
        if(isInCollection(testItem))return;
        String type;
        switch (testItem.getClass().getSimpleName()){
            case "TestFillItem":
                type = FILL;
                break;
            case "TestHearChoiceItem":
                type = HEAR_CHOICE;
                break;
            case "TestTOFItem":
                type = TOF;
                break;
            case "TestHearTOFItem":
                type = HEAR_TOF;
                break;
            case "TestComponentItem":
                type = COMPONENT;
                break;
            default:
                type = null;
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID",testItem.getTestId());
        cv.put("testtype",type);
        db.insert("collection_test",null,cv);
        db.close();
    }
    public void putIntoCollection(CharItem charItem){
        if(isInCollection(charItem))return;
        if(charItem.getClass().equals(CharItem.class)){
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("ID",charItem.getId());
            db.insert("collection_char",null,cv);
            db.close();
        }else{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("ID",((ShapeCharItem)charItem).getShapeId());
            db.insert("collection_shape_char",null,cv);
            db.close();
        }
    }
    public boolean isInCollection(CharItem charItem){
        SQLiteDatabase db = getWritableDatabase();
        if(charItem.getClass().equals(ShapeCharItem.class)){
            Cursor cursor = db.query("collection_shape_char",null,"ID = "+((ShapeCharItem)charItem).getShapeId(),null,null,null,null);
            boolean exist = cursor.moveToFirst();
            cursor.close();
            return exist;
        }else {
            Cursor cursor = db.query("collection_char",null,"ID = "+charItem.getId(),null,null,null,null);
            boolean exist = cursor.moveToFirst();
            cursor.close();
            return exist;
        }
    }
    public void removeCollection(CharItem charItem){
        if(!isInCollection(charItem))return;
        SQLiteDatabase db = getWritableDatabase();
        if(charItem.getClass().equals(ShapeCharItem.class)){
            ShapeCharItem sc = (ShapeCharItem)charItem;
            db.delete("collection_shape_char","ID = ?",new String[]{sc.getShapeId()});
            db.close();
        }else{
            db.delete("collection_char","ID = ?",new String[]{charItem.getId()});
            db.close();
        }
    }
    public void removeCollection(TestItem testItem){
        if(!isInCollection(testItem))return;
        SQLiteDatabase db = getWritableDatabase();
        db.delete("collection_test","ID = ? AND testtype = ? ",new String[]{testItem.getTestId(),testItem.getType()});
        db.close();
    }
    public Knowledge getKnowledge(String id){
        PhalApiClientResponse response = PhalApiClient.create()
                .withHost(HOST)
                .withService("Knowledge.GetKnowledge")
                .withTimeout(500)
                .withParams("id",id)
                .request();
        if(response.getRet() == 200){
            try{
                JSONObject json = new JSONObject(response.getData());
                return new Knowledge(id,json.getString("title"),json.getString("video"),json.getString("ktext"));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }
    public Knowledge[] getKnowledgeList(){
        PhalApiClientResponse response = PhalApiClient.create()
                .withHost(HOST)
                .withService("Knowledge.GetList")
                .withTimeout(500)
                .request();
        if(response.getRet() == 200){
            try{
                JSONArray array = new JSONArray(response.getData());
                int l = array.length();
                ArrayList<Knowledge> knowledgeList=new ArrayList<>();
                for(int i=0;i<l;i++){
                    JSONObject json = array.getJSONObject(i);
                    knowledgeList.add(new Knowledge(json.getString("ID"),json.getString("title"),null,null));
                }
                return knowledgeList.toArray(new Knowledge[knowledgeList.size()]);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        return null;
    }
    public String[] getCollectionCharsId(boolean isShape){
        SQLiteDatabase db = getReadableDatabase();
        if(!isShape){
            Cursor cursor = db.query("collection_char",null,"1 = 1",null,null,null,null);
            if(!cursor.moveToFirst()){
                cursor.close();
                return new String[]{};
            }
            ArrayList<String> ids=new ArrayList<>();
            do {
                String id = cursor.getString(cursor.getColumnIndex("ID"));
                ids.add(id);
            }while (cursor.moveToNext());
            cursor.close();
            return ids.toArray(new String[ids.size()]);
        }else{
            Cursor cursor = db.query("collection_shape_char",null,"1 = 1",null,null,null,null);
            if(!cursor.moveToFirst()){
                cursor.close();
                return new String[]{};
            }
            ArrayList<String> ids=new ArrayList<>();
            do {
                String id = cursor.getString(cursor.getColumnIndex("ID"));
                ids.add(id);
            }while (cursor.moveToNext());
            cursor.close();
            return ids.toArray(new String[ids.size()]);
        }
    }
    public static final String TABLE_CHARACTER="character";
    public static final String TABLE_COMPONENT="component";
    public static final String TABLE_COMP_SORT="comp_sort";
    public static final String TABLE_KNOWLEDGE="knowledge";
    //TODO::待补全
    public int countTableItem(String tableName){
        PhalApiClientResponse response = PhalApiClient.create()
                .withHost(HOST)
                .withTimeout(500)
                .withParams("tablename",tableName)
                .withService("Alltable.GetCount")
                .request();
        if(response.getRet() == 200 && (!response.getData().equals("null"))){
            return Integer.valueOf(response.getData());
        }else{
            return -1;
        }
    }
    //数据库建表操作
    private static final String[] tableList ={"collection_char",
            "collection_shape_char",
            "collection_test",
            "char_item",
            "radical",
            "test_fill",
            "test_tof",
            "test_hear_tof",
            "test_hear_choice",
            "test_component"};
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_COLLECTION_CHAR="create table collection_char (" +
                "ID integer primary key" +
                ")";
        final String CREATE_COLLECTION_SHAPE_CHAR =
                "create table collection_shape_char (" +
                        "ID integer primary key" +
                        ")";
        final String CREATE_COLLECTION_TEST =
                "create table collection_test (" +
                        "ID integer," +
                        "testtype text" +
                        ")";
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
                +"choice1 text, "
                +"choice2 text,"
                +"choice3 text,"
                +"choice4 text,"
                +"choice5 text,"
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
                "character_shape text," +
                "picture_a text," +
                "picture_b text," +
                "picture_c text," +
                "picture_d text," +
                "date text)";
        final String CREATE_TEST_COMPONENT="create table test_component (" +
                "ID integer primary key, " +
                "comp_id text," +
                "choice_a text," +
                "choice_b text," +
                "choice_c text," +
                "choice_d text," +
                "correct_ans text)";
        db.execSQL(CREATE_CHAR_ITEM);
        db.execSQL(CREATE_RADICAL);
        db.execSQL(CREATE_TEST_HEAR_TOF);
        db.execSQL(CREATE_TEST_TOF);
        db.execSQL(CREATE_TEST_FILL_ITEM);
        db.execSQL(CREATE_TEST_HEAR_CHOICE);
        db.execSQL(CREATE_COLLECTION_CHAR);
        db.execSQL(CREATE_COLLECTION_SHAPE_CHAR);
        db.execSQL(CREATE_COLLECTION_TEST);
        db.execSQL(CREATE_TEST_COMPONENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        for(String tableName:tableList){
            db.execSQL("drop table if exists "+tableName);
        }
        onCreate(db);
    }

    private static String URLEncode(String source){
        try{
            return URLEncoder.encode(source,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
