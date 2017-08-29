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
 * @author 匡申升
 * <p>数据管理器，用于从服务器上获取汉字实例、测试题目等数据，使用SQLite数据库作为本地缓存。</p>
 * <p>关于缓存：在调用者试图获取数据实例时，这里会检测该数据是否存在本地缓存，如果存在，直接返回缓存的数据，否则从服务器
 * 上获取数据，然后缓存到本地。</p>
 * <p>该类中，涉及到网络访问的方法只能在非主线程中进行。</p>
 * @see android.database.sqlite
 */

public final class DataManager extends SQLiteOpenHelper{

    private static final String LOCAL_DATABASE="local_character.db";//本地缓存数据库的文件名。
    private static final int VERSION=7;//数据库的版本，如果数据要更新，改成更大的数字
    private static final String HOST="http://115.159.147.198/hzw/PhalApi/public/hzw/";//服务器的host
    private static DataManager sManager=null;//静态实例对象，符合单例模式。请参考http://blog.csdn.net/jason0539/article/details/23297037/

    /**
     * getInstance()用于获取一个DataManager实例。
     * 该实例在此方法第一次被调用时生成，为全局唯一。为防止出现并发错误，
     * 请尽量在主线程中调用这个方法，除非你确信之前已经调用过该方法生成过实例。
     * @return DataManager 数据管理器实例
     * @param context App的上下文*/
    public static DataManager getInstance(Context context){
        if(sManager==null){
            sManager=new DataManager(context,LOCAL_DATABASE,null,VERSION);
        }
        return sManager;
    }

    /**
     * 私有的构造器，用于构造一个数据管理器。参数已经在getInstance()内写死，此处不再赘述。*/
    private DataManager(Context context, String name , SQLiteDatabase.CursorFactory factory , int version){
        super(context,name,factory,version);
    }
    /**
     * 私有的一个工具方法，可以将游标处的数据转换为JSON对象。
     * 关于JSON可以参考 http://www.w3school.com.cn/json/
     * @throws JSONException 当JSON解析错误时抛出。*/
    private JSONObject cursorToJSON(Cursor c) throws JSONException{
        JSONObject json = new JSONObject();
        for (int i = 0; i < c.getColumnCount(); i++) {
            json.put(c.getColumnName(i), c.getString(i));
        }
        return json;
    }

    /**
     * 通过部首的ID序列获取对应的部首。
     * @param id 要获取的所有部首的ID
     * @return RadicalItem[] 部首数组，id不正确数组的该位置为null*/
    public RadicalItem[] getRadicalByIds(int[] id){
        ArrayList<RadicalItem> array=new ArrayList<>();
        for(int i:id){
            array.add(getRadicalById(i));
        }
        return array.toArray(new RadicalItem[array.size()]);
    }
    /**
     * 通过ID获取单一部首，有缓存机制。
     * @param id 部首的id
     * @return RadicalItem 部首实例，ID不正确则返回null*/
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
    /**
     * 私有方法，从数据库缓存中获取部首实例。
     * @param id 部首的ID
     * @return RadicalItem 部首实例，当缓存中不存在时，返回null*/
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
    /**
     * 私有方法，将部首实例缓存到本地数据库。
     * @param radical 部首实例*/
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

    /**
     * 从本地缓存获取汉字实例。
     * @param id 汉字的ID
     * @return CharItem 汉字实例，本地没有缓存时返回null*/
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
    /**
     * 将汉字缓存到本地。
     * @param c 要缓存的汉字
     * @return boolean 失败时为false。一般不会失败，返回值可以丢弃。*/
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
    /**
     * 根据ID序列来获取对应的所有汉字实例。
     * @param ids 汉字的ID序列
     * @return CharItem[] 汉字的实例数组，当某个ID不存在时，该位置为null*/
    public CharItem[] getCharItemByIds(int[] ids){
        ArrayList<CharItem> charItems=new ArrayList<>();
        for(int id :ids){
            charItems.add(getCharItemById(id));
        }
        return charItems.toArray(new CharItem[charItems.size()]);
    }
    /**
    * 从某个ID开始，获取固定数量的汉字实例。
    * @param from 开始的汉字ID，注意该汉字也会被包括在返回的数组里。
    * @param number 要获取的汉字数量。
    * @return CharItem[] 返回的所有汉字
    * */
    public CharItem[] getCharItemsByFrom(int from, int number){
        return getCharItemByIds(getIdsInTable(from,number,TABLE_CHARACTER));
    }
    /**
     * 根据ID获取汉字实例。有缓存机制。
     * @param id 汉字ID
     * @return CharItem 汉字实例，ID不正确时返回null*/
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
    /*
    这五个静态常量是表名。和远程数据库里的表名相同。*/
    public static final String TOF="tof";
    public static final String HEAR_TOF="hear_tof";
    public static final String FILL="fill";
    public static final String HEAR_CHOICE="hear_choice";
    public static final String COMPONENT="component";

