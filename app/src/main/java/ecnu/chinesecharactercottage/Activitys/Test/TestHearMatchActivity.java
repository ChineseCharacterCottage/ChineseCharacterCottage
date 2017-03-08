package ecnu.chinesecharactercottage.Activitys.Test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.ModelsBackground.TestHearChoiceItem;
import ecnu.chinesecharactercottage.ModelsForeground.NextRunnable;
import ecnu.chinesecharactercottage.ModelsForeground.TestFragments.TestHearMatchFragment;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/2/28.
 */

public class TestHearMatchActivity extends Activity {

    //题目页面
    private TestHearMatchFragment mTestFragment;
    //id列表
    private String[] mIds;
    //当前题目序号
    private int mNowIndex;
    //题目列表
    private TestHearChoiceItem[] mTestHearChoiceItems;

    static public void startActivity(Context context, int startId, int len){
        if(len<=0)
            return;
        String[] ids=new String[len];
        for(int i=0;i<len;i++)
            ids[i]=String.valueOf(startId+i);

        Intent intent=new Intent(context,TestHearMatchActivity.class);
        intent.putExtra("ids",ids);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_hear_match);
        init();

        //后台读取testTOFItem列表，完成后设置next()函数
        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params){
                DataManager dataManager=DataManager.getInstance(TestHearMatchActivity.this);
                mTestHearChoiceItems=new TestHearChoiceItem[mIds.length];
                for(int i=0;i<mIds.length;i++){
                    mTestHearChoiceItems[i]=(TestHearChoiceItem)dataManager.getTestItemById(mIds[i],DataManager.HEAR_CHOICE);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                mTestFragment.setNext(new NextRunnable() {
                    @Override
                    public void next() {
                        if(mNowIndex<=mTestHearChoiceItems.length) {
                            mTestFragment.setTest(mTestHearChoiceItems[mNowIndex]);
                            mNowIndex++;
                        }else
                            finishTest();
                    }
                });
            }
        };
        task.execute();
    }

    private void init(){
        mTestFragment=(TestHearMatchFragment)getFragmentManager().findFragmentById(R.id.test_hear_match_fragment);
        mIds=getIntent().getStringArrayExtra("ids");
        mNowIndex=0;
    }

    private void finishTest(){
        finish();
    }

}
