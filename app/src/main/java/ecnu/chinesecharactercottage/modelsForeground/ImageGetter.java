package ecnu.chinesecharactercottage.modelsForeground;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import ecnu.chinesecharactercottage.modelsBackground.PictureGetter;

/**
 * @author 胡家斌
 * 这个类用来获取图片，将开线程获取数据、设置图片控件封装起来，便于使用。
 */

public class ImageGetter {
    //图片获取器
    private PictureGetter mPictureGetter;
    //图片id
    private String mId;
    //图片控件
    private ImageView mImageView;

    /**
     * 最关键的构造方法，需要传入上下文来获取服务，还有图片id，以及需要设置的图片控件
     * @param context 上下文
     * @param id 图片id
     * @param iv 需要设置的图片控件
     */
    public ImageGetter(Context context,String id, ImageView iv){
        mPictureGetter=new PictureGetter(context);
        mId=id;
        mImageView=iv;
        mImageView.setEnabled(false);
    }

    /**
     * 调用后即会开线程获取数据并且设置图片控件
     */
    public void setImage(){
        //创建一个异步对象
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                return mPictureGetter.getPicture(mId);//获取图片Bitmap
            }

            @Override
            protected void onPostExecute(Object o) {
                mImageView.setImageBitmap((Bitmap)o);//设置图片控件
            }
        };
        task.execute();//执行异步对象
    }
}
