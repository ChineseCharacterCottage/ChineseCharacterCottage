package ecnu.chinesecharactercottage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Shensheng on 2016/12/1.
 */

public class RadicalLab {
    private static RadicalLab sLab;
    private DatabaseHelper mDatabaseHelper;
    private Context mContext;
    private int mDataSize;
    public int getSize(){
        return mDataSize;
    }
    public static RadicalLab getLab(Context context){
        if(sLab==null){
            sLab=new RadicalLab(context);
        }
        return sLab;
    }
    public static RadicalLab getLabWithoutContext()throws Exception{
        if(sLab==null){
            throw new Exception("Have no RadicalLab Instance!");
        }
        return sLab;
    }
    private RadicalLab(Context context){
        mContext=context;
        mDatabaseHelper=DatabaseHelper.getDateBaseInstance(mContext,"char_date.db",null,1);
        Cursor cursor=mDatabaseHelper.getReadableDatabase().rawQuery("select count(*) from radical_learning",null);
        cursor.moveToNext();
        mDataSize=cursor.getInt(0);
        cursor.close();
    }

    public RadicalItem getRadical(String id){
        SQLiteDatabase db=mDatabaseHelper.getReadableDatabase();
        Cursor cursor=db.query("radical_learning", null, "ID=" + id, null, null, null, null);
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
        return new RadicalItem(chars,shape,id,name);
        /*
        ArrayList<CharItem> charItems=new ArrayList<>();
        for(String c : chars){
            try {
                CharItem[] cs=CharItemLab.getLab(mContext).findCharItemsByShape(c);
                charItems.add(cs[0]);
            }catch (Exception e){
                Log.d("RadicalLab",e.toString());
            }
        }
        return new RadicalItem(charItems.toArray(new CharItem[charItems.size()]),id,shape);*/
    }
}
