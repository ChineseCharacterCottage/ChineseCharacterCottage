package ecnu.chinesecharactercottage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shensheng on 2016/11/20.
 */

public class UserCharDataHelper extends SQLiteOpenHelper {
    private static UserCharDataHelper sData=null;
    public static final String DATABASE_LOCATION="user_data.db";
    public static final int VERSION=1;
    private static final String CREATE_USER_CHAR_DATA =
             "create table user_char_data ("
            +"id integer, "
            +"date text )";
    private static final String CREATE_COLLECTION =
            "create table collection ("
            +"KeyID integer primary key autoincrement, "
            +"id integer )";
    public static UserCharDataHelper getDatabaseInstance(Context context, String name , SQLiteDatabase.CursorFactory factory , int version){
        if(sData==null){
            sData=new UserCharDataHelper(context.getApplicationContext(),name,factory,version);
        }
        return sData;
    }
    private UserCharDataHelper(Context context, String name , SQLiteDatabase.CursorFactory factory , int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_CHAR_DATA);
        db.execSQL(CREATE_COLLECTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        db.execSQL("drop table if exists user_char_data");
        db.execSQL("drop table if exists collection");
        onCreate(db);
    }
}
