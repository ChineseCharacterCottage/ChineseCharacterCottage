package ecnu.chinesecharactercottage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by Shensheng on 2016/9/13.
 * A class keep an ArrayList of CharItem.
 */

public class CharItemLab {

    private static CharItemLab sCharItemLab=null;

    private DatabaseHelper mDatabaseHelper;
    private UserCharDataHelper mUserCharDataHelper;

   // private static final String RESOURCES_FILENAME="resources.json";
 //   private static final String fileName="userdata.json";
  //  private ArrayList<CharItem> mCharItems;
  //  private HashMap<String,CharItem> mHashItems;

    private Context mContext;

    public static CharItemLab getLab(Context c)throws IOException,JSONException{
        if(sCharItemLab==null) {
            sCharItemLab = new CharItemLab(c.getApplicationContext());
        }
        return sCharItemLab;
    }

    public static CharItemLab getLabWithoutContext() throws Exception{
        if(sCharItemLab==null){
            throw new Exception("No lab here.");
        }
        return sCharItemLab;
    }
    private CharItemLab(Context context) throws IOException,JSONException{
        mContext=context.getApplicationContext();
        loadCharacters();
    }
    public CharItem[] getCharItems(String[] id) throws Exception{
        ArrayList<CharItem> arrayList=new ArrayList<>();
        for(String i : id){
            arrayList.add(getCharItem(i));
        }
        return arrayList.toArray(new CharItem[arrayList.size()]);
    }

    public CharItem getCharItem(String id) throws Exception{
     //   return mHashItems.get(id);
        SQLiteDatabase cdb=mDatabaseHelper.getReadableDatabase();
      //  SQLiteDatabase udb_w=mUserCharDataHelper.getWritableDatabase();
        SQLiteDatabase udb_r=mUserCharDataHelper.getReadableDatabase();
        Cursor cursor1=cdb.query("char_data",null,"id="+id,null,null,null,null);
        Cursor cursor2=udb_r.query("user_char_data",null,"id="+id,null,null,null,null);
        JSONObject json=new JSONObject();
        if(cursor1.moveToFirst()){
            json.put("ID",id);
            String pinyin=cursor1.getString(cursor1.getColumnIndex("拼音"));
            String radical=cursor1.getString(cursor1.getColumnIndex("部首"));
            String struct = cursor1.getString(cursor1.getColumnIndex("平面结构"));
            String notes = cursor1.getColumnName(cursor1.getColumnIndex("英文字意"));
            String address = cursor1.getColumnName(cursor1.getColumnIndex("图片"));
            String shape=cursor1.getColumnName(cursor1.getColumnIndex("汉字字形"));
            String media=cursor1.getColumnName(cursor1.getColumnIndex("音频"));
            String words=cursor1.getColumnName(cursor1.getColumnIndex("例词"));
            String sent=cursor1.getColumnName(cursor1.getColumnIndex("例句"));
            json.put("拼音",pinyin);
            json.put("部首",radical);
            json.put("平面结构",struct);
            json.put("英文字意",notes);
            json.put("图片",address);
            json.put("汉字字形",shape);
            json.put("音频",media);
            json.put("例词",words);
            json.put("例句",sent);
        }else {
            throw new Exception("Data not found");
        }
        if(cursor2.moveToFirst()){
            json.put("date",cursor2.getString(cursor2.getColumnIndex("date")));
            udb_r.close();
        }else{
            String s=String.valueOf(new GregorianCalendar().getTime().getTime());
            json.put("date",s);
            ContentValues values=new ContentValues();
            values.put("ID",id);
            values.put("date",s);
            udb_r.close();
            SQLiteDatabase udb_w=mUserCharDataHelper.getWritableDatabase();
            udb_w.insert("user_char_data",null,values);
            udb_w.close();
        }
        cursor1.close();
        cursor2.close();
        return new CharItem(json);
    }



   /* public ArrayList<CharItem> getCharItems(){
        return mCharItems;
    }*/


    private void loadCharacters() {
        mDatabaseHelper = new DatabaseHelper(mContext,"char_date.db",null,0);
        mUserCharDataHelper = new UserCharDataHelper(mContext,"user_data.db",null,0);
    }
        /*
        mCharItems=new ArrayList<>();
        mHashItems=new HashMap<>();
        BufferedReader reader=null;
        try{
            InputStream in=mContext.openFileInput(fileName);
            reader=new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString=new StringBuilder();
            String line=null;
            while((line=reader.readLine())!=null){
                jsonString.append(line);
            }
            JSONArray array=(JSONArray)new JSONTokener(jsonString.toString()).nextValue();
            for(int i=0;i<array.length();i++){
                CharItem ci=new CharItem(array.getJSONObject(i));
                mCharItems.add(ci);
                mHashItems.put(ci.getId(),ci);
            }

        }catch (FileNotFoundException e){
            try {
                InputStream in = mContext.getResources().getAssets().open(RESOURCES_FILENAME);
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder jsonString = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }
                JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
                for (int i = 0; i < array.length(); i++) {
                    CharItem ci=new CharItem(array.getJSONObject(i));
                    mCharItems.add(ci);
                    mHashItems.put(ci.getId(),ci);
                }
            }catch (FileNotFoundException ex){
                System.exit(1);
            }
        }finally {
            if(reader!=null){
                reader.close();
            }
        }
    }
*/
}
