package ecnu.chinesecharactercottage.activitys.character;

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

import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsForeground.ListViewItemAdapter;
import ecnu.chinesecharactercottage.modelsForeground.ComponentDialog;
import ecnu.chinesecharactercottage.modelsBackground.ComponentItem;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2016/11/28.
 */

public class ComponentListActivity extends Activity {

    //每页数量
    private static final int ITEM_NUMBER=20;
    //模式,0:shape.1:voice
    private int mModel;


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
        mModel=getIntent().getIntExtra("model",ComponentItem.SHAPE);

        init();
        AsyncTask task=new AsyncTask<Object,Object,ComponentItem[]>() {
            @Override
            protected ComponentItem[] doInBackground(Object[] params) {
                DataManager dataManager=DataManager.getInstance(ComponentListActivity.this);
                ComponentItem[] componentItems=dataManager.getAllComponents(mModel==ComponentItem.VOICE);
                return componentItems;
            }

            @Override
            protected void onPostExecute(ComponentItem[] componentItems) {
                if(componentItems!=null) {
                    mComponentItems = componentItems;
                    buildList();
                }else
                    saveData();
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
                if(mListIndex>mComponentItems.length)
                    saveData();
                else
                    buildList();
            }
        });
    }

    private void refresh(){
        mListView.setAdapter(new ListViewItemAdapter<>(ComponentListActivity.this,R.layout.component_list_item,mComponentList,"getShape"));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                ComponentDialog.startDialog(ComponentListActivity.this,mComponentList.get(position),mModel);
            }
        });
    }

    private void buildList(){
        if(mListIndex+ITEM_NUMBER>=mComponentItems.length){
            //由于不包括结束位置，所以要到length这里
            buildList(mListIndex,mComponentItems.length);
        }
        else{
            buildList(mListIndex,mListIndex+ITEM_NUMBER);
        }
        mListIndex+=ITEM_NUMBER;
    }

    //构建列表中从start到end的部分，不包括end
    private void buildList(int start,int end){
        mComponentList=new ArrayList<>();
        for(int i=start;i<end;i++) {
            mComponentList.add(mComponentItems[i]);
        }
        refresh();
    }

    private void saveData(){
        //保存数据
        finish();
    }

}