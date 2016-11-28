package ecnu.chinesecharactercottage;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

/**
 * Created by 10040 on 2016/11/28.
 */

public class RadicalListActivity extends Activity {

    //布局
    private ListView mListView;

    //部首列表
    private RadicalItem[] RadiaclList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_radical_list);
        mListView=(ListView)findViewById(R.id.rdical_list);
    }


}
