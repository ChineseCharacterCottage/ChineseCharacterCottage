package ecnu.chinesecharactercottage.modelsForeground;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/5/3.
 */

public class TopBar extends LinearLayout {

    //返回按键
    private Button mBtBack;

    public TopBar(Context context, AttributeSet attr){
        super(context,attr);
        LayoutInflater.from(context).inflate(R.layout.top_bar,this);

        mBtBack=(Button)findViewById(R.id.bt_back);
        mBtBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).finish();
            }
        });
    }
}
