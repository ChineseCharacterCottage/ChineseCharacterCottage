package ecnu.chinesecharactercottage.modelsForeground.testFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import ecnu.chinesecharactercottage.modelsBackground.TestFillItem;
import ecnu.chinesecharactercottage.modelsForeground.Marker;
import ecnu.chinesecharactercottage.modelsForeground.NextRunnable;
import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;

/**
 * @author 胡家斌
 * 阅读匹配题部分
 */

public class TestCompleteFragment extends Fragment {
    //4个候选词
    private TextView[] mWords;
    //当前候选词
    private String[] mNowWords;
    //4个题目句子
    private TextView[] mSentences;
    //选择的答案
    private Spinner[] mChosenAnswers;
    //确定按键
    @InjectView(id=R.id.bt_submit)
    private Button mBtSubmit;
    //下一个按键
    @InjectView(id=R.id.bt_next)
    private Button mBtNext;
    //下一个(函数方法)
    private NextRunnable mNext;
    //错误信息
    @InjectView(id=R.id.layout_error_msg)
    private LinearLayout mLayoutErrorMsg;
    //错误内容
    @InjectView(id=R.id.tv_error_msg)
    private TextView mTvErrorMsg;
    //当前题目
    private TestFillItem mNowTest;
    //收藏按键
    @InjectView(id=R.id.mark)
    private Button mMark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_test_complete,container,false);
        Injecter.autoInjectAllField(this,view);
        init(view);
        //设置按钮的监听器
        initButtons();
        return view;
    }

    private void init(View view){
        //初始化控件
        mWords=new TextView[5];
        mSentences=new TextView[5];
        mChosenAnswers=new Spinner[5];
        //绑定控件
        mWords[0]=(TextView)view.findViewById(R.id.tv_word_1);
        mWords[1]=(TextView)view.findViewById(R.id.tv_word_2);
        mWords[2]=(TextView)view.findViewById(R.id.tv_word_3);
        mWords[3]=(TextView)view.findViewById(R.id.tv_word_4);
        mWords[4]=(TextView)view.findViewById(R.id.tv_word_5);
        mSentences[0]=(TextView)view.findViewById(R.id.tv_sentence_1);
        mSentences[1]=(TextView)view.findViewById(R.id.tv_sentence_2);
        mSentences[2]=(TextView)view.findViewById(R.id.tv_sentence_3);
        mSentences[3]=(TextView)view.findViewById(R.id.tv_sentence_4);
        mSentences[4]=(TextView)view.findViewById(R.id.tv_sentence_5);
        mChosenAnswers[0]=(Spinner) view.findViewById(R.id.spinner_1);
        mChosenAnswers[1]=(Spinner) view.findViewById(R.id.spinner_2);
        mChosenAnswers[2]=(Spinner) view.findViewById(R.id.spinner_3);
        mChosenAnswers[3]=(Spinner) view.findViewById(R.id.spinner_4);
        mChosenAnswers[4]=(Spinner) view.findViewById(R.id.spinner_5);
        mBtSubmit.setEnabled(false);//初始化提交为不可点击
        mLayoutErrorMsg.setVisibility(View.GONE);//隐藏错误消息显示部分

    }

    private void initButtons() {
        mBtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correctAnswer=mNowTest.getCorrectAnswer();//获取正确答案
                String chosenAnswer="";
                //获取用户选择的顺序
                for(int i=0;i<5;i++){
                    chosenAnswer+=getAnswerNumber(mChosenAnswers[i]);
                }

                if(chosenAnswer.equals(correctAnswer)){
                    //正确回答时
                    //显示提交按钮
                    mBtSubmit.setVisibility(View.VISIBLE);
                    //隐藏错误信息
                    mLayoutErrorMsg.setVisibility(View.GONE);
                    //隐藏下一个按键
                    mBtNext.setVisibility(View.GONE);
                    //调用回答正确函数
                    mNext.next();
                }else{
                    //回答错误时
                    //隐藏提交按钮
                    mBtSubmit.setVisibility(View.GONE);
                    //显示错误信息
                    mLayoutErrorMsg.setVisibility(View.VISIBLE);
                    //显示下一个按键
                    mBtNext.setVisibility(View.VISIBLE);
                    //设置错误信息
                    mTvErrorMsg.setText(getCorrectAnswerMsg(correctAnswer));
                }
            }
        });

        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示提交按钮
                mBtSubmit.setVisibility(View.VISIBLE);
                //隐藏错误信息
                mLayoutErrorMsg.setVisibility(View.GONE);
                //隐藏下一个按键
                mBtNext.setVisibility(View.GONE);
                //调用回答正确函数
                mNext.next();
            }
        });
        mBtNext.setVisibility(View.GONE);//隐藏下一个按键

    }

    //根据传入的下拉框确定选择的词的序号
    private String getAnswerNumber(Spinner answer){
        String selectedAnswer=(String)answer.getSelectedItem();
        for(int i=0;i<5;i++){
            if(mNowWords[i].equals(selectedAnswer))
                return String.valueOf(i+1);
        }
        return "0";
    }

    //返回错误信息，即正确答案匹配
    private String getCorrectAnswerMsg(String correctAnswer){
        String correctAnswerMsg="";
        String[] index={"A、","B、","C、","D、","E、"};
        for(int i=0;i<5;i++){
            correctAnswerMsg=correctAnswerMsg+index[i]+correctAnswer.charAt(i)+"   ";
        }
        return correctAnswerMsg;
    }


    public void setNext(NextRunnable next){
        mNext=next;
    }

    public void setTest(TestFillItem testFillItem){
        if(testFillItem==null) {
            mNext.next();
            return;
        }
        mBtSubmit.setEnabled(true);
        mNowTest=testFillItem;
        mNowWords=testFillItem.getChoices();
        String[] sentences=testFillItem.getSentences();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,mNowWords);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        for(int i=0;i<5;i++){
            mWords[i].setText(mNowWords[i]);
            mChosenAnswers[i].setAdapter(adapter);
            mSentences[i].setText(sentences[i]);
        }

        new Marker(getActivity()).setMark(mMark,mNowTest);
    }
}
