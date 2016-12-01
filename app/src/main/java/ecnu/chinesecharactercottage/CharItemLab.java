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
            SQLiteDatabase udb_r = mUserCharDataHelper.getReadableDatabase();
            Cursor cursor1 = cdb.query("char_item", null, "id=" + id, null, null, null, null);
            Cursor cursor2 = udb_r.query("user_char_data", null, "id=" + id, null, null, null, null);
            JSONObject json = null;
            if (cursor1.moveToFirst()) {
                json = cursorToJSON(cursor1);
            } else {
                throw new Exception("Data not found");
            }
            if (cursor2.moveToFirst()) {
                json.put("date", cursor2.getString(cursor2.getColumnIndex("date")));
                udb_r.close();
            } else {
                String s = String.valueOf(new GregorianCalendar().getTime().getTime());
                json.put("date", s);
                ContentValues values = new ContentValues();
                values.put("ID", id);
                values.put("date", s);
                udb_r.close();
                SQLiteDatabase udb_w = mUserCharDataHelper.getWritableDatabase();
                udb_w.insert("user_char_data", null, values);
                udb_w.close();
            }
            cursor1.close();
            cursor2.close();
            mCharCache[intIdMod]=new CharItem(json);
        }
        return mCharCache[intId%CACHE_SIZE];
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
    private void loadCharacters() {
        mDatabaseHelper = new DatabaseHelper(mContext,"char_date.db",null,0);
        mUserCharDataHelper = new UserCharDataHelper(mContext,"user_data.db",null,0);
    }
}
