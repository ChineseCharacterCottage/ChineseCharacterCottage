package ecnu.chinesecharactercottage.modelsForeground.mainLearningFragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsBackground.CharItem;
import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsForeground.CharacterFragment;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;

/**
 * @author 胡家斌
 * 这个fragment是主学习板块的第二部分（例字学习）的主要逻辑部分。
 */

public class CharsLearningFragment extends BaseFragment {
    //数据存取键值
    static final private String CHARACTERS="characters";
    //显示控件：
    //进度条
    @InjectView(id=R.id.progress_bar)
    private ProgressBar mProgressBar;
    //汉字详情
    private CharacterFragment mCharacterFragment;
    //下一个按键
    @InjectView(id=R.id.button_next)
    private Button mBtNext;

    //相关数据：
    //当前字
    private CharItem mNowItem;
    //当前字索引
    private int mPosition;
    //字总数
    private int mTotalNum;
    //字id列表
    private String[] mCharacters;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ml_chars_learning,container,false);//渲染视图
        //获取字详情显示fragment
        mCharacterFragment=(CharacterFragment)getChildFragmentManager().findFragmentById(R.id.character_fragment);
        //绑定控件
        Injecter.autoInjectAllField(this,view);

        //设置点击下一个按键的监听器
        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtNext.setEnabled(false);//数据加载完成前设置为不可再点击
                next();//刷新数据
            }
        });
        return view;
    }

    /**
     * 根据传入字id数组设置例字数据并刷新页面
     * @param characters 例字id数组
     */
    public void setCharacters(String[] characters){
        mCharacters=characters;
        mPosition =0;
        mTotalNum=mCharacters.length;
        mProgressBar.setMax(mTotalNum);
        next();//第一次加载数据
    }

    //加载数据
    private void next(){
        if(mPosition<mTotalNum) {//判断索引是否超过长度
            AsyncTask task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    DataManager dataManager = DataManager.getInstance(getActivity());//获取DataManager实例
                    mNowItem = dataManager.getCharItemByShape(mCharacters[mPosition]);//获取当前字的数据
                    mPosition++;//索引自增
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    if (mNowItem != null) {
                        mCharacterFragment.setCharacter(mNowItem);//设置Fragment的字数据
                        mBtNext.setEnabled(true);//激活下一个按键为可点击
                        mProgressBar.setProgress(mPosition);//设置进度条
                    } else if (mPosition < mTotalNum) {//如果当前字没数据但是例字id数组还有数据，则继续查下一个字id
                        next();
                    } else {//如果字没数据且字id数组也查完，则结束
                        finish();
                    }
                }
            };
            task.execute();
        }else//索引超过长度则结束进入下阶段
            finish();
    }
}
