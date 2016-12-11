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

import org.json.JSONException;

import java.io.IOException;

public class CCCMainActivity extends Activity {

    private SlidingLayout slidingLayout;
    private LinearLayout mainLayout;
    private LinearLayout mButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccc_main);

        init();

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth=wm.getDefaultDisplay().getWidth();
        int screenHeight=wm.getDefaultDisplay().getHeight();
        mainLayout=(LinearLayout)findViewById(R.id.mainLayout);
        mButtons=(LinearLayout)findViewById(R.id.homepageButtons);
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) mButtons.getLayoutParams();
        int left=(int)(screenWidth/4.9);
        int top=(int)(screenHeight/5.2);
        linearParams.setMargins(left,top,0,0);
        linearParams.height=(int)(screenHeight/2.15);
        linearParams.width=(int)(screenWidth/1.648);
        mButtons.setLayoutParams(linearParams);

        slidingLayout=(SlidingLayout)findViewById(R.id.slidingLayout);
        slidingLayout.setScrollEvent(mainLayout);

        Button HSKLeaning = (Button) findViewById(R.id.HSKLeaning);
        HSKLeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CCCMainActivity.this, HSKActivity.class);
                intent.putExtra("learned_number",0);
                startActivityForResult(intent,1);
                //HSKActivity.startHSKLeaning(CCCMainActivity.this);
            }
        });

        Button RadicalLeaning = (Button) findViewById(R.id.component_leaning);
        RadicalLeaning.setOnClickListener(new View.OnClickListener() {
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

    private void init(){
        try{
            CharItemLab.getLab(CCCMainActivity.this);
            RadicalLab.getLab(CCCMainActivity.this);

        }
        catch (IOException exp){
            finish();
            return;
        }
        catch(JSONException exp){
            finish();
            return;
        }
    }
}
