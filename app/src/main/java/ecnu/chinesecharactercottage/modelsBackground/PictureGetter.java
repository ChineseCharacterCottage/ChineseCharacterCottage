package ecnu.chinesecharactercottage.modelsBackground;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ktool.*;

/**
 * @author 匡申升
 * 图片获取器。用于从本地缓存或者远程服务器获取一个图片实例。
 * @see ThreeLevelCache
 */

public class PictureGetter extends WeakHashMapCache<String,Bitmap>{
    //服务器的HOST
    private static final String HOST = "http://115.159.147.198/files/";
    //应用的上下文
    private Context mContext;
    //根据应用上下文，创建一个图片获取器实例。
    public PictureGetter(Context context){
        mContext = context.getApplicationContext();
    }
    //根据ID获取图片。
    public Bitmap getPicture(String id){
        return getObject(id);
    }
    //从磁盘上获取图片。
    @Override
    protected Bitmap getObjectFromDisk(String key) {
        try{
            FileInputStream fis = mContext.openFileInput("cache_"+key);
            Bitmap bm = BitmapFactory.decodeStream(fis);
            fis.close();
            return bm;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    //从远程服务器获取图片
    @Override
    protected Bitmap getObjectFromWebServer(String key) {
        try {
            URL url = new URL(HOST + key);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            if(conn.getResponseCode() != 200)return null;
            InputStream is = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //在磁盘上获取图片
    @Override
    protected void restoreObjectToDisk(String key, Bitmap value) {
        try {
            FileOutputStream fos = mContext.openFileOutput("cache_"+key,Context.MODE_PRIVATE);
            value.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
