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
        mDatabaseHelper=DatabaseHelper.getDateBaseInstance(mContext,"char_date.db",null,0);
    }

    public RadicalItem getRadical(String id){
        SQLiteDatabase db=mDatabaseHelper.getReadableDatabase();
        Cursor cursor=db.query("radical_learning", null, "ID=" + id, null, null, null, null);
        String shape=cursor.getString(cursor.getColumnIndex("radical_shape"));
        String[] chars;
        if(cursor.moveToFirst()){
            chars=cursor.getString(cursor.getColumnIndex("characters")).split(",");
        }else {
            db.close();
            return null;
        }
        db.close();
        return new RadicalItem(chars,id,shape);
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
