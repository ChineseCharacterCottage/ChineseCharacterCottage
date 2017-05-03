package ecnu.chinesecharactercottage.ModelsBackground;
import android.content.ContentValues;
import android.database.Cursor;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shensheng on 2017/3/6.
 * 听力判断
 */

public class TestHearTOFItem extends TestItem implements Readable{

    private String mId;
    private boolean mTof;
    private String mRelationCharacterId;
    private String mPicture;
    private String mPronunciation;

    public TestHearTOFItem(JSONObject json)throws JSONException{
        mId=json.getString("ID");
        mTof=(json.getInt("tof")==1);
        mRelationCharacterId=json.getString("relation_character_id");
        mPronunciation=json.getString("pronunciation");
        mPicture=json.getString("picture");
    }

    @Override
    public String getMediaKey() {
        return mPronunciation;
    }

    public TestHearTOFItem(Cursor cursor){
        mId=cursor.getString(cursor.getColumnIndex("ID"));
        mTof=(cursor.getInt(cursor.getColumnIndex("tof"))==1);
        mRelationCharacterId=cursor.getString(cursor.getColumnIndex("relation_character_id"));
        mPronunciation=cursor.getString(cursor.getColumnIndex("pronunciation"));
        mPicture=cursor.getString(cursor.getColumnIndex("picture"));
    }
    @Override
    public ContentValues toContentValue(){
        ContentValues values=new ContentValues();
        values.put("ID",mId);
        values.put("tof",mTof?"1":"0");
        values.put("relation_character_id",mRelationCharacterId);
        values.put("pronunciation",mPronunciation);
        values.put("picture",mPicture);
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

    public String getPicture() {
        return mPicture;
    }

    public String getPronunciation() {
        return mPronunciation;
    }

    @Override
    public Boolean getCorrectAnswer(){
        return mTof;
    }

    @Override
    public String getType(){
        return DataManager.HEAR_TOF;
    }
}
