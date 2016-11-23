package ecnu.chinesecharactercottage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shensheng on 2016/11/20.
 */

public class UserCharDataHelper extends SQLiteOpenHelper {

    private static final String CREATE_USER_CHAR_DATA =
             "create table user_char_data ("
            +"id integer, "
            +"date text )";
    public UserCharDataHelper(Context context, String name , SQLiteDatabase.CursorFactory factory , int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_CHAR_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        db.execSQL("drop table if exists user_char_data ");
        onCreate(db);
    }
}
