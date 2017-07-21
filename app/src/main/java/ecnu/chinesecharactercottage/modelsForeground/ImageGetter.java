package ecnu.chinesecharactercottage.modelsForeground;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import ecnu.chinesecharactercottage.modelsBackground.PictureGetter;

/**
 * Created by 10040 on 2017/5/3.
 */

public class ImageGetter {

    private PictureGetter mPictureGetter;
    private String mId;
    private ImageView mImageView;

    public ImageGetter(Context context,String id, ImageView iv){
        mPictureGetter=new PictureGetter(context);
        mId=id;
        mImageView=iv;
    }

    public void setImage(){
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                return mPictureGetter.getPicture(mId);
            }

            @Override
            protected void onPostExecute(Object o) {
                mImageView.setImageBitmap((Bitmap)o);
            }
        };
        task.execute();
    }
}
