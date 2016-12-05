package ecnu.chinesecharactercottage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10040 on 2016/11/28.
 */

public class RadicalListActivity extends Activity {

    //布局
    private ListView mListView;

    //部首库
    private RadicalLab mRadicalLab;
    //部首列表
    private List<RadicalItem> mRadicalList;
    //部首列表首个对象索引
    private int mListIndex;

    public static void startActivity(Context context){
        Intent intent=new Intent(context,RadicalActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_radical_list);

        init();
        mListView.setAdapter(new RadicalAdapter(RadicalListActivity.this,R.layout.radical_list_item,mRadicalList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                RadicalActivity.startActivity(RadicalListActivity.this,mRadicalList.get(position));
            }
        });

    }

    private void init(){
        mListView=(ListView)findViewById(R.id.rdical_list);
        mListIndex=1;
        buildList();
        mRadicalLab.getLab(RadicalListActivity.this);
    }
    
    private void buildList(){
        mRadicalList=new ArrayList<RadicalItem>();
        for(int i=0;i<20;i++){
            mRadicalList.add(mRadicalLab.getRadical(String.valueOf(i+mListIndex)));
        }
    }

}
