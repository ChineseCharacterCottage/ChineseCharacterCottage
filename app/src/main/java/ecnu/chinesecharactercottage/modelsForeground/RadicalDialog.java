package ecnu.chinesecharactercottage.modelsForeground;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ecnu.chinesecharactercottage.R;
import ecnu.chinesecharactercottage.modelsBackground.RadicalItem;

/**
 * Created by 10040 on 2016/12/10.
 */

public class RadicalDialog extends DialogFragment {

    //对话框tag
    private static final String TAG="component_dialog";
    //部首
    private static RadicalItem sRadical;

    //部首字形
    private TextView mFigure;
    //部首中文意思
    private TextView mMeaning;
    //部首例字
    private LinearLayout mExampleCharacter;

    static public RadicalDialog getDialogInstance(RadicalItem radicalItem){
        sRadical=radicalItem;
        return new RadicalDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME,android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.dialog_radical,container,false);
        mFigure=(TextView)v.findViewById(R.id.radical_figure);
        mMeaning=(TextView)v.findViewById(R.id.radical_name);
        mExampleCharacter=(LinearLayout) v.findViewById(R.id.radical_example);

        setRadical();

        return v;
    }

    public void show(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        super.show(ft,TAG);
    }

    private void setRadical() {
        mFigure.setText(sRadical.getRadical());
        mMeaning.setText(sRadical.getName());

        String[] examples = sRadical.getExamples();
        int exampleNumber = examples.length;

        LinearLayout linearLayout;
        linearLayout = new LinearLayout(getActivity());//为了解决报错

        TextView aExample;
        for (int i = 0; i < exampleNumber; i++) {
            if (i % 5 == 0) {
                linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                mExampleCharacter.addView(linearLayout);
            }
            aExample = new TextView(getActivity());
            aExample.setPadding(10,0,10,0);
            aExample.setTextSize(20);
            aExample.setText(examples[i]);
            /*防止无限点击，这里先设定为不可点击
            aExample.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CharItem exampleItem;
                    CharItemLab charItemLab;
                    //获取对应例字
                    try {
                        charItemLab = CharItemLab.getLabWithoutContext();
                    } catch (Exception e) {
                        Log.d("getLab IOException", e.toString());
                        getActivity().finish();
                        return;
                    }
                    if (charItemLab == null) {
                        Log.d("charItemLab", "is null");
                    }
                    try {
                        exampleItem = charItemLab.findCharItemsByShape(((TextView) view).getText().toString())[0];
                        ExampleActivity.startActivity(getActivity(), exampleItem);
                    } catch (Exception e) {
                        Log.d("findCharItemsByShape", e.toString());
                        e.printStackTrace();
                    }
                }
            });*/
            linearLayout.addView(aExample);
        }
    }
}