package ecnu.chinesecharactercottage;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Shensheng on 2016/9/30.
 */

public class WordItem implements Readable{

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
        return null;
    }
}
