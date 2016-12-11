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
        if(isAdded(charItem)){
            return;
        }
        SQLiteDatabase db=mUserCharDataHelper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("id",charItem.getId());
        db.insert("collection",null,cv);
        db.close();
    }
    public boolean isAdded(CharItem charItem){
        SQLiteDatabase db=mUserCharDataHelper.getReadableDatabase();
        Cursor cursor=db.query("collection",null,"id="+charItem.getId(), null, null, null, null);
        if(cursor.moveToFirst()){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }
    public void removeCollection(CharItem charItem){
        if(!isAdded(charItem)){
            return;
        }
        SQLiteDatabase db=mUserCharDataHelper.getWritableDatabase();
        db.delete("collection","id=?",new String[]{charItem.getId()});
        db.close();
    }
    public String[] getCharItemIDs(){
        try {
            CharItemLab lab = CharItemLab.getLab(mContext);
            SQLiteDatabase db = mUserCharDataHelper.getReadableDatabase();
            ArrayList<String> arrayList = new ArrayList<>();
            Cursor cursor = db.query("collection", null, null, null, null, null, null);
            if (!cursor.moveToFirst()){
                cursor.close();
                return null;
            }
            do {
                String id=cursor.getString(cursor.getColumnIndex("id"));
                arrayList.add(id);
            } while (cursor.moveToNext());
            cursor.close();
            return arrayList.toArray(new String[arrayList.size()]);
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
