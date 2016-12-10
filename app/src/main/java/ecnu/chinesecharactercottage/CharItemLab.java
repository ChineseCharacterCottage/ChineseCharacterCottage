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
    private static final int CACHE_SIZE=30;
    private DatabaseHelper mDatabaseHelper;
    private UserCharDataHelper mUserCharDataHelper;
    private int mDataSize;
   // private static final String RESOURCES_FILENAME="resources.json";
 //   private static final String fileName="userdata.json";
  //  private ArrayList<CharItem> mCharItems;
  //  private HashMap<String,CharItem> mHashItems;

    private Context mContext;

    private CharItem[] mCharCache;
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
        mCharCache = new CharItem[CACHE_SIZE];
        loadCharacters();
    }

    public CharItem[] getCharItems(String[] id) throws Exception{
        ArrayList<CharItem> arrayList=new ArrayList<>();
        for(String i : id) {
            arrayList.add(getCharItem(i));
        }
        return arrayList.toArray(new CharItem[arrayList.size()]);
    }

    public CharItem getCharItem(String id) throws Exception{
        int intId=Integer.valueOf(id);
        int intIdMod=intId%CACHE_SIZE;
        if(mCharCache[intIdMod]==null || !(mCharCache[intIdMod].getId().equals(id))) {
                SQLiteDatabase cdb = mDatabaseHelper.getReadableDatabase();
                SQLiteDatabase udb = mUserCharDataHelper.getWritableDatabase();
                Cursor cursor1 = cdb.query("char_item", null, "ID=" + id, null, null, null, null);
                Cursor cursor2 = udb.query("user_char_data", null, "ID=" + id, null, null, null, null);
                JSONObject json = null;
                if (cursor1.moveToFirst()) {
                    json = cursorToJSON(cursor1);
                } else {
                    cdb.close();
                    udb.close();
                    return null;
                    //throw new Exception("Data not found");
                }
                if (cursor2.moveToFirst()) {
                    json.put("date", cursor2.getString(cursor2.getColumnIndex("date")));
                } else {
                    String s = String.valueOf(new GregorianCalendar().getTime().getTime());
                    json.put("date", s);
                    ContentValues values = new ContentValues();
                    values.put("ID", id);
                    values.put("date", s);
                    udb.insert("user_char_data", null, values);
                }
                cdb.close();
                udb.close();
                mCharCache[intIdMod] = new CharItem(json);
        }
        return mCharCache[intIdMod];
    }



   /* public ArrayList<CharItem> getCharItems(){
        return mCharItems;
    }*/

    private JSONObject cursorToJSON(Cursor c)throws JSONException {
        JSONObject json=new JSONObject();
        for(int i=0;i<c.getColumnCount();i++) {
            json.put(c.getColumnName(i),c.getString(i));
        }
        return json;
    }
    //根据汉字字形来找CharItem
    public CharItem[] findCharItemsByShape(String shape){
        ArrayList<CharItem> list=new ArrayList<>();
        SQLiteDatabase db= mDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("char_item", null, CharItem.CHARACTER+"='" + shape+"'", null, null, null, null);
        if(!cursor.moveToFirst())return null;
        do{
            try {
                list.add(new CharItem(cursorToJSON(cursor)));
            }catch (Exception e){
                Log.d("CharItemLab",e.toString());
            }
        }while(cursor.moveToNext());
        cursor.close();
        return list.toArray(new CharItem[list.size()]);
    }
    public int getSize(){
        return mDataSize;
    }
    private void loadCharacters() {
        mDatabaseHelper = DatabaseHelper.getDateBaseInstance(mContext,DatabaseHelper.DATABASE_LOCATION,null,DatabaseHelper.VERSION);
        mUserCharDataHelper = new UserCharDataHelper(mContext,"user_data.db",null,1);
        SQLiteDatabase db=mDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from char_item ",null);
        cursor.moveToNext();
        mDataSize = cursor.getInt(0);
        cursor.close();
        db.close();
    }
}
