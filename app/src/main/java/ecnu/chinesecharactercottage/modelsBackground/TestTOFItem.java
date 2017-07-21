package ecnu.chinesecharactercottage.modelsBackground;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shensheng on 2017/3/6.
 * 判断
 */

public class TestTOFItem extends TestItem {

    private String mId;
    private boolean mTof;
    private String mRelationCharacterId;
    private String mCharacterShape;
    private String mPicture;

    public TestTOFItem(JSONObject json)throws JSONException{
        mId=json.getString("ID");
        mTof=(json.getInt("tof")==1);
        mRelationCharacterId=json.getString("relation_character_id");
        mPicture=json.getString("picture");
        mCharacterShape=json.getString("character_shape");
    }
    public TestTOFItem(Cursor cursor){
        mId=cursor.getString(cursor.getColumnIndex("ID"));
        mTof=(cursor.getInt(cursor.getColumnIndex("tof"))==1);
        mRelationCharacterId=cursor.getString(cursor.getColumnIndex("relation_character_id"));
        mPicture=cursor.getString(cursor.getColumnIndex("picture"));
        mCharacterShape=cursor.getString(cursor.getColumnIndex("character_shape"));
    }
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
    public String getTestId() {
        return mId;
    }

    public boolean isTof() {
        return mTof;
    }


    public String getRelationCharacterId() {
        return mRelationCharacterId;
    }

    public String getCharacterShape() {
        return mCharacterShape;
    }


    public String getPicture() {
        return mPicture;
    }

    @Override
    public Boolean getCorrectAnswer(){
        return mTof;
    }

    @Override
    public String getType(){
        return DataManager.TOF;
    }
}
