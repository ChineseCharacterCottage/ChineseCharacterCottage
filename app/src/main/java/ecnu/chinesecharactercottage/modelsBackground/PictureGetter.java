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
 * Created by Shensheng on 2017/5/2.
 * 图片获取器
 */

public class PictureGetter extends WeakHashMapCache<String,Bitmap>{

    private static final String HOST = "http://115.159.147.198/files/";
    private Context mContext;

    public PictureGetter(Context context){
        mContext = context.getApplicationContext();
    }

    public Bitmap getPicture(String id){
        return getObject(id);
    }

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
