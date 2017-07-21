package ecnu.chinesecharactercottage.activitys.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsBackground.TestItem;
import ecnu.chinesecharactercottage.modelsBackground.TestTOFItem;
import ecnu.chinesecharactercottage.modelsForeground.NextRunnable;
import ecnu.chinesecharactercottage.modelsForeground.testFragments.TestTOFFragment;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/2/28.
 */

public class TestTOFActivity extends Activity {

    //运行模式
    final static public int LEARNING=0;
    final static public int COLLECTION=1;
    static private int sModel;
    //题目页面
    private TestTOFFragment mTestFragment;
    //id列表
    private String[] mIds;
    //当前题目序号
    private int mNowIndex;
    //题目列表
    private TestTOFItem[] mTestTOFItems;

    static public void startActivity(Context context,int startId,int len){
        sModel=LEARNING;
        if(len<=0)
            return;
        String[] ids=new String[len];
        for(int i=0;i<len;i++)
            ids[i]=String.valueOf(startId+i);

        Intent intent=new Intent(context,TestTOFActivity.class);
        intent.putExtra("ids",ids);
        context.startActivity(intent);
    }

    static public void startActivity(Context context,int model){
        sModel=model;


        if(sModel==LEARNING){
            startActivity(context,1,10);
        }else if(sModel==COLLECTION){
            Intent intent=new Intent(context,TestTOFActivity.class);
            context.startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tof);
        init();

        //后台读取testTOFItem列表，完成后设置next()函数
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params){
                DataManager dataManager=DataManager.getInstance(TestTOFActivity.this);
                if(sModel==LEARNING){
                    mTestTOFItems=new TestTOFItem[mIds.length];
                    for(int i=0;i<mIds.length;i++){
                        mTestTOFItems[i]=(TestTOFItem)dataManager.getTestItemById(mIds[i],DataManager.TOF);
                    }
                }
                else if(sModel==COLLECTION) {
                    TestItem[] testItems=dataManager.getTestItemsCollection(DataManager.TOF);
                    mTestTOFItems=new TestTOFItem[testItems.length];
                    for(int i=0;i<testItems.length;i++){
                        mTestTOFItems[i]=(TestTOFItem)testItems[i];
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if (mTestTOFItems.length > 0) {
                    mTestFragment.setNext(new NextRunnable() {
                        @Override
                        public void next() {
                            if (mNowIndex < mTestTOFItems.length) {
                                if (mTestTOFItems[mNowIndex] != null) {
                                    mTestFragment.setTest(mTestTOFItems[mNowIndex]);
                                    mNowIndex++;
                                } else {
                                    mNowIndex++;
                                    next();
                                }
                            } else
                                finishTest();
                        }
                    });
                    mTestFragment.setTest(mTestTOFItems[mNowIndex]);
                    mNowIndex++;
                }else
                    finishTest();
            }
        };
        task.execute();
    }

    private void init(){
        mTestFragment=(TestTOFFragment)getFragmentManager().findFragmentById(R.id.test_tof_fragment);
        mIds=getIntent().getStringArrayExtra("ids");
        mNowIndex=0;
    }

    private void finishTest(){
        finish();
    }

}
