package ecnu.chinesecharactercottage.modelsForeground;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * @author 胡家斌
 * 音频播放管理器，保证每次只有一个音频发音
 */

public class MediaManager {

    //静态实例
    private static MediaManager sMediaManager;

    //当前播放的音频
    private MediaPlayer mNowMP;

    /**
     * 获取管理器单例
     * @return 单例
     */
    public static MediaManager getInstance(){
        if(sMediaManager==null){
            sMediaManager=new MediaManager();
        }
        return sMediaManager;
    }


    /**
     * 设置当前音频为传入的音频
     * @param newMP 希望播放的音频
     */
    public void setMediaPlayer(MediaPlayer newMP){
        if(newMP!=null) {
            if(mNowMP!=null&&mNowMP.isPlaying()) {//判断当前音频存在且正在播放
                mNowMP.pause();//暂停音频
            }
            mNowMP = newMP;//设置新的音频
        }
    }

    public void prepareMediaPlayer() throws IOException {
        if(mNowMP!=null)
            mNowMP.prepare();
    }

    /**
     * 播放当前音频
     */
    public void startMediaPlayer(){
        if(mNowMP!=null) {
            mNowMP.seekTo(0);//重置播放到初始位置
            mNowMP.start();//播放当前音频
        }
    }

    public void pauseMediaPlayer(){
        if(mNowMP!=null)
            mNowMP.pause();
    }
}
