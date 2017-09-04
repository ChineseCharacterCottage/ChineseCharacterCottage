package ecnu.chinesecharactercottage.modelsBackground;
import android.content.ContentValues;
import android.database.Cursor;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 匡申升
 * 听力判断题
 * 根据音频判断图片正误
 */

public class TestHearTOFItem extends TestItem implements Readable{

    private String mId;
    private boolean mTof;
    private String mRelationCharacterId;
    private String mPicture;
    private String mPronunciation;
    /**
     * 根据一个JSON对象生成一个听力判断题实例。*/
    public TestHearTOFItem(JSONObject json)throws JSONException{
        mId=json.getString("ID");
        mTof=(json.getInt("tof")==1);
        mRelationCharacterId=json.getString("relation_character_id");
        mPronunciation=json.getString("pronunciation");
        mPicture=json.getString("picture");
    }
    /**
     * @see Readable
     * 获取读音*/
    @Override
    public String getMediaKey() {
        return mPronunciation;
    }
    /**
     * 从一个数据库游标得到一个听力判断题实例。
     * @see Cursor,android.database.sqlite.SQLiteOpenHelper*/
    public TestHearTOFItem(Cursor cursor){
        mId=cursor.getString(cursor.getColumnIndex("ID"));
        mTof=(cursor.getInt(cursor.getColumnIndex("tof"))==1);
        mRelationCharacterId=cursor.getString(cursor.getColumnIndex("relation_character_id"));
        mPronunciation=cursor.getString(cursor.getColumnIndex("pronunciation"));
        mPicture=cursor.getString(cursor.getColumnIndex("picture"));
    }
    /**
     * @see TestItem*/
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
    /**
     * @see TestItem*/
    @Override
    public String getTestId() {
        return mId;
    }
    /**
     * @deprecated
     * 已经废除的方法，请用getCorrectAnswer代替。*/
    public boolean isTof() {
        return mTof;
    }

    /**
     * 获取和这道题有关的汉字的ID。*/
    public String getRelationCharacterId() {
        return mRelationCharacterId;
    }
    /**
     * 获取图片。*/
    public String getPicture() {
        return mPicture;
    }
    /**
     * 获取发音。*/
    public String getPronunciation() {
        return mPronunciation;
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
        return DataManager.HEAR_TOF;
    }
}
