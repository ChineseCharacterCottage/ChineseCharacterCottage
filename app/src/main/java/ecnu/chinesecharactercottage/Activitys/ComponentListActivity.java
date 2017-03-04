package ecnu.chinesecharactercottage.Activitys;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ecnu.chinesecharactercottage.ModelsForeground.ComponentAdapter;
import ecnu.chinesecharactercottage.ModelsForeground.ComponentDialog;
import ecnu.chinesecharactercottage.ModelsBackground.ComponentItem;
import ecnu.chinesecharactercottage.ModelsBackground.ComponentLab;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2016/11/28.
 */

public class ComponentListActivity extends Activity {

    //模式
    int mModel;

    //每页数量
    final int ITEM_NUMBER=20;

    //部首列表布局
    private ListView mListView;
    //下一页
    private Button mButton;
    //部首总个数
    private int mMaxNumber;
    //部首库
    private ComponentLab mComponentLab;
    //部首列表
    private List<ComponentItem> mComponentList;
    //部首列表首个对象索引
    private int mListIndex;

    public static void startActivity(Context context,int model){
        Intent intent=new Intent(context,ComponentListActivity.class);
        intent.putExtra("model",model);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_component_list);
        mModel=getIntent().getIntExtra("model",0);

        init();
        reflesh();

    }

    private void init(){
        mListView=(ListView)findViewById(R.id.component_list);
        mListIndex=1;

        try{
            mComponentLab=ComponentLab.getLab(ComponentListActivity.this);
        }
        catch (Exception e){
            Log.d("Component.getLab:",e.toString());
            e.printStackTrace();
        }
        if(mModel==0){
            mMaxNumber=mComponentLab.getNumOfShapeComponents();
        }else{
            mMaxNumber=mComponentLab.getNumOfVoiceComponents();
        }
        mButton=(Button)findViewById(R.id.button_next);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListIndex+=ITEM_NUMBER;
                if(mListIndex>ITEM_NUMBER){
                    saveData();
                }
                else{
                    reflesh();
                }
            }
        });
    }
    
    private void buildList(){
        mComponentList=new ArrayList<ComponentItem>();
        int thisIndex;
        for(int i=0;i<ITEM_NUMBER;i++){
            thisIndex=i+mListIndex;
            if(thisIndex<=mMaxNumber)
            {
                if(mModel==0){
                    mComponentList.add(mComponentLab.getShapeComponent(String.valueOf(thisIndex)));
                }else {
                    mComponentList.add(mComponentLab.getVoiceComponent(String.valueOf(thisIndex)));
                }

            }
            else
                return;
        }
    }

    private void reflesh(){

        if(mListIndex+ITEM_NUMBER>mMaxNumber){
            mButton.setText(R.string.button_finish);
        }

        buildList();
        mListView.setAdapter(new ComponentAdapter(ComponentListActivity.this,R.layout.component_list_item,mComponentList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("component_dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                ComponentDialog myComponentDialog= ComponentDialog.getDialogInstance(mComponentList.get(position),mModel);
                myComponentDialog.show(ft,"component_dialog");
            }
        });
    }

    private void saveData(){
        //保存数据
        finish();
    }

}