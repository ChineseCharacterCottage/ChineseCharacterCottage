package ecnu.chinesecharactercottage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shensheng on 2017/2/13.
 */

public final class CharItemManager extends SQLiteOpenHelper{
    private static final String LOCAL_DATABASE="local_character.db";
    private static final int VERSION=1;

    private static CharItemManager sManager=null;

    public static CharItemManager getInstance(Context context){
        if(sManager==null){
            sManager=new CharItemManager(context,LOCAL_DATABASE,null,VERSION);
        }
        return sManager;
    }
    private CharItemManager(Context context, String name , SQLiteDatabase.CursorFactory factory , int version){
        super(context,name,factory,version);
    }

    public CharItem getCharItemById(int id){
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {

    }
}
