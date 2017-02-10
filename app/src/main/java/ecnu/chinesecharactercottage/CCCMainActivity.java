package ecnu.chinesecharactercottage;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

public class CCCMainActivity extends Activity {

    private SlidingLayout mSlidingLayout;
    private LinearLayout mainLayout;
    private LinearLayout mButtons;
    private Button mHskLeaning;
    private Button mRadicalLeaning;
    private Button mTest;
    private Button mReview;
    private CollectionLab mCollectionLab;

    private int HSKNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccc_main);

        init();
        setButtons();
        mSlidingLayout.setScrollEvent(mainLayout);
        setHSKLeaning();
        setRadicalLeaning();
        setReview();



    }

    private void init(){
        try{
            CharItemLab.getLab(CCCMainActivity.this);
            RadicalLab.getLab(CCCMainActivity.this);
            mCollectionLab=CollectionLab.getLab(CCCMainActivity.this);

        }
        catch (IOException exp){
            finish();
            return;
        }
        catch(JSONException exp){
            finish();
            return;
        }

        mainLayout=(LinearLayout)findViewById(R.id.mainLayout);
        mButtons=(LinearLayout)findViewById(R.id.homepageButtons);
        mSlidingLayout=(SlidingLayout)findViewById(R.id.slidingLayout);
        mHskLeaning = (Button) findViewById(R.id.HSKLeaning);
        mRadicalLeaning = (Button) findViewById(R.id.component_leaning);
        mReview=(Button)findViewById(R.id.review);

        HSKNumber=0;
    }

    private void setButtons(){
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth=wm.getDefaultDisplay().getWidth();
        int screenHeight=wm.getDefaultDisplay().getHeight();
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) mButtons.getLayoutParams();
        int left=(int)(screenWidth/4.9);
        int top=(int)(screenHeight/5.2);
        linearParams.setMargins(left,top,0,0);
        linearParams.height=(int)(screenHeight/2.15);
        linearParams.width=(int)(screenWidth/1.648);
        mButtons.setLayoutParams(linearParams);
    }

    private void setHSKLeaning(){
        final String[] charId=new String[20];
        for(int i=0;i<20;i++)
            charId[i]=String.valueOf(i+1);


        mHskLeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HSKNumber==20){
                    Toast.makeText(CCCMainActivity.this,"You have finish your goal today",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(CCCMainActivity.this, HSKActivity.class);
                    intent.putExtra("learned_number", HSKNumber);
                    intent.putExtra("char_id", charId);
                    startActivityForResult(intent, 1);
                    //HSKActivity.startHSKLeaning(CCCMainActivity.this);
                }
            }
        });
    }

    private void setRadicalLeaning(){
        mRadicalLeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("component_dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                ChoseComponentDialog myChoseComponentDialog= ChoseComponentDialog.getDialogInstance();
                myChoseComponentDialog.show(ft,"chose_dialog");
            }
        });
    }

    private void setReview(){
        mReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] charId=mCollectionLab.getCharItemIDs();
                if(charId==null||charId.length==0)
                    Toast.makeText(CCCMainActivity.this,"You have not mark any character",Toast.LENGTH_SHORT).show();
                else
                    ReviewActivity.starActivity(CCCMainActivity.this,charId);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    HSKNumber=data.getIntExtra("learned_number",0);
                }
        }
    }
}
