package ecnu.chinesecharactercottage.modelsForeground;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ecnu.chinesecharactercottage.ModelsBackground.CharItem;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/5/3.
 */

public class ChoseItem extends LinearLayout{

    TextView mTextView;
    Button mBtChose;

    public ChoseItem(Context context, AttributeSet attr){
        super(context,attr);
        LinearLayout.inflate(context,R.layout.chose_item,this);
        mBtChose=(Button)findViewById(R.id.bt_item_img);
        mTextView=(TextView)findViewById(R.id.tv_item_name);
        int stringRS=attr.getAttributeResourceValue("android","text",R.string.chose_item);
        int imgRS=attr.getAttributeResourceValue("android","src",R.drawable.arrow);
        mTextView.setText(context.getString(stringRS));
        mBtChose.setBackgroundResource(imgRS);

    }

    @Override
    public void setOnClickListener(OnClickListener listener){
        mBtChose.setOnClickListener(listener);
    }
}
