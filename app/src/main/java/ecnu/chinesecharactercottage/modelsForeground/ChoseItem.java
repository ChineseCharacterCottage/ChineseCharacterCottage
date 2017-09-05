package ecnu.chinesecharactercottage.modelsForeground;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ecnu.chinesecharactercottage.R;

/**
 * @author 胡家斌
 * 这是一个自定义控件，各界面上的跳转的标签就是这个类中实现的把显示的文字和下面的图片绑定在一起，实现共同的点击监听器
 */

public class ChoseItem extends LinearLayout{

    TextView mTextView;
    Button mBtChose;

    public ChoseItem(Context context, AttributeSet attrs){
        super(context,attrs);
        LinearLayout.inflate(context,R.layout.chose_item,this);//渲染布局
        //绑定控件
        mBtChose=(Button)findViewById(R.id.bt_item_img);
        mTextView=(TextView)findViewById(R.id.tv_item_name);
        mTextView.setTextSize(20);//设置显示字体大小
        //获取控件的属性，其中R.styleable是自定义在value里的资源文件attrs.xml中的属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ChoseItem);
        setText(array.getString(R.styleable.ChoseItem_text));//设置控件字为属性中的字
        setImg(array.getResourceId(R.styleable.ChoseItem_img,R.drawable.bg));//设置控件图片为属性中设置的图片

        array.recycle();//TypedArray的实例使用完后要回收,官方解释是为了方便后面重用。这个TypedArray实际上用的是单例模式，所以必须回收。
    }

    /**
     * 设置控件的点击监听器
     * @param listener 点击监听器
     */
    @Override
    public void setOnClickListener(OnClickListener listener){
        mBtChose.setOnClickListener(listener);
    }

    /**
     * 设置控件的文字
     * @param text 需要显示的文字
     */
    public void setText(String text){
        mTextView.setText(text);
    }

    /**
     * 设置控件的图片
     * @param resourceId 需要设置的图片的资源id
     */
    public void setImg(int resourceId){
        mBtChose.setBackgroundResource(resourceId);
    }
}
