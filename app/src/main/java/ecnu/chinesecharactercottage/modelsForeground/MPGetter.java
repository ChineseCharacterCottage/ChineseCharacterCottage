package ecnu.chinesecharactercottage.modelsForeground;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;

import ecnu.chinesecharactercottage.modelsBackground.Readable;
import ecnu.chinesecharactercottage.modelsBackground.SoundGetter;

/**
 * @author 胡家斌
 * 这个类用来获取音频，将开线程获取数据、设置播放按键控件封装起来，便于使用。
 */

public class MPGetter {
    //音频获取器
    private SoundGetter mSoundGetter;
    //通过这个接口可以获取到音频的信息
    private Readable mReadable;
    //点击播放的视图控件
    private View mClickableView;

    public MPGetter(Context context, Readable readable, View view){
        mSoundGetter=new SoundGetter(context);//创建音频获取器
        mReadable=readable;//保存音频信息
        mClickableView=view;

        //在下载完成前设置为不可点击
        mClickableView.setEnabled(false);
    }

    /**
     * 设置音频数据
     * @param isPlay 是否需要立即播放一次，为true时立即播放一次
     */
    public void setMP(final boolean isPlay) {
        AsyncTask task=new AsyncTask() {
            MediaPlayer mMediaPlayer;
            @Override
            protected Object doInBackground(Object[] params) {
                //利用Readable从音频获取器获取音频
                mMediaPlayer= mSoundGetter.getMediaPlayer(mReadable);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                mClickableView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MediaManager mm=MediaManager.getInstance();//获取音频管理器实例
                        mm.setMediaPlayer(mMediaPlayer);//设置音频
                        mm.startMediaPlayer();//播放音频
                    }
                });
                if(isPlay){//如果希望设置完直接播放一次
                    MediaManager mm=MediaManager.getInstance();
                    mm.setMediaPlayer(mMediaPlayer);
                    mm.startMediaPlayer();
                }
                mClickableView.setEnabled(true);
            }
        };
        task.execute();
    }

    public void setMP() {
        setMP(false);//默认不播放音频
    }
}
