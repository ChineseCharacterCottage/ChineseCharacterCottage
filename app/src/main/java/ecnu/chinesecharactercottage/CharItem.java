package ecnu.chinesecharactercottage;



import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Shensheng on 2016/9/11.
 * A class to describe a character item.
 * It can only created by JSONObject.
 */

public class CharItem implements Readable,Serializable {
    private static final String ID="ID";
    public static final String CHARACTER="character";
    public static final String PINYIN="pinyin";
    private static final String WORDS="words";
    public static final String SENTENCE="sentence";
    public static final String EXPLANATION="explanation";
    private static final String RADICAL="radical_id";

    private JSONObject mJSON;
    private String mId;
    private WordItem[] mWords;
    public CharItem(JSONObject json) {
        mWords=null;
        mJSON=json;
        mId=get(ID);
    }
    //调用getRadical之前，一定要先调用RadicalLab.getLab(Context)来生成静态实例！切记。
    public RadicalItem getRadical(){
        try {
            return RadicalLab.getLabWithoutContext().getRadical(get(RADICAL));
        }catch (Exception e){
            Log.d("CharItem",e.toString());
        }
        return null;
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
        if(mWords==null) {
            String[] words = get(WORDS).split("/|,");
            ArrayList<WordItem> list = new ArrayList<>();
            for (int i = 0; i < words.length; i += 2) {
                list.add(new WordItem(words[i+1], words[i], "w_" + get("ID") +"_"+ i / 2  + ".mp3"));
            }
            mWords=list.toArray(new WordItem[list.size()]);
        }
        return mWords;
    }

    public JSONObject toJSON(){
        return mJSON;
    }
    @Override
    public MediaPlayer getMediaPlayer(Context c) {
        MediaPlayer mp=new MediaPlayer();
        try {
            AssetFileDescriptor fd=c.getAssets().openFd(mJSON.getString(PINYIN)+".mp3");
            if(Build.VERSION.SDK_INT<24) {
                mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            }else {
                mp.setDataSource(c.getAssets().openFd(mJSON.getString(PINYIN)+".mp3"));
            }
        }catch (IOException e){
            Log.d("CharItem","Media file not found :"+e.toString());
            return null;
        }catch (JSONException j){
            Log.d("CharItem",j.toString());
        }
        return mp;
    }
    public Readable getSentenceReadable(Context c){
        Readable readable=new Readable() {
            @Override
            public MediaPlayer getMediaPlayer(Context c) {
                MediaPlayer mp=new MediaPlayer();
                try {
                    AssetFileDescriptor fd=c.getAssets().openFd("s_"+get("ID")+".mp3");
                    if(Build.VERSION.SDK_INT<24) {
                        mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                    }else {
                        mp.setDataSource(c.getAssets().openFd("s_"+get("ID")+".mp3"));
                    }
                }catch (IOException e){
                    Log.d("WordItem","Media file not found :"+e.toString());
                    return null;
                }
                return mp;
            }
        };
        return readable;
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
    @Override
    public String toString(){
        return get(ID)+" "+get(PINYIN);
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
