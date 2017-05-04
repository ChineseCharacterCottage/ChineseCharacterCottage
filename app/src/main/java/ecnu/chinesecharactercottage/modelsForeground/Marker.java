package ecnu.chinesecharactercottage.modelsForeground;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import ecnu.chinesecharactercottage.ModelsBackground.CharItem;
import ecnu.chinesecharactercottage.ModelsBackground.DataManager;
import ecnu.chinesecharactercottage.ModelsBackground.TestItem;
import ecnu.chinesecharactercottage.R;

/**
 * Created by 10040 on 2017/5/3.
 */

public class Marker {

    private Context mContext;
    private DataManager mDataManager;
    private boolean mIsMark;

    public Marker(Context context){
        mContext=context;
        mDataManager=DataManager.getInstance(context);
    }

    public void setMark(final Button mark, final CharItem charItem){
        mIsMark=mDataManager.isInCollection(charItem);
        if(mIsMark)
            mark.setBackgroundResource(R.drawable.star_marked);
        else
            mark.setBackgroundResource(R.drawable.star);
        //收藏按键
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsMark) {
                    mDataManager.removeCollection(charItem);
                    mark.setBackgroundResource(R.drawable.star);
                }
                else {
                    mDataManager.putIntoCollection(charItem);
                    mark.setBackgroundResource(R.drawable.star_marked);
                }
                mIsMark=!mIsMark;
            }
        });
    }

    public void setMark(final Button mark,final TestItem testItem){
        mIsMark=mDataManager.isInCollection(testItem);
        if(mIsMark)
            mark.setBackgroundResource(R.drawable.star_marked);
        else
            mark.setBackgroundResource(R.drawable.star);
        //收藏按键
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsMark) {
                    mDataManager.removeCollection(testItem);
                    mark.setBackgroundResource(R.drawable.star);
                    Toast.makeText(mContext,"Add to Collection!",Toast.LENGTH_SHORT).show();
                }
                else {
                    mDataManager.putIntoCollection(testItem);
                    mark.setBackgroundResource(R.drawable.star_marked);
                    Toast.makeText(mContext,"Remove from Collection!",Toast.LENGTH_SHORT).show();
                }
                mIsMark=!mIsMark;
            }
        });
    }
}
