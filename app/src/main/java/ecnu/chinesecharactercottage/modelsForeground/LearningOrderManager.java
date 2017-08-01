package ecnu.chinesecharactercottage.modelsForeground;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 10040 on 2017/7/29.
 */

public class LearningOrderManager {
    private static LearningOrderManager sLearningOrderManager;

    private SharedPreferences mSharedPreferences;

    public static LearningOrderManager getManager(Context context){
        if(sLearningOrderManager==null){
            sLearningOrderManager=new LearningOrderManager(context);
        }
        return sLearningOrderManager;
    }

    private LearningOrderManager(Context context){
        mSharedPreferences=context.getSharedPreferences("LearningOrder",0);
    }

    public void saveOrder(String key,int order){
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putInt(key,order);
        editor.apply();
    }

    public int getOrder(String key){
        return mSharedPreferences.getInt(key,1);
    }

}
