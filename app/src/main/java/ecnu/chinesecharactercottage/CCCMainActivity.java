package ecnu.chinesecharactercottage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;

import java.io.IOException;

public class CCCMainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccc_main);

        init();

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
                ComponentListActivity.startActivity(CCCMainActivity.this);
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
