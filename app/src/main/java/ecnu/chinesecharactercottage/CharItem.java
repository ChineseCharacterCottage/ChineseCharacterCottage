package ecnu.chinesecharactercottage;



import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Shensheng on 2016/9/11.
 * A class to describe a character item.
 * It can only created by JSONObject.
 */
public class CharItem implements Readable{


    public enum CharStruct{
        ALONE,
        UP_DOWN,
        LEFT_RIGHT,
        LEFT_MID_RIGHT,
        RIGHT_UP_AROUND,
        LEFT_UP_AROUND,
        LEFT_DOWN_AROUND,
        UP_3_AROUND,
        DOWN_3_AROUND,
        ALL_AROUND,
        SYMMETRY
    }
    private static final String JSON_PY="pinyin";
    private static final String JSON_NOTE="notes";
    private static final String JSON_ADDRESS="sourceAddress";
    private static final String JSON_DATE="date";
    private static final String JSON_BS="radical";
    private static final String JSON_ID="theId";
    private static final String JSON_STR="struct";
  //  private static final String JSON_WORDS="words";
    //Pinyin of this character.
    private String mPinyin;
    //Anything else about this character.
    private String mNotes;
    //The local address of the picture of the word.
    private String mSourceAddress;
    //To search the character.
    private String mId;
    //Last appear time of this character.
    private String mRadical;

    private WordItem[] mWords;

    private GregorianCalendar mLastAppearDate=new GregorianCalendar();

    private Bitmap mImage;

    private CharStruct mStruct;

    public CharStruct getStruct(){
        return mStruct;
    }

    public WordItem[] getWords(){
        return mWords;
    }

    public Bitmap getImage(Context context) {
        Context appContext=context.getApplicationContext();
        if (mImage == null) {
            AssetManager manager=appContext.getAssets();
            try {
                InputStream stream = manager.open(mSourceAddress);
                mImage = BitmapFactory.decodeStream(stream);
            } catch (IOException e) {
                mImage = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.imagenotfound);
            }
        }
        return mImage;
    }

    public Bitmap getImage(){
        return mImage;
    }

    //Create an CharItem object by JSON.
    public CharItem(JSONObject json) throws JSONException {
        mPinyin=json.getString(JSON_PY);
        mNotes=json.getString(JSON_NOTE);
        mLastAppearDate=new GregorianCalendar();
        mLastAppearDate.setTime(new Date(json.getLong(JSON_DATE)));
        mSourceAddress=json.getString(JSON_ADDRESS);
        mRadical=json.getString(JSON_BS);
        mId=json.getString(JSON_ID);
        mStruct=CharStruct.valueOf(json.getString(JSON_STR));
        mImage=null;
    }

    public String getRadical() {
        return mRadical;
    }


    //Translate the object to JSON.
    public JSONObject toJSON()throws JSONException {
        JSONObject json=new JSONObject();
        json.put(JSON_ID,mId);
        json.put(JSON_ADDRESS,mSourceAddress);
        json.put(JSON_DATE,mLastAppearDate.getTime().getTime());
        json.put(JSON_PY,mPinyin);
        json.put(JSON_NOTE,mNotes);
        json.put(JSON_BS,mRadical);
        json.put(JSON_STR,mStruct.toString());
        return json;
    }

    //Change last appear date to now.
    //调用这个函数用来更新日期到当前时间
    public void newAppear(){
        mLastAppearDate=new GregorianCalendar();
    }

    //Get a copy of the date.
    public Calendar getLastAppearDate(){
        if(mLastAppearDate==null)return null;
        GregorianCalendar r=new GregorianCalendar();
        r.setTime(mLastAppearDate.getTime());
        return r;
    }

    public String getId() {
        return mId;
    }


    public String getSourceAddress() {
        return mSourceAddress;
    }

    public String getNotes() {
        return mNotes;
    }


    public String getPinyin() {
        return mPinyin;
    }

    @Override
    public MediaPlayer getMediaPlayer(Context c){
        return null;
    }
}
