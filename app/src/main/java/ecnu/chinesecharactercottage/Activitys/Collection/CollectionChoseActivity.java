package ecnu.chinesecharactercottage.activitys.collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import ecnu.chinesecharactercottage.activitys.character.PictogramActivity;
import ecnu.chinesecharactercottage.activitys.test.TestCompleteActivity;
import ecnu.chinesecharactercottage.activitys.test.TestHearMatchActivity;
import ecnu.chinesecharactercottage.activitys.test.TestHearTOFActivity;
import ecnu.chinesecharactercottage.activitys.test.TestTOFActivity;
import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsForeground.ChoseItem;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;
import ecnu.chinesecharactercottage.modelsForeground.testFragments.TestChoseListenDialog;
import ecnu.chinesecharactercottage.modelsForeground.testFragments.TestChoseReadDialog;

/**
 * Created by 10040 on 2017/4/30.
 */

public class CollectionChoseActivity extends Activity{
    //字收藏
    @InjectView(id=R.id.bt_character)
    private ChoseItem mBtCharacter;
    //象形字收藏
    @InjectView(id=R.id.bt_pictogram)
    private ChoseItem mBtPictogram;
    //测试题收藏
    //听力
    @InjectView(id=R.id.bt_listen)
    private ChoseItem mListen;
    //阅读
    @InjectView(id=R.id.bt_read)
    private ChoseItem mRead;

    public static void startActivity(Context context){
        Intent intent=new Intent(context,CollectionChoseActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_chose);
        Injecter.autoInjectAllField(this);

        setListener();
        
    }
    
    private void setListener(){

        mBtCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask task= new AsyncTask(){
                    @Override
                    protected Object doInBackground(Object[] params) {
                        DataManager dataManager=DataManager.getInstance(CollectionChoseActivity.this);
                        String[] ids=dataManager.getCollectionCharsId(false);
                        int[] ids2=new int[ids.length];
                        for (int i=0;i<ids.length;i++)
                            ids2[i]=Integer.parseInt(ids[i]);
                        return ids2;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        int[] ids=(int[])o;
                        CharacterActivity.startActivity(CollectionChoseActivity.this,ids);

                    }
                };
                task.execute();
            }
        });

        mBtPictogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask task= new AsyncTask(){
                    @Override
                    protected Object doInBackground(Object[] params) {
                        DataManager dataManager=DataManager.getInstance(CollectionChoseActivity.this);
                        String[] ids=dataManager.getCollectionCharsId(true);
                        return ids;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        String[] ids=(String[])o;
                        PictogramActivity.startActivity(CollectionChoseActivity.this,ids);

                    }
                };
                task.execute();
            }
        });


        mListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestChoseListenDialog(CollectionChoseActivity.this).show();
            }
        });

        mRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestChoseReadDialog(CollectionChoseActivity.this).show();

            }
        });
    }
}
