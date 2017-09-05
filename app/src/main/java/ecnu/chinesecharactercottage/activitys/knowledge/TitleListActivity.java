package ecnu.chinesecharactercottage.activitys.knowledge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import ecnu.chinesecharactercottage.modelsBackground.DataManager;
import ecnu.chinesecharactercottage.modelsBackground.Knowledge;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;
import ecnu.chinesecharactercottage.modelsForeground.ListViewItemAdapter;
import ecnu.chinesecharactercottage.R;

/**
 * @author 胡家斌
 * 这个活动负责显示小知识列表
 */
public class TitleListActivity extends Activity {

    //文章列表
    private List<Knowledge> mKnowledges;
    //ListView
    @InjectView(id= R.id.list_view)
    ListView mListView;

    /**
     * 静态活动跳转方法
     * @param context 需要跳转的活动上下文
     */
    public static void startActivity(Context context){
        Intent intent=new Intent(context,TitleListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_list);//设置布局文件
        Injecter.autoInjectAllField(this);//利用注释类动态绑定布局中的控件,详细请查看modesForeground.inject包中的两个类

        //创建一个异步对象，从服务器上获取小知识列表
        AsyncTask task=new AsyncTask() {
            //doInBackground方法会在子线程中运行
            @Override
            protected Object doInBackground(Object[] params) {
                DataManager dataManager=DataManager.getInstance(TitleListActivity.this);//获取DaaManager实例
                Knowledge[] knowledges=dataManager.getKnowledgeList();//从DataManager实例获取收藏的汉字id数组
                if(knowledges!=null)
                    mKnowledges= Arrays.asList(knowledges);//将数组转换成list
                return null;
            }

            //onPostExecute方法在主线程中运行，它的参数是doInBackground的返回值
            @Override
            protected void onPostExecute(Object o) {
                if(mKnowledges!=null)
                    setListView();
                else
                    finish();
            }
        };
        task.execute();
    }

    public void setListView(){
        //设置适配器
        mListView.setAdapter(new ListViewItemAdapter<>(this,R.layout.component_list_item,mKnowledges,"getTitle"));
        //设置点击监听器
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到小知识详情活动，并传入小知识的id
                KnowledgeDetailsActivity.startActivity(TitleListActivity.this,mKnowledges.get(position).id);
            }
        });

    }
}
