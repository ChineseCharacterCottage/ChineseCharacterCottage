package ecnu.chinesecharactercottage.modelsBackground;

import java.io.Serializable;

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
    public String getMediaKey(){
        return mSource;
    }
}
