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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Shensheng on 2016/9/11.
 * A class to describe a character item.
 * It can only created by JSONObject.
 */
public class CharItem implements Readable{


    private static final String JSON_PY="拼音";
    private static final String JSON_SHAPE="汉字字形";
    private static final String JSON_NOTE="英文字意";
    private static final String JSON_ADDRESS="图片";
    private static final String JSON_DATE="date";
    private static final String JSON_BS="部首";
    private static final String JSON_ID="ID";
    private static final String JSON_STR="平面结构";
    private static final String JSON_MEDIA_ADDRESS="音频";
    private static final String JSON_WORDS="例词";
    private static final String JSON_SEN="例句";

    private String mShape;
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
    private String mMediaAddress;
    private WordItem[] mWords;
    private String mSens;
    private GregorianCalendar mLastAppearDate=new GregorianCalendar();

    private Bitmap mImage;

    private String mStruct;

    public String getSentence(){
        return mSens;
    }
    public String getShape(){
        return mShape;
    }

    public String getStruct(){
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
        mSens=json.getString(JSON_SEN);
        mShape=json.getString(JSON_SHAPE);
        mPinyin=json.getString(JSON_PY);
        mNotes=json.getString(JSON_NOTE);
        mLastAppearDate=new GregorianCalendar();
        mLastAppearDate.setTime(new Date(Long.valueOf(json.getString(JSON_DATE))));
        mSourceAddress=json.getString(JSON_ADDRESS);
        mRadical=json.getString(JSON_BS);
        mId=json.getString(JSON_ID);
        mStruct=json.getString(JSON_STR);
        mMediaAddress=json.getString(JSON_MEDIA_ADDRESS);
        ArrayList<WordItem> temList=new ArrayList<>();
        String[] words=json.getString(JSON_WORDS).split(",");
        for(int i=0;i<words.length;i+=2) {
            temList.add(new WordItem(words[i+1],words[i],null));
        }
        mWords=temList.toArray(new WordItem[temList.size()]);
        mImage=null;
    }

    public String getRadical() {
        return mRadical;
    }


    //Translate the object to JSON.
    public JSONObject toJSON()throws JSONException {
        JSONObject json=new JSONObject();
        json.put(JSON_SEN,mSens);
        json.put(JSON_SHAPE,mShape);
        json.put(JSON_ID,mId);
        json.put(JSON_ADDRESS,mSourceAddress);
        json.put(JSON_DATE,String.valueOf(mLastAppearDate.getTime().getTime()));
        json.put(JSON_PY,mPinyin);
        json.put(JSON_NOTE,mNotes);
        json.put(JSON_BS,mRadical);
        json.put(JSON_STR,mStruct);
        json.put(JSON_MEDIA_ADDRESS,mMediaAddress);
        StringBuffer buffer=new StringBuffer();
        for(int i=0;i<mWords.length;i++) {
            buffer.append(mWords[i].getChinese());
            buffer.append(",");
            buffer.append(mWords[i].getWord());
            if(i!=mWords.length-1){
                buffer.append(",");
            }
        }
        json.put(JSON_WORDS,buffer.toString());
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
        return pinyinTranslate(mPinyin);
    }

    @Override
    public MediaPlayer getMediaPlayer(Context c){
        return null;
    }

    private static String pinyinTranslate(String s){
        int d;
        char[] array;
        if(s.charAt(s.length()-1)<='4' && s.charAt(s.length()-1)>='0'){
            d=s.charAt(s.length()-1)-'0';
            array=new char[s.length()-1];
            for(int i=0;i<s.length()-1;++i){
                array[i]=s.charAt(i);
            }
        }
        else
        {
            array=s.toCharArray();
            d=0;
        }
        for(int i=0;i<array.length;++i){
            if(array[i]=='v'){
                if(s.contains("j") || s.contains("q") || s.contains("x")){
                    array[i]='u';
                }
                else
                {
                    array[i]='ü';
                }
            }
        }
        s=new String(array);
        int loc=-1;
        if(s.contains("a")){
            loc=s.indexOf('a');
        }
        else if(s.contains("o"))
        {
            loc=s.indexOf('o');
        }
        else if(s.contains("e"))
        {
            loc=s.indexOf('e');
        }
        else if(s.contains("i") && s.contains("u")){
            int a=s.indexOf('i');
            int b=s.indexOf('u');
            loc=a>b?a:b;
        }
        else if(s.contains("i")){
            loc=s.indexOf('i');
        }
        else if(s.contains("u")){
            loc=s.indexOf('u');
        }
        else if(s.contains("ü")){
            loc=s.indexOf('ü');
        }
        char[][] pylist=
                {
                        {'a','ā','á','ǎ','à'},
                        {'o','ō','ó','ǒ','ò'},
                        {'e','ē','é','ě','è'},
                        {'i','ī','í','ǐ','ì'},
                        {'u','ū','ú','ǔ','ù'},
                        {'ü','ǖ','ǘ','ǚ','ǜ'}
                };
        switch (array[loc]){
            case 'a':
                array[loc]=pylist[0][d];break;
            case 'o':
                array[loc]=pylist[1][d];break;
            case 'e':
                array[loc]=pylist[2][d];break;
            case 'i':
                array[loc]=pylist[3][d];break;
            case 'u':
                array[loc]=pylist[4][d];break;
            case 'ü':
                array[loc]=pylist[5][d];break;
        }
        return new String(array);
    }
}
