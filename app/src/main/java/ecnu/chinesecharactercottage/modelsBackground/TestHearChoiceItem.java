package ecnu.chinesecharactercottage.modelsBackground;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 匡申升
 * 听力选择
 */

public class TestHearChoiceItem extends TestItem implements Readable {
    private String mId;
    private String mCorrectChoice;
    private String mPronunciation;
    private String mRelationCharacterId;
    private String mPictureA;
    private String mPictureB;
    private String mPictureC;
    private String mPictureD;
    private String mRelationCharacterShape;
    /**
     * 从JSON对象中获取一个听力选择题实例。*/
    public TestHearChoiceItem(JSONObject json)throws JSONException{
        mId=json.getString("ID");
        mCorrectChoice=json.getString("correct_choice");
        mRelationCharacterId=json.getString("relation_character_id");
        mPronunciation=json.getString("pronunciation");
        mPictureA=json.getString("picture_a");
        mPictureB=json.getString("picture_b");
        mPictureC=json.getString("picture_c");
        mPictureD=json.getString("picture_d");
        mRelationCharacterShape=json.getString("character_shape");
    }

    /**
     * 获取音频的键。
     * @see Readable*/
    @Override
    public String getMediaKey() {
        return mPronunciation;
    }
    /**
     * 获取和这道题相关的汉字的字形。*/
    public String getRelationCharacterShape() {
        return mRelationCharacterShape;
    }
    /**
     * 从一个数据库游标生成一个汉字实例。
     * @see Cursor,android.database.sqlite.SQLiteOpenHelper*/
    public TestHearChoiceItem(Cursor cursor){
        mId=cursor.getString(cursor.getColumnIndex("ID"));
        mCorrectChoice=cursor.getString(cursor.getColumnIndex("correct_choice"));
        mRelationCharacterId=cursor.getString(cursor.getColumnIndex("relation_character_id"));
        mPronunciation=cursor.getString(cursor.getColumnIndex("pronunciation"));
        mPictureA=cursor.getString(cursor.getColumnIndex("picture_a"));
        mPictureB=cursor.getString(cursor.getColumnIndex("picture_b"));
        mPictureC=cursor.getString(cursor.getColumnIndex("picture_c"));
        mPictureD=cursor.getString(cursor.getColumnIndex("picture_d"));
        mRelationCharacterShape = cursor.getString(cursor.getColumnIndex("character_shape"));
    }
    /**
     * @see TestItem*/
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
        values.put("character_shape",mRelationCharacterShape);
        return values;
    }
    /**
     * @deprecated
     * 用getCorrectAnswer代替*/
    public String getCorrectChoice() {
        return mCorrectChoice;
    }
    /**
     * @deprecated
     * 用getMediaKey代替*/
    public String getPronunciation() {
        return mPronunciation;
    }
    /**
     * 获取和这道题相关的汉字的ID。*/
    public String getRelationCharacterId() {
        return mRelationCharacterId;
    }
    //下面是获取四个选项，不再赘述。
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
    /**
     * @see TestItem*/
    @Override
    public String getTestId(){
        return mId;
    }
    /**
     * @see TestItem*/
    @Override
    public String getCorrectAnswer(){
        return mCorrectChoice;
    }
    /**
     * @see TestItem*/
    @Override
    public String getType(){
        return DataManager.HEAR_CHOICE;
    }
}
