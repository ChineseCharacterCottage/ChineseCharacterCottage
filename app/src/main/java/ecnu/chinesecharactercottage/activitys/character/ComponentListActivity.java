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
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;

/**
 * @author 胡家斌
 * 这个活动负责展示部件列表，部件可以是表音的或者表形的，通过创建实例时传入的模式常量区分。
 */
public class ComponentListActivity extends Activity {

    //每页数量
    private static final int ITEM_NUMBER=20;
    //模式,0:shape.1:voice
    private int mModel;

    //部件列表布局
    @InjectView(id=R.id.component_list)
    private ListView mListView;
    //下一页按键
    @InjectView(id=R.id.button_next)
    private Button mButtonNext;
    //部件列表
    private List<ComponentItem> mComponentList;
    //所有部件的数组
    private ComponentItem[] mComponentItems;
    //部首列表首个对象索引
    private int mListIndex;

    /**
     * 静态活动跳转方法
     * @param context 需要跳转的活动上下文
     * @param model 模式，0:shape.1:voice,这个常量定义在ComponentItem类中。
     * @see ComponentItem
     */
    public static void startActivity(Context context,int model){
        Intent intent=new Intent(context,ComponentListActivity.class);
        intent.putExtra("model",model);//将模式常量值存到bundle中
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_component_list);//设置布局
        mModel=getIntent().getIntExtra("model",ComponentItem.SHAPE);//获取类型常量值

        init();//初始化成员变量
        AsyncTask task=new AsyncTask<Object,Object,ComponentItem[]>() {
            @Override
            protected ComponentItem[] doInBackground(Object[] params) {
                DataManager dataManager=DataManager.getInstance(ComponentListActivity.this);//获取实例
                return dataManager.getAllComponents(mModel==ComponentItem.VOICE);//返回对应类型的部件数组，以后部件数量多了以后，这里必然要改为只获取部分部件
            }

            @Override
            protected void onPostExecute(ComponentItem[] componentItems) {
                if(componentItems!=null) {
                    mComponentItems = componentItems;//将数据保存到成员变量中
                    buildList();//构建显示列表并刷新
                }else
                    saveData();
            }
        };
        task.execute();//执行异步对象

    }

    private void init(){
        Injecter.autoInjectAllField(this);//绑定控件
        mListIndex=1;//初始化索引为1

        //设置下一个按键的监听器
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListIndex>mComponentItems.length)//如果索引已经超了，则结束本次学习
                    saveData();//保存数据
                else
                    buildList();
            }
        });
    }

    private void refresh(){
        //更新列表
        mListView.setAdapter(new ListViewItemAdapter<>(ComponentListActivity.this,R.layout.component_list_item,mComponentList,"getShape"));
        //设置列表项监听器
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //显示部件详情对话框
                ComponentDialog.startDialog(ComponentListActivity.this,mComponentList.get(position));
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
        //索引增加每页显示数量
        mListIndex+=ITEM_NUMBER;
    }

    //构建列表中从start到end的部分，不包括end
    private void buildList(int start,int end){
        mComponentList=new ArrayList<>();
        //将数组相应下标的元素添加到列表中
        for(int i=start;i<end;i++) {
            mComponentList.add(mComponentItems[i]);
        }
        refresh();
    }

    private void saveData(){
        //保存数据
        //现在没有实现记录功能，以后要在这里记录上次学习的记录
        finish();
    }

}
