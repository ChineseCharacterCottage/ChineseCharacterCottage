package ecnu.chinesecharactercottage.ModelsBackground;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
        MediaPlayer mp=new MediaPlayer();
        File file = mContext.getFileStreamPath("cache_"+key);
        try {
            mp.setDataSource(file.getAbsolutePath());
            mp.prepare();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return mp;
    }

    private MediaPlayer getMediaFromDisk(String key){
        return getObjectFromDisk(key);
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
            fos.write(transformInputStream(is));
            fos.close();
            is.close();
            return getMediaFromDisk(key);
        }catch (Exception e){
            e.printStackTrace();
        }
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
