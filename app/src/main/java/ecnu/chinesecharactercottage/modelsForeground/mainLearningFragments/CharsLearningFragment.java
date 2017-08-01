package ecnu.chinesecharactercottage.modelsForeground.mainLearningFragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.activitys.character.PictogramActivity;
import ecnu.chinesecharactercottage.modelsBackground.CharItem;
import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsBackground.ShapeCharItem;
import ecnu.chinesecharactercottage.modelsForeground.CharacterFragment;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;

/**
 * Created by 10040 on 2017/7/26.
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
    //当前字编号
    private int mPosition;
    //字总数
    private int mTotalNum;
    //字列表
    private String[] mCharacters;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ml_chars_learning,container,false);
        mCharacterFragment=(CharacterFragment)getChildFragmentManager().findFragmentById(R.id.character_fragment);
        Injecter.autoInjectAllField(this,view);

        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtNext.setEnabled(false);
                next();
            }
        });
        return view;
    }

    public void setCharacters(String[] characters){
        mCharacters=characters;
        mPosition =0;
        mTotalNum=mCharacters.length;
        mProgressBar.setMax(mTotalNum);
        next();
    }

    private void next(){
        if(mPosition<mTotalNum) {
            AsyncTask task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    DataManager dataManager = DataManager.getInstance(getActivity());
                    mNowItem = dataManager.getCharItemByShape(mCharacters[mPosition]);
                    mPosition++;
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    if (mNowItem != null) {
                        mCharacterFragment.setCharacter(mNowItem);
                        mBtNext.setEnabled(true);
                        mProgressBar.setProgress(mPosition);
                    } else if (mPosition < mTotalNum) {
                        next();
                    } else {
                        finish();
                    }
                }
            };
            task.execute();
        }else
            finish();
    }

}
