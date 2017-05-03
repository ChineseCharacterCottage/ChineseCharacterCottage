package ecnu.chinesecharactercottage.modelsForeground;

import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by 10040 on 2017/5/3.
 */

public class MediaManager {

    //静态实例
    private static MediaManager sMediaManager;

    //当前播放的音频
    private MediaPlayer mNowMP;

    public static MediaManager getInstance(){
        if(sMediaManager==null){
            sMediaManager=new MediaManager();
        }
        return sMediaManager;
    }



    public void setMediaPlayer(MediaPlayer newMP){
        if(newMP!=null) {
            if(mNowMP!=null&&mNowMP.isPlaying())
                mNowMP.pause();
            mNowMP = newMP;
            //mNowMP.reset();
        }
    }

    public void prepareMediaPlayer() throws IOException {
        if(mNowMP!=null)
            mNowMP.prepare();
    }

    public void startMediaPlayer(){
        if(mNowMP!=null)
            mNowMP.start();
    }

    public void pauseMediaPlayer(){
        if(mNowMP!=null)
            mNowMP.pause();
    }
}