    /**
     * 私有方法，从本地缓存获取测验题实例。
     * @param id 测验题的ID
     * @param type 测验题的类型，需要填写一个已经定义的常量，下面有说明。
     * @return TestItem 测验题的实例。需要向下转型为你需要的测验题类型。*/
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
    /**
     * 获取一个有视频的汉字实例，没有缓存机制。
     * @param id 汉字ID
     * @return ShapeCharItem 有视频的汉字实例*/
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
    /**
     * 通过字形来获取汉字。
     * @param shape 汉字的字形，如“广”、“床”等。
     * @return CharItem 汉字的实例。*/
    public CharItem getCharItemByShape(String shape){
        PhalApiClientResponse response=PhalApiClient.create()
                .withHost(HOST)
                .withService("Character.GetShapeId")
                .withTimeout(500)
                .withParams("shape", URLEncode(shape))
                .request();
        //说明：这里先从服务器获取了字形的ID，然后再通过getCharItemById()获取汉字实例。因为可以利用这个方法的缓存机制。
        if(response.getRet()==200){
            return getCharItemById(Integer.parseInt(response.getData()));
        }else{
            Log.d("DataManager","shape not found");
            return null;
        }
    }
    /**
     * 私有方法，将测试题目的数据放在本地。\
     * @param testItem 测试题实例。
     * @param type 测试题类型。注：以后可以改成直接通过反射来知道测试题的类型。*/
    private void putTestItemToLocal(TestItem testItem,String type){
        ContentValues values=testItem.toContentValue();
        values.put("date",System.currentTimeMillis());
        SQLiteDatabase db=this.getWritableDatabase();
        db.insert("test_"+type,null,values);
        db.close();
    }
    /**
     * 根据部件的排列顺序获取一个部件。
     * @param order 这个部件的序列号
     * @return ComponentItem 部件实例*/
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
    /**
     * 通过ID来获取部件。
     * @param id 部件的ID
     * @return ComponentItem 部件实例*/
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
    /**
     * 获取所有表声（形）的部件。
     * @param  isVoice 为true时，返回所有声部，否则返回形部。
     * @return ComponentItem[] 返回的部件
     * */
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
    /**
     * 判断某个测试题是否在收藏里。
     * @param t 测验题实例
     * @return boolean 在收藏里则返回true*/
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
    /**
     * 一次性获取所有固定类型的测验题实例。
     * @param type 测验题类型，如果为null，则返回所有测验题。
     * @return TestItem[] 测验题实例
     * */
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
    /**
     * 获取和某个汉字有关的听力测试题。
     * @param shape 汉字字形，如“广”等。
     * @return TestHearChoiceItem 听力选择测验题实例。
     * */
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
    /**
     * 根据部件ID，获取部件相关的测验题实例。
     * @param compId 部件ID
     * @return 测验题实例*/
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
    /**
     * 根据ID和类型获取测验题实例，有缓存机制。
     * @param id 测验题ID
     * @param type 类型
     * @return 测验题实例*/
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
    /**
     * 收藏一个测验题。
     * @param testItem 测验题*/
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
    /**
     * 收藏一个汉字。
     * @param charItem 汉字实例*/
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
    /**
     * 判断汉字是否在收藏中。
     * @param charItem 要判断的汉字。
     * @return 在则返回true*/
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
    /**
     * 移除收藏的汉字。
     * @param charItem 汉字*/
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
    /**
     * 移除收藏的题目。
     * @param testItem 题目*/
    public void removeCollection(TestItem testItem){
        if(!isInCollection(testItem))return;
        SQLiteDatabase db = getWritableDatabase();
        db.delete("collection_test","ID = ? AND testtype = ? ",new String[]{testItem.getTestId(),testItem.getType()});
        db.close();
    }
    /**
     * 获取一个小知识。
     * @param id 小知识的ID
     * @return 小知识的实例*/
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
    /**
     * @return 获取所有小知识。*/
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
    /**
     * 获取收藏的所有汉字的ID。
     * @param isShape 为true时，返回所有带视频的汉字ID。
     * @return ID*/
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
    /*表名。*/
    public static final String TABLE_CHARACTER="character";
    public static final String TABLE_COMPONENT="component";
    public static final String TABLE_COMP_SORT="comp_sort";
    public static final String TABLE_KNOWLEDGE="knowledge";
    /**
     * 获取服务器上某个表的条目数量。*/
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
    /**
     * 获取服务器上从某个ID开始的规定数量的实例ID。
     * @param from 开始的ID
     * @param number 获取的数量
     * @param tableName 表名
     * @return id 数组*/
    private int[] getIdsInTable(int from, int number,String tableName){
        PhalApiClientResponse response = PhalApiClient.create()
                .withHost(HOST)
                .withTimeout(500)
                .withParams("tablename",tableName)
                .withParams("from",String.valueOf(from))
                .withParams("lmt",String.valueOf(number))
                .withService("Alltable.GetIds")
                .request();
        if(response.getRet() == 200){
            try {
                JSONArray ja = new JSONArray(response.getData());
                int[] integers = new int[ja.length()];
                for(int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    integers[i] = Integer.parseInt( jo.getString("ID") );
                }
                return integers;
            }catch (Exception e){
                e.printStackTrace();
                return new int[]{};
            }
        }else{
            return new int[]{};
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
    /**
     * 用于创建一个SQL数据库实例。
     * @see android.database.sqlite*/
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

    /**
     * 当本地缓存数据库结构更新时，自动调用。*/
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        for(String tableName:tableList){
            db.execSQL("drop table if exists "+tableName);
        }
        onCreate(db);
    }
    /**
     * URLEncode处理。注意，发往服务器的所有汉字必须做这个处理，否则无法被服务器接受。
     * 关于URLEncode，可参考：http://baike.sogou.com/v7995214.htm?fromTitle=urlencode*/
    private static String URLEncode(String source){
        try{
            return URLEncoder.encode(source,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
