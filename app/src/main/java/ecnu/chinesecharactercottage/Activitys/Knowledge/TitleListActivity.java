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

import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.ModelsBackground.Knowledge;
import ecnu.chinesecharactercottage.modelsForeground.inject.InjectView;
import ecnu.chinesecharactercottage.modelsForeground.inject.Injecter;
import ecnu.chinesecharactercottage.modelsForeground.ListViewItemAdapter;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/5/2.
 */

public class TitleListActivity extends Activity {

    //文章列表
    private List<Knowledge> mKnowledges;
    //ListView
    @InjectView(id= R.id.list_view)
    ListView mListView;

    public static void startActivity(Context context){
        Intent intent=new Intent(context,TitleListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_list);
        Injecter.autoInjectAllField(this);

        AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                DataManager dataManager=DataManager.getInstance(TitleListActivity.this);
                Knowledge[] knowledges=dataManager.getKnowledgeList();
                mKnowledges= Arrays.asList(knowledges);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                setListView();
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
                KnowledgeDetailsActivity.startActivity(TitleListActivity.this,mKnowledges.get(position).id);
            }
        });

    }
}
