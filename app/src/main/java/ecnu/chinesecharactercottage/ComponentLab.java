package ecnu.chinesecharactercottage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Shensheng on 2016/12/9.
 */

public class ComponentLab {
    private static ComponentLab sComponentLab;
    private Context mContext;
    private DatabaseHelper mDatabaseHelper;
    private int mNumOfShape;
    private int mNumOfVoice;
    public int getNumOfShapeComponents(){
        return mNumOfShape;
    }
    public int getNumOfVoiceComponents(){
        return mNumOfVoice;
    }
    public static ComponentLab getLab(Context c){
        if(sComponentLab==null){
            c=c.getApplicationContext();
            sComponentLab=new ComponentLab(c);
        }
        return sComponentLab;
    }
    public ComponentItem[] getShapeComponents(String[] ids){
        ArrayList<ComponentItem> arrayList=new ArrayList<>();
        for (String id :ids){
            arrayList.add(getShapeComponent(id));
        }
        return arrayList.toArray(new ComponentItem[arrayList.size()]);
    }
    public ComponentItem[] getVoiceComponents(String[] ids){
        ArrayList<ComponentItem> arrayList=new ArrayList<>();
        for (String id :ids){
            arrayList.add(getVoiceComponent(id));
        }
        return arrayList.toArray(new ComponentItem[arrayList.size()]);
    }
    public ComponentItem getShapeComponent(String id){
        SQLiteDatabase db=mDatabaseHelper.getReadableDatabase();
        Cursor cursor=db.query("component_shape", null, "KeyID=" + id, null, null, null, null);
        String shape;
        String[] chars;
        String exp;
        String gid;
        if(cursor.moveToFirst()){
            shape=cursor.getString(cursor.getColumnIndex("shape"));
            chars=cursor.getString(cursor.getColumnIndex("characters")).split("/");
            exp=cursor.getString(cursor.getColumnIndex("explanation"));
            gid=cursor.getString(cursor.getColumnIndex("ID"));
        }else {
            cursor.close();
            return null;
        }
        cursor.close();
        return new ComponentItem(shape,chars,exp,"shape",gid);
    }
    public ComponentItem getVoiceComponent(String id){
        SQLiteDatabase db=mDatabaseHelper.getReadableDatabase();
        Cursor cursor=db.query("component_voice", null, "KeyID=" + id, null, null, null, null);
        String shape;
        String[] chars;
        String exp;
        String gid;
        if(cursor.moveToFirst()){
            shape=cursor.getString(cursor.getColumnIndex("shape"));
            chars=cursor.getString(cursor.getColumnIndex("characters")).split("/");
            exp=cursor.getString(cursor.getColumnIndex("explanation"));
            gid=cursor.getString(cursor.getColumnIndex("ID"));
        }else {
            cursor.close();
            return null;
        }
        cursor.close();
        return new ComponentItem(shape,chars,exp,"voice",gid);
    }
    private ComponentLab(Context c){
        mContext=c;
        mDatabaseHelper=DatabaseHelper.getDateBaseInstance(mContext,DatabaseHelper.DATABASE_LOCATION,null,DatabaseHelper.VERSION);
        SQLiteDatabase db=mDatabaseHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select count(*) from component_shape",null);
        cursor.moveToNext();
        mNumOfShape=cursor.getInt(0);
        cursor=db.rawQuery("select count(*) from component_voice",null);
        cursor.moveToNext();
        mNumOfVoice=cursor.getInt(0);
        cursor.close();
    }
}
