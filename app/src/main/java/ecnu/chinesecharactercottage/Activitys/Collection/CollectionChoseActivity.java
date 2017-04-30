package ecnu.chinesecharactercottage.Activitys.Collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import ecnu.chinesecharactercottage.Activitys.PictogramActivity;
import ecnu.chinesecharactercottage.Activitys.Test.TestCompleteActivity;
import ecnu.chinesecharactercottage.Activitys.Test.TestHearMatchActivity;
import ecnu.chinesecharactercottage.Activitys.Test.TestHearTOFActivity;
import ecnu.chinesecharactercottage.Activitys.Test.TestTOFActivity;
import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/4/30.
 */

public class CollectionChoseActivity extends Activity{
    //用于设置按键到屏幕中心
    private LinearLayout mButtons;
    //字收藏
    private Button mBtCharacter;
    //象形字收藏
    private Button mBtPictogram;
    //四种测试题收藏
    //听力选择
    private Button mListenMatch;
    //听力判断
    private Button mListenTOF;
    //填空
    private Button mComplete;
    //判断
    private Button mTOF;

    public static void startActivity(Context context){
        Intent intent=new Intent(context,CollectionChoseActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_chose);

        init();
        setButtons();
        setListener();
        
    }


    private void setButtons(){
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth=wm.getDefaultDisplay().getWidth();
        int screenHeight=wm.getDefaultDisplay().getHeight();
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) mButtons.getLayoutParams();
        int left=(int)(screenWidth/4.9);
        int top=(int)(screenHeight/3.05);
        linearParams.setMargins(left,top,0,0);
        linearParams.height=(int)(screenHeight/2.15);
        linearParams.width=(int)(screenWidth/1.648);
        mButtons.setLayoutParams(linearParams);
    }

    private void init(){
        mButtons=(LinearLayout)findViewById(R.id.homepageButtons);
        mBtCharacter=(Button)findViewById(R.id.bt_character);
        mBtPictogram =(Button)findViewById(R.id.bt_pictogram);
        mListenMatch=(Button)findViewById(R.id.bt_hear_match);
        mListenTOF=(Button)findViewById(R.id.bt_hear_tof);
        mComplete=(Button)findViewById(R.id.bt_complete);
        mTOF=(Button)findViewById(R.id.bt_tof);
    }
    
    private void setListener(){

        mBtCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask task= new AsyncTask(){
                    @Override
                    protected Object doInBackground(Object[] params) {
                        DataManager dataManager=DataManager.getInstance(CollectionChoseActivity.this);
                        String[] ids=dataManager.getCollectionCharsId(true);
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
                        String[] ids=dataManager.getCollectionCharsId(false);
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

        mTOF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestTOFActivity.startActivity(CollectionChoseActivity.this,TestTOFActivity.COLLECTION);
            }
        });

        mComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestCompleteActivity.startActivity(CollectionChoseActivity.this,TestCompleteActivity.COLLECTION);
            }
        });

        mListenTOF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestHearTOFActivity.startActivity(CollectionChoseActivity.this,TestHearTOFActivity.COLLECTION);
            }
        });

        mListenMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestHearMatchActivity.startActivity(CollectionChoseActivity.this,TestHearMatchActivity.COLLECTION);
            }
        });
    }
}
