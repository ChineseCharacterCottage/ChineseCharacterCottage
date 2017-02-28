package ecnu.chinesecharactercottage.ModelsForeground;

import android.os.AsyncTask;

import ecnu.chinesecharactercottage.ModelsBackground.CharItem;

/**
 * Created by 10040 on 2017/2/28.
 */

public class DataManager extends AsyncTask<String,Integer,Object>{

    //各种模式
    public static final String CHARACTER="character";//获取字
    public static final String CHARACTERS="characters";//获取字数组
    public static final String RADICAL="radical";//获取部首
    public static final String TEST_HEAR_TOF="test_hear_tof";//听力判读题
    public static final String TEST_HEAR_MATCH="test_hear_match";//听力选择题
    public static final String TEST_TOF="test_tof";//判断题
    public static final String TEST_COMPLETE="test_complete";//填空题

    @Override
    protected Object doInBackground(String... params) {
        String model=params[0];
        Object result=null;
        switch (model){
            case CHARACTER:
                result=getCharItem();
                break;
            case RADICAL:
                break;
            case TEST_HEAR_TOF:
                break;
            case TEST_HEAR_MATCH:
                break;
            case TEST_TOF:
                break;
            case TEST_COMPLETE:
                break;
            default:
                result=null;
        }
        return result;
    }

    private CharItem getCharItem(){
        ecnu.chinesecharactercottage.ModelsBackground.DataManager backgroundDM;
        backgroundDM=ecnu.chinesecharactercottage.ModelsBackground.DataManager.getInstance(null);
        return null;
    }
}
