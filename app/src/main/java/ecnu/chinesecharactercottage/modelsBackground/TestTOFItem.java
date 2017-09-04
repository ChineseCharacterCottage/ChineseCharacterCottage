package ecnu.chinesecharactercottage.modelsBackground;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 匡申升
 * 判断题。简单的图片判断题，根据一张图片来判断正误。
 */

public class TestTOFItem extends TestItem {

    private String mId;
    private boolean mTof;
    private String mRelationCharacterId;
    private String mCharacterShape;
    private String mPicture;
    /**
     * 根据一个JSON生成一个判断题实例。*/
    public TestTOFItem(JSONObject json)throws JSONException{
        mId=json.getString("ID");
        mTof=(json.getInt("tof")==1);
        mRelationCharacterId=json.getString("relation_character_id");
        mPicture=json.getString("picture");
        mCharacterShape=json.getString("character_shape");
    }
    /**
     * 根据一个游标生成一个判断题实例。
     * @see DataManager,android.database.sqlite.SQLiteDatabase,android.database.sqlite.SQLiteOpenHelper,Cursor*/
    public TestTOFItem(Cursor cursor){
        mId=cursor.getString(cursor.getColumnIndex("ID"));
        mTof=(cursor.getInt(cursor.getColumnIndex("tof"))==1);
        mRelationCharacterId=cursor.getString(cursor.getColumnIndex("relation_character_id"));
        mPicture=cursor.getString(cursor.getColumnIndex("picture"));
        mCharacterShape=cursor.getString(cursor.getColumnIndex("character_shape"));
    }
    /**
     * @see TestItem*/
    @Override
    public ContentValues toContentValue(){
        ContentValues values=new ContentValues();
        values.put("ID",mId);
        values.put("tof",mTof?"1":"0");
        values.put("relation_character_id",mRelationCharacterId);
        values.put("picture",mPicture);
        values.put("character_shape",mCharacterShape);
        return values;
    }
    /**
     * @see TestItem*/
    @Override
    public String getTestId() {
        return mId;
    }

    /**
     * 返回这道题的正确答案。已废除，用getCorrectAnswer代替。
     * @deprecated */
    public boolean isTof() {
        return mTof;
    }
    /**
     *获取和这个判断题有关的字的ID。
     */
    public String getRelationCharacterId() {
        return mRelationCharacterId;
    }
    /**
     * 获取和这个判断题有关的字。*/
    public String getCharacterShape() {
        return mCharacterShape;
    }

    /**
     * 获取这个判断题的图片。*/
    public String getPicture() {
        return mPicture;
    }
    /**
     * @see TestItem*/
    @Override
    public Boolean getCorrectAnswer(){
        return mTof;
    }
    /**
     * @see TestItem*/
    @Override
    public String getType(){
        return DataManager.TOF;
    }
}
