package ecnu.chinesecharactercottage.ModelsBackground;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Handler;

import ktool.WeakHashMapCache;

/**
 * Created by Shensheng on 2017/5/3.
 * 获取声音
 */

public class SoundGetter extends WeakHashMapCache<String,MediaPlayer>{

    private static final String HOST = "http://115.159.147.198/files/";
    private Context mContext;
    public SoundGetter(Context context){
        mContext = context.getApplicationContext();
    }
    public MediaPlayer getMediaPlayer(Readable readable){
        return getObject(readable.getMediaKey());
    }

    @Override
    protected MediaPlayer getObjectFromDisk(String key) {
        MediaPlayer mp;
        File file = mContext.getFileStreamPath("cache_"+key);
        try {
            FileInputStream fis = mContext.openFileInput("cache_"+key);
            byte[] bytes = transformInputStream(fis);
            mp = createMediaPlayer(bytes);
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return mp;
    }

    private MediaPlayer getMediaFromDisk(String key){
        return getObjectFromDisk(key);
    }
    private static MediaPlayer createMediaPlayer(byte[] mediaBytes) {
        MediaPlayer mediaplayer = null;
        try {
            File temp = File.createTempFile("cache_","mp3");
            FileOutputStream fos = new FileOutputStream(temp);
            fos.write(mediaBytes);
            fos.flush();
            fos.close();
            FileInputStream fis = new FileInputStream(temp);
            mediaplayer = new MediaPlayer();
            mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            FileDescriptor fd = fis.getFD();
            mediaplayer.setDataSource(fd);
            mediaplayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaplayer;
    }
    @Override
    protected MediaPlayer getObjectFromWebServer(String key) {
        try {
            URL url = new URL(HOST + key);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            if(conn.getResponseCode()!=200)return null;
            InputStream is = conn.getInputStream();
            FileOutputStream fos = mContext.openFileOutput("cache_"+key,Context.MODE_PRIVATE);
            byte[] bytes = transformInputStream(is);
            fos.write(bytes);
            fos.close();
            is.close();
            return createMediaPlayer(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d(this.getClass().getSimpleName(),"xxxxxxxxxxxx");
        return null;
    }

    @Override
    protected void restoreObjectToDisk(String key, MediaPlayer value) {
        //Auto.
    }

    private static byte[] transformInputStream(InputStream input)throws Exception
    {
        byte[] byt;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        b = input.read();
        while( b != -1)
        {
            baos.write(b);
            b = input.read();
        }
        byt = baos.toByteArray();
        return byt;
    }
}
