package ecnu.chinesecharactercottage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CCCMainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ccc_main_activity);

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

        Button RadicalLeaning = (Button) findViewById(R.id.radical_leaning);
        RadicalLeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadicalListActivity.startActivity(CCCMainActivity.this);
            }
        });
    }
}
