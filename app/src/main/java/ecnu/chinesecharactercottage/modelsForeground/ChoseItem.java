package ecnu.chinesecharactercottage.modelsForeground;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/5/3.
 */

public class ChoseItem extends LinearLayout{

    TextView mTextView;
    Button mBtChose;

    public ChoseItem(Context context, AttributeSet attrs){
        super(context,attrs);
        LinearLayout.inflate(context,R.layout.chose_item,this);
        mBtChose=(Button)findViewById(R.id.bt_item_img);
        mTextView=(TextView)findViewById(R.id.tv_item_name);
        mTextView.setTextSize(20);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ChoseItem);
        setText(array.getString(R.styleable.ChoseItem_text));
        setImg(array.getResourceId(R.styleable.ChoseItem_img,R.drawable.bg));

        array.recycle();
    }

    @Override
    public void setOnClickListener(OnClickListener listener){
        mBtChose.setOnClickListener(listener);
    }

    public void setText(String text){
        mTextView.setText(text);
    }

    public void setImg(int resourceId){
        mBtChose.setBackgroundResource(resourceId);
    }
}
