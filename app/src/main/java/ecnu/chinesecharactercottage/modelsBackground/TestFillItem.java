package ecnu.chinesecharactercottage.modelsBackground;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 匡申升
 * 填空题
 * 五个空，五个词语，选填
 */

public class TestFillItem extends TestItem {

    private String mId;
    private String[] mSentences;
    private String[] mChoices;
    private String mCorrectAnswer;
    /**
     * 从一个JSON对象生成一个填空题实例。*/
    public TestFillItem(JSONObject json) throws JSONException{
        mId=json.getString("ID");
        mSentences=new String[5];
        mChoices=new String[5];
        for(int i=1;i<=5;i++){
            mSentences[i-1]=json.getString("sentence"+i);
            mChoices[i-1]=json.getString("choice"+i);
        }
        mCorrectAnswer=json.getString("correct_order");
    }
    /**
     * 从一个游标生成一个填空题实例。
     * @see DataManager,Cursor,android.database.sqlite.SQLiteOpenHelper*/
    public TestFillItem(Cursor cursor){
        mId=cursor.getString(cursor.getColumnIndex("ID"));
        mSentences=new String[5];
        mChoices=new String[5];
        for(int i=1;i<=5;i++){
            mSentences[i-1]=cursor.getString(cursor.getColumnIndex("sentence"+i));
            mChoices[i-1]=cursor.getString(cursor.getColumnIndex("choice"+i));
        }
        mCorrectAnswer=cursor.getString(cursor.getColumnIndex("correct_order"));
    }
    /**
     * @see TestItem*/
    @Override
    public ContentValues toContentValue(){
        ContentValues values=new ContentValues();
        values.put("ID",mId);
        values.put("correct_order",mCorrectAnswer);
        for(int i=1;i<=5;i++){
            values.put("sentence"+i,mSentences[i-1]);
            values.put("choice"+i,mChoices[i-1]);
        }
        return values;
    }
    /**
     * 获取所有的句子。*/
    public String[] getSentences() {
        return mSentences;
    }

    /**
     * 获取选项。*/
    public String[] getChoices() {
        return mChoices;
    }
    /**
     * @see TestItem*/
    @Override
    public String getTestId(){
        return mId;
    }
    /**
     * @see TestItem*/
    @Override
    public String getCorrectAnswer(){
        return mCorrectAnswer;
    }
    /**
     * @see TestItem*/
    @Override
    public String getType(){
        return DataManager.FILL;
    }
}
