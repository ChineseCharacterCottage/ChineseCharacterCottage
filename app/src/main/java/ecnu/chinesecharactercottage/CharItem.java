package ecnu.chinesecharactercottage;



import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Log;

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

public class CharItem implements Readable {
    private static final String ID="ID";
    public static final String CHARACTER="character";
    public static final String PINYIN="pinyin";
    private static final String WORDS="words";
    public static final String SENTENCE="sentence";
    public static final String EXPLANATION="explanation";
    private static final String RADICAL="radical_id";

    private JSONObject mJSON;
    private String mId;
    public CharItem(JSONObject json) {
        mJSON=json;
        mId=get(ID);
    }

    public String getId(){
        return mId;
    }
    public String get(String property) {
        String r=null;
        try {
            r=mJSON.getString(property);
        }
        catch (JSONException e){
            Log.d("CharItem",e.toString());
        }
        if(property.equals(PINYIN)){
            return pinyinTranslate(r);
        }
        return r;
    }

    public WordItem[] getWords(){
        String[] words=get(WORDS).split(",");
        ArrayList<WordItem> list=new ArrayList<>();
        for(int i=0;i<words.length;i+=2) {
            list.add(new WordItem(words[1],words[0],"w_"+get("ID")+i/2+1+".wav"));
        }
        return list.toArray(new WordItem[list.size()]);
    }
    public JSONObject toJSON(){
        try {
            return new JSONObject(mJSON.toString());
        }catch (Exception e) {
            Log.d("CharItem",e.toString());
        }
        return null;
    }
    @Override
    public MediaPlayer getMediaPlayer(Context c){
        return null;
    }
    public MediaPlayer getSentenceMediaPlayer(Context c){
        return null;
    }
    public Bitmap getImage(Context context){
        Context appContext=context.getApplicationContext();
        AssetManager manager=appContext.getAssets();
        Bitmap image;
        try {
            InputStream stream = manager.open(get("ID")+".jpg");
            image = BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            image = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.imagenotfound);
        }
        return image;
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
