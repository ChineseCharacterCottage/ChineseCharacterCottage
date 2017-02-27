package ecnu.chinesecharactercottage.ModelsBackground;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;

import ecnu.chinesecharactercottage.ModelsBackground.Readable;

/**
 * Created by Shensheng on 2016/9/30.
 */

public class WordItem implements Readable,Serializable {

    private String mWord;
    private String mChinese;
    private String mSource;

    public WordItem(String word,String chinese,String source){
        mChinese=chinese;
        mWord=word;
        mSource=source;
    }

    public String getWord(){
        return mWord;
    }
    public String getChinese(){
        return mChinese;
    }
    @Override
    public MediaPlayer getMediaPlayer(Context c) {
        MediaPlayer mp=new MediaPlayer();
        try {
            AssetFileDescriptor fd=c.getAssets().openFd(mSource);
            if(Build.VERSION.SDK_INT<24) {
                mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            } else {
                mp.setDataSource(c.getAssets().openFd(mSource));
            }
        }catch (IOException e){
            Log.d("WordItem","Media file not found :"+e.toString());
            return null;
        }
        return mp;
    }
}
