package ecnu.chinesecharactercottage.modelsForeground;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;

import java.io.IOException;

import ecnu.chinesecharactercottage.ModelsBackground.Readable;
import ecnu.chinesecharactercottage.ModelsBackground.SoundGetter;

/**
 * Created by 10040 on 2017/5/3.
 */

public class MPGetter {

    private SoundGetter mSoundGetter;
    private Readable mReadable;
    private View mClickableView;

    public MPGetter(Context context, Readable readable, View view){
        mSoundGetter=new SoundGetter(context);
        mReadable=readable;
        mClickableView=view;

        //在下载完成前设置为不可点击
        mClickableView.setEnabled(false);
    }

    public void setMP() {
        AsyncTask task=new AsyncTask() {
            MediaPlayer mMediaPlayer;
            @Override
            protected Object doInBackground(Object[] params) {
                mMediaPlayer= mSoundGetter.getMediaPlayer(mReadable);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                mClickableView.setOnClickListener(new View.OnClickListener() {
                        @Override
                    public void onClick(View v) {
                        MediaManager mm=MediaManager.getInstance();
                        mm.setMediaPlayer(mMediaPlayer);
                        mm.startMediaPlayer();
                    }
                });
                mClickableView.setEnabled(true);
            }
        };
        task.execute();
    }
}
