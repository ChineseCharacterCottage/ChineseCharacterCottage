package ecnu.chinesecharactercottage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Shensheng on 2016/12/10.
 */

public class CollectionLab {
    private static CollectionLab sLab=null;
    private Context mContext;
    private UserCharDataHelper mUserCharDataHelper;
    public static CollectionLab getLab(Context c){
        if(sLab==null){
            sLab=new CollectionLab(c.getApplicationContext());
        }
        return sLab;
    }
    public void addCollection(CharItem charItem){
        SQLiteDatabase db=mUserCharDataHelper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("id",charItem.getId());
        db.insert("collection",null,cv);
        db.close();
    }
    public boolean isAdded(CharItem charItem){
        SQLiteDatabase db=mUserCharDataHelper.getReadableDatabase();
        Cursor cursor=db.query("collection",null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }
    public CharItem[] getCharItems(){
        try {
            CharItemLab lab = CharItemLab.getLab(mContext);
            SQLiteDatabase db = mUserCharDataHelper.getReadableDatabase();
            ArrayList<CharItem> arrayList = new ArrayList<>();
            Cursor cursor = db.query("collection", null, null, null, null, null, null);
            if (!cursor.moveToFirst()){
                cursor.close();
                return null;
            }
            do {
                String id=cursor.getString(cursor.getColumnIndex("id"));
                CharItem charItem=lab.getCharItem(id);
                if(charItem==null){
                    Log.d("CollectionLab","Null");
                    cursor.close();
                    return null;
                }
                arrayList.add(charItem);
            } while (cursor.moveToNext());
            cursor.close();
            return arrayList.toArray(new CharItem[arrayList.size()]);
        }catch (Exception e){
            Log.d("CollectionLab",e.toString());
        }
        return null;
    }
    private CollectionLab(Context c){
        mContext=c;
        mUserCharDataHelper=UserCharDataHelper.getDatabaseInstance(mContext,UserCharDataHelper.DATABASE_LOCATION,null,UserCharDataHelper.VERSION);
    }
}
