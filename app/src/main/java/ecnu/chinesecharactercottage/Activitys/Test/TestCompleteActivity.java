package ecnu.chinesecharactercottage.Activitys.Test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.ModelsBackground.TestFillItem;
import ecnu.chinesecharactercottage.ModelsForeground.NextRunnable;
import ecnu.chinesecharactercottage.ModelsForeground.TestFragments.TestCompleteFragment;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/2/28.
 */

public class TestCompleteActivity extends Activity {

    //题目页面
    private TestCompleteFragment mTestFragment;
    //id列表
    private String[] mIds;
    //当前题目序号
    private int mNowIndex;
    //题目列表
    private TestFillItem[] mTestFillItems;

    static public void startActivity(Context context, int startId, int len){
        if(len<=0)
            return;
        String[] ids=new String[len];
        for(int i=0;i<len;i++)
            ids[i]=String.valueOf(startId+i);

        Intent intent=new Intent(context,TestCompleteActivity.class);
        intent.putExtra("ids",ids);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_complete);
        init();

        //后台读取testFillItem列表，完成后设置next()函数
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params){
                DataManager dataManager=DataManager.getInstance(TestCompleteActivity.this);
                mTestFillItems=new TestFillItem[mIds.length];
                for(int i=0;i<mIds.length;i++){
                    mTestFillItems[i]=(TestFillItem)dataManager.getTestItemById(mIds[i],DataManager.FILL);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                mTestFragment.setNext(new NextRunnable() {
                    @Override
                    public void next() {
                        if(mNowIndex<mTestFillItems.length) {
                            if(mTestFillItems[mNowIndex]!=null) {
                                mTestFragment.setTest(mTestFillItems[mNowIndex]);
                                mNowIndex++;
                            }else{
                                mNowIndex++;
                                next();
                            }
                        }else
                            finishTest();
                    }
                });
                mTestFragment.setTest(mTestFillItems[mNowIndex]);
                mNowIndex++;
            }
        };
        task.execute();
    }

    private void init(){
        mTestFragment=(TestCompleteFragment)getFragmentManager().findFragmentById(R.id.test_complete_fragment);
        mIds=getIntent().getStringArrayExtra("ids");
        mNowIndex=0;
    }

    private void finishTest(){
        finish();
    }

}
