package ecnu.chinesecharactercottage.ModelsBackground;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import ktool.WeakHashMapCache;

/**
 * Created by Shensheng on 2017/5/3.
 * 获取声音
 */

public class SoundGetter{

    private static final String HOST = "http://115.159.147.198/files/";
    private Context mContext;
    public SoundGetter(Context context){
        mContext = context.getApplicationContext();
    }
    public MediaPlayer getMediaPlayer(Readable readable){
        MediaPlayer mp=new MediaPlayer();
        try {
            AssetFileDescriptor fd=mContext.getAssets().openFd(readable.getMediaKey());
            if(Build.VERSION.SDK_INT<24) {
                mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            }else {
                mp.setDataSource(mContext.getAssets().openFd(readable.getMediaKey()));
            }
            mp.prepare();
        }catch (IOException e){
            return null;
        }
        return mp;
    }

}
