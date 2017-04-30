package ecnu.chinesecharactercottage.Activitys.Test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.ModelsBackground.TestHearTOFItem;
import ecnu.chinesecharactercottage.ModelsBackground.TestItem;
import ecnu.chinesecharactercottage.ModelsForeground.NextRunnable;
import ecnu.chinesecharactercottage.ModelsForeground.TestFragments.TestHearTOFFragment;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/2/28.
 */

public class TestHearTOFActivity extends Activity {

    //运行模式
    final static public int LEARNING=0;
    final static public int COLLECTION=1;
    static private int sModel;
    //题目页面
    private TestHearTOFFragment mTestFragment;
    //id列表
    private String[] mIds;
    //当前题目序号
    private int mNowIndex;
    //题目列表
    private TestHearTOFItem[] mTestHearTOFItems;

    static public void startActivity(Context context, int startId, int len){
        sModel=LEARNING;
        if(len<=0)
            return;
        String[] ids=new String[len];
        for(int i=0;i<len;i++)
            ids[i]=String.valueOf(startId+i);

        Intent intent=new Intent(context,TestHearTOFActivity.class);
        intent.putExtra("ids",ids);
        context.startActivity(intent);
    }

    static public void startActivity(Context context,int model){
        sModel=model;


        if(sModel==LEARNING){
            startActivity(context,1,10);
        }else if(sModel==COLLECTION){
            Intent intent=new Intent(context,TestHearTOFActivity.class);
            context.startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_hear_tof);
        init();

        //后台读取testTOFItem列表，完成后设置next()函数
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params){
                DataManager dataManager=DataManager.getInstance(TestHearTOFActivity.this);
                if(sModel==LEARNING) {
                    mTestHearTOFItems=new TestHearTOFItem[mIds.length];
                    for (int i = 0; i < mIds.length; i++) {
                        mTestHearTOFItems[i] = (TestHearTOFItem) dataManager.getTestItemById(mIds[i], DataManager.HEAR_TOF);
                    }
                }
                else if(sModel==COLLECTION) {
                    TestItem[] testItems=dataManager.getTestItemsCollection(DataManager.HEAR_TOF);
                    mTestHearTOFItems=new TestHearTOFItem[testItems.length];
                    for(int i=0;i<testItems.length;i++){
                        mTestHearTOFItems[i]=(TestHearTOFItem)testItems[i];
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if (mTestHearTOFItems.length > 0) {
                    mTestFragment.setNext(new NextRunnable() {
                        @Override
                        public void next() {
                            if (mNowIndex < mTestHearTOFItems.length) {
                                if (mTestHearTOFItems[mNowIndex] != null) {
                                    mTestFragment.setTest(mTestHearTOFItems[mNowIndex]);
                                    mNowIndex++;
                                } else {
                                    mNowIndex++;
                                    next();
                                }
                            } else
                                finishTest();
                        }
                    });
                    mTestFragment.setTest(mTestHearTOFItems[mNowIndex]);
                    mNowIndex++;
                }else
                    finishTest();
            }
        };
        task.execute();
    }

    private void init(){
        mTestFragment=(TestHearTOFFragment)getFragmentManager().findFragmentById(R.id.test_hear_tof_fragment);
        mIds=getIntent().getStringArrayExtra("ids");
        mNowIndex=0;
    }

    private void finishTest(){
        finish();
    }

}
