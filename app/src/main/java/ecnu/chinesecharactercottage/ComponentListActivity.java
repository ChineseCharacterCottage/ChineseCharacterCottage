package ecnu.chinesecharactercottage;

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

/**
 * Created by 10040 on 2016/11/28.
 */

public class ComponentListActivity extends Activity {

    final int ITEM_NUMBER=20;

    //部首列表布局
    private ListView mListView;
    //下一页
    private Button mButton;
    //部首总个数
    private int mMaxNumber;
    //部首库
    private RadicalLab mRadicalLab;
    //部首列表
    private List<RadicalItem> mRadicalList;
    //部首列表首个对象索引
    private int mListIndex;

    public static void startActivity(Context context){
        Intent intent=new Intent(context,ComponentListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_component_list);

        init();
        reflesh();

    }

    private void init(){
        mListView=(ListView)findViewById(R.id.rdical_list);
        mListIndex=1;

        try{
            mRadicalLab=RadicalLab.getLab(ComponentListActivity.this);
        }
        catch (Exception e){
            Log.d("Radical.getLab:",e.toString());
            e.printStackTrace();
        }
        mMaxNumber=mRadicalLab.getSize();
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
        mRadicalList=new ArrayList<RadicalItem>();
        int thisIndex;
        for(int i=0;i<ITEM_NUMBER;i++){
            thisIndex=i+mListIndex;
            if(thisIndex<=mMaxNumber)
                mRadicalList.add(mRadicalLab.getRadical(String.valueOf(thisIndex)));
            else
                return;
        }
    }

    private void reflesh(){

        if(mListIndex+ITEM_NUMBER>mMaxNumber){
            mButton.setText(R.string.button_finish);
        }

        buildList();
        mListView.setAdapter(new RadicalAdapter(ComponentListActivity.this,R.layout.component_list_item,mRadicalList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //ComponentActivity.startActivity(ComponentListActivity.this,mRadicalList.get(position));
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("component_dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                ComponentDialog myComponentDialog= ComponentDialog.getDialogInstance(mRadicalList.get(position));
                myComponentDialog.show(ft,"component_dialog");
            }
        });
    }

    private void saveData(){
        //保存数据
        finish();
    }

}
