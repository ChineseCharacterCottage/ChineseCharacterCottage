package ecnu.chinesecharactercottage.Activitys;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.ModelsBackground.RadicalItem;
import ecnu.chinesecharactercottage.ModelsForeground.ComponentAdapter;
import ecnu.chinesecharactercottage.ModelsForeground.ComponentDialog;
import ecnu.chinesecharactercottage.ModelsBackground.ComponentItem;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2016/11/28.
 */

public class ComponentListActivity extends Activity {

    //模式,0:shape.1:voice
    int mModel;

    //每页数量
    final int ITEM_NUMBER=20;

    //部件列表布局
    private ListView mListView;
    //下一页
    private Button mButtonNext;
    //部件列表
    private List<ComponentItem> mComponentList;
    //所有部件数组，这是临时的
    private ComponentItem[] mComponentItems;
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
        AsyncTask task=new AsyncTask<Object,Object,ComponentItem[]>() {
            @Override
            protected ComponentItem[] doInBackground(Object[] params) {
                DataManager dataManager=DataManager.getInstance(ComponentListActivity.this);
                ComponentItem[] componentItems=dataManager.getAllComponents(mModel==1);
                return componentItems;
            }

            @Override
            protected void onPostExecute(ComponentItem[] componentItems) {
                mComponentItems=componentItems;
                buildList();
            }
        };
        task.execute();

    }

    private void init(){
        mListView=(ListView)findViewById(R.id.component_list);
        mListIndex=1;

        mButtonNext=(Button)findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListIndex+=ITEM_NUMBER;
                if(mListIndex>ITEM_NUMBER){
                    mListIndex-=ITEM_NUMBER;
                    buildLastList();
                }
                else{
                    buildList();
                }
            }
        });
    }

    private void refresh(){
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

    private void buildList(){
        int thisIndex;
        mComponentList=new ArrayList<>();
        for(int i=0;i<ITEM_NUMBER;i++) {
            thisIndex = i + mListIndex;
            mComponentList.add(mComponentItems[thisIndex]);
        }
        refresh();
    }

    private void buildLastList(){
        int len=mComponentItems.length;
        mComponentList=new ArrayList<>();
        for(;mListIndex<len;mListIndex++) {
            mComponentList.add(mComponentItems[mListIndex]);
        }
        refresh();
        saveData();
    }

    private void saveData(){
        //保存数据
        finish();
    }

}
