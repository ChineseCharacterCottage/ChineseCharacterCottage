package ecnu.chinesecharactercottage.modelsBackground;

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
 * 用于获取声音的类。
 * @author 匡申升
 * @see WeakHashMapCache
 */

public class SoundGetter extends WeakHashMapCache<String,MediaPlayer>{

    /**
     * 服务器的HOST地址*/
    private static final String HOST = "http://115.159.147.198/files/";
    /**
     * 应用上下文，用于读取缓存文件。*/
    private Context mContext;
    public SoundGetter(Context context){
        mContext = context.getApplicationContext();
    }
    /**
     * 获取一个MediaPlayer实例。 */
    public MediaPlayer getMediaPlayer(Readable readable){
        return getObject(readable.getMediaKey());
    }

    /**
     * 保护方法。从磁盘上获取一个MediaPlayer对象。
     * @param key 键
     * @return 键对应的MediaPlayer对象。*/
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
    /**
     * 从远程服务器获取一个MediaPlayer对象。
     * @param key 键
     * @return 键对应的MediaPlayer对象。*/
    @Override
    protected MediaPlayer getObjectFromWebServer(String key) {
        try {
            URL url = new URL(HOST + key);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            if(conn.getResponseCode()!=200)return null;
            //将下载完成的媒体文件存到磁盘中。
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
    /**
     * 这里不必实现这个方法，因为在音频从远程服务器加载下来之后，就已经存到磁盘上了。
     * @see ktool.ThreeLevelCache */
    @Override
    protected void restoreObjectToDisk(String key, MediaPlayer value) {
        //Auto.
    }
    /**
     * 私有方法，将输入流转换为字节数组。
     * @param input 输入流
     * @return 字节数组
     * @throws Exception 当输入流读取出现错误时，抛出。*/
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