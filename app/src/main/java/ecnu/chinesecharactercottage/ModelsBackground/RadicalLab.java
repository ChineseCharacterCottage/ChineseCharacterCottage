package ecnu.chinesecharactercottage.ModelsBackground;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
    public RadicalItem[] findRelationship(RadicalItem radicalItem){
        ArrayList<RadicalItem> arrayList=new ArrayList<>();
        ArrayList<String> stringArrayList=new ArrayList<>();
        String id=radicalItem.getId();
        SQLiteDatabase db=mDatabaseHelper.getReadableDatabase();
        Cursor cursor=db.query("radical_relationship",null,"radical_ID1="+id,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                stringArrayList.add(cursor.getString(cursor.getColumnIndex("radical_ID2")));
            }while (cursor.moveToNext());
        }
        cursor=db.query("radical_relationship",null,"radical_ID2="+id,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                stringArrayList.add(cursor.getString(cursor.getColumnIndex("radical_ID1")));
            }while (cursor.moveToNext());
        }
        cursor.close();
        if(stringArrayList.size()==0){
            return null;
        }
        for (String idi:stringArrayList){
            arrayList.add(getRadical(idi));
        }
        return arrayList.toArray(new RadicalItem[arrayList.size()]);
    }

    private RadicalLab(Context context){
        mContext=context;
        mDatabaseHelper=DatabaseHelper.getDateBaseInstance(mContext,DatabaseHelper.DATABASE_LOCATION,null,DatabaseHelper.VERSION);
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
    }
}
