package ecnu.chinesecharactercottage;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Shensheng on 2016/9/30.
 */

public class WordItem implements Readable{

    private String mWord;
    private String mSource;
    public WordItem(String word,String source){
        mWord=word;
        mSource=source;
    }

    public String getWord(){
        return mWord;
    }
    @Override
    public MediaPlayer getMediaPlayer(Context c){
        return null;
    }
}
