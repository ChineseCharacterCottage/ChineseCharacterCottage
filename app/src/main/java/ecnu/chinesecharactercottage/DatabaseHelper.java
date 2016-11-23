package ecnu.chinesecharactercottage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Shensheng on 2016/11/20.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;
    public DatabaseHelper(Context context, String name , SQLiteDatabase.CursorFactory factory ,int version){
        super(context,name,factory,version);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open("char_database.sql")));
            StringBuffer buffer=new StringBuffer();
            String line;
            while((line=reader.readLine())!=null){
                buffer.append(line);
                buffer.append("\n");
            }
            String[] a=buffer.toString().split(";");
            for (String s :a){
                db.execSQL(s);
            }
        }catch (Exception e){
            Log.d("DEBUG",e.toString());
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        onCreate(db);
    }
}
