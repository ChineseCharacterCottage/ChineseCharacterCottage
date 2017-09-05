package ecnu.chinesecharactercottage.modelsForeground;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ecnu.chinesecharactercottage.modelsBackground.CharItem;
import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsBackground.TestItem;
import ecnu.chinesecharactercottage.R;

/**
 * @author 胡家斌
 * 这个类负责设置传入Button控件的收藏功能，包括点击图片变化以及收藏相关数据。
 */

public class Marker {

    //上下文
    private Context mContext;
    //数据管理器
    private DataManager mDataManager;
    //是否被收藏的标志位
    private boolean mIsMark;

    public Marker(Context context){
        mContext=context;//保存上下文
        mDataManager=DataManager.getInstance(context);//获取DataManager实例
    }

    /**
     * 设置收藏按键的背景以及监听器，需要传入绑定的字数据
     * @param mark 收藏按键
     * @param charItem 字数据
     */
    public void setMark(final Button mark, final CharItem charItem){
        mIsMark=mDataManager.isInCollection(charItem);//初始化收藏标志位
        //根据收藏情况初始化按键的背景图片
        if(mIsMark)
            mark.setBackgroundResource(R.drawable.star_marked);
        else
            mark.setBackgroundResource(R.drawable.star);
        //设置收藏按键监听器
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsMark) {//若已被收藏
                    mDataManager.removeCollection(charItem);//移除收藏
                    mark.setBackgroundResource(R.drawable.star);//设置背景
                    Toast.makeText(mContext,"Remove from Collection!",Toast.LENGTH_SHORT).show();//显示图片
                }
                else {//若未被收藏
                    mDataManager.putIntoCollection(charItem);//添加收藏
                    mark.setBackgroundResource(R.drawable.star_marked);//设置背景
                    Toast.makeText(mContext,"Add to Collection!",Toast.LENGTH_SHORT).show();//显示图片
                }
                mIsMark=!mIsMark;//变更收藏状态，实际上这里如果从DataManager实例中获取收藏状态更好，但是效率稍微低一些。
            }
        });
    }

    /**
     * 设置收藏按键的背景以及监听器，需要传入绑定的测试数据
     * @param mark 收藏按键
     * @param testItem 测试数据
     */
    public void setMark(final Button mark,final TestItem testItem){
        //这个方法的逻辑和上面的方法一样
        mIsMark=mDataManager.isInCollection(testItem);
        if(mIsMark)
            mark.setBackgroundResource(R.drawable.star_marked);
        else
            mark.setBackgroundResource(R.drawable.star);
        //收藏按键
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsMark) {
                    mDataManager.removeCollection(testItem);
                    mark.setBackgroundResource(R.drawable.star);
                    Toast.makeText(mContext,"Remove from Collection!",Toast.LENGTH_SHORT).show();
                }
                else {
                    mDataManager.putIntoCollection(testItem);
                    mark.setBackgroundResource(R.drawable.star_marked);
                    Toast.makeText(mContext,"Add to Collection!",Toast.LENGTH_SHORT).show();
                }
                mIsMark=!mIsMark;
            }
        });
    }
}
