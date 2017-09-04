package ecnu.chinesecharactercottage.modelsBackground;

import java.io.Serializable;

/**
 * @author 匡申升
 * @see Readable,CharItem,DataManager
 * 汉字的组词实例，带有这个词语的读音。
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
    /**
     * 获取这个词语的英文释义。*/
    public String getWord(){
        return mWord;
    }
    /**
     * 获取这个词语。*/
    public String getChinese(){
        return mChinese;
    }

    /**
     * 获取这个词语的读音。
     * @see Readable*/
    @Override
    public String getMediaKey(){
        return mSource;
    }
}
