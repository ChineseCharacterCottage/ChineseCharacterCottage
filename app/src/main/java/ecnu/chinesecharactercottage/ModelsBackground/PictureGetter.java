package ecnu.chinesecharactercottage.ModelsBackground;

import android.content.Context;
import android.graphics.Bitmap;

import ktool.*;

/**
 * Created by Shensheng on 2017/5/2.
 * 图片获取器
 */

public class PictureGetter extends WeakHashMapCache<String,Bitmap>{

    private Context mContext;
    private double mCpmpressRate;

    public PictureGetter(Context context){
        mContext = context.getApplicationContext();
    }

    public Bitmap getPicture(String url){
        return getObject(url);
    }

    @Override
    protected Bitmap getObjectFromDisk(String key) {
        return null;
    }

    @Override
    protected Bitmap getObjectFromWebServer(String key) {
        return null;
    }

    @Override
    protected void restoreObjectToDisk(String key, Bitmap value) {

    }

    @Override
    public void cleanDiskCache() {

    }

    @Override
    public long getDiskCacheSize() {
        return 0;
    }
}
