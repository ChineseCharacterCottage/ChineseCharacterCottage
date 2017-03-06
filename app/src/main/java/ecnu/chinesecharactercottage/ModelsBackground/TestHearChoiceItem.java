package ecnu.chinesecharactercottage.ModelsBackground;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shensheng on 2017/3/6.
 * 听力选择
 */

public class TestHearChoiceItem extends TestItem {
    private String mId;
    private String mCorrectChoice;
    private String mPronunciation;
    private String mRelationCharacterId;
    private String mPictureA;
    private String mPictureB;
    private String mPictureC;
    private String mPictureD;
    public TestHearChoiceItem(JSONObject json)throws JSONException{
        mId=json.getString("ID");
        mCorrectChoice=json.getString("correct_choice");
        mRelationCharacterId=json.getString("relation_character_id");
        mPronunciation=json.getString("pronunciation");
        mPictureA=json.getString("picture_a");
        mPictureB=json.getString("picture_b");
        mPictureC=json.getString("picture_c");
        mPictureD=json.getString("picture_d");
    }

    public TestHearChoiceItem(Cursor cursor){
        mId=cursor.getString(cursor.getColumnIndex("ID"));
        mCorrectChoice=cursor.getString(cursor.getColumnIndex("correct_choice"));
        mRelationCharacterId=cursor.getString(cursor.getColumnIndex("relation_character_id"));
        mPronunciation=cursor.getString(cursor.getColumnIndex("pronunciation"));
        mPictureA=cursor.getString(cursor.getColumnIndex("picture_a"));
        mPictureB=cursor.getString(cursor.getColumnIndex("picture_b"));
        mPictureC=cursor.getString(cursor.getColumnIndex("picture_c"));
        mPictureD=cursor.getString(cursor.getColumnIndex("picture_d"));
    }
    @Override
    public ContentValues toContentValue(){
        ContentValues values=new ContentValues();
        values.put("ID",mId);
        values.put("correct_choice",mCorrectChoice);
        values.put("relation_character_id",mRelationCharacterId);
        values.put("pronunciation",mPronunciation);
        values.put("picture_a",mPictureA);
        values.put("picture_b",mPictureB);
        values.put("picture_c",mPictureC);
        values.put("picture_d",mPictureD);
        return values;
    }
    public String getCorrectChoice() {
        return mCorrectChoice;
    }

    public String getPronunciation() {
        return mPronunciation;
    }

    public String getRelationCharacterId() {
        return mRelationCharacterId;
    }

    public String getPictureA() {
        return mPictureA;
    }

    public String getPictureB() {
        return mPictureB;
    }

    public String getPictureC() {
        return mPictureC;
    }

    public String getPictureD() {
        return mPictureD;
    }

    public String getTestId(){
        return mId;
    }

    @Override
    public String getCorrectAnswer(){
        return mCorrectChoice;
    }
}
