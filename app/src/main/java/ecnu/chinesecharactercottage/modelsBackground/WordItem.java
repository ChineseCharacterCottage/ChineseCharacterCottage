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
    /*
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
    }*/
}
