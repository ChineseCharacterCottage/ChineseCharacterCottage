package ecnu.chinesecharactercottage.modelsForeground;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author 胡家斌
 * 顺序管理器类，利用SharedPreferences存储key-value对来存储顺序，内置一些键值常量
 */

public class LearningOrderManager {
    //数据键值
    static public final String MAIN_LEARNING="main_learning";
    static public final String PICTOGRAM_LEARNING="pictogram_learning";
    static public final String TEST_HEAR_MATCH="test_hear_match";
    static public final String TEST_HEAR_TOF="test_hear_tof";
    static public final String TEST_COMPLETE="test_complete";
    static public final String TEST_TOF="test_tof";

    //保存的单例
    private static LearningOrderManager sLearningOrderManager;

    //SharedPreferences服务的对象
    private SharedPreferences mSharedPreferences;

    /**
     * 获取管理器的实例
     * @param context 传入的上下文，用于获取服务
     * @return 管理器的实例
     */
    public static LearningOrderManager getManager(Context context){
        if(sLearningOrderManager==null){
            //如果单例不存在则初始化。
            sLearningOrderManager=new LearningOrderManager(context);
        }
        return sLearningOrderManager;
    }

    private LearningOrderManager(Context context){
        //获取haredPreferences服务
        mSharedPreferences=context.getSharedPreferences("LearningOrder",0);
    }

    /**
     * 储存数据
     * @param key 存储键值
     * @param order 存储顺序值
     */
    public void saveOrder(String key,int order){
        SharedPreferences.Editor editor=mSharedPreferences.edit();//获取editor对象
        editor.putInt(key,order);//添加键值-顺序对
        editor.apply();//应用编辑器
    }

    /**
     * 储存数据
     * @param key 存储键值
     * @param order 存储顺序值
     */
    public void saveOrder(String key,String order){
        saveOrder(key,Integer.parseInt(order));//转换string到int
    }

    /**
     * 获取顺序
     * @param key 键值
     * @return 键值对应的顺序值
     */
    public int getOrder(String key){
        return mSharedPreferences.getInt(key,1);//获取对应键值的顺序值
    }

}
