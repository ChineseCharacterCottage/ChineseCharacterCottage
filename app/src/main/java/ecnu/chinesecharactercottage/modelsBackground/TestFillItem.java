package ecnu.chinesecharactercottage.modelsBackground;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shensheng on 2017/3/6.
 * 填空
 */

public class TestFillItem extends TestItem {

    private String mId;
    private String[] mSentences;
    private String[] mChoices;
    private String mCorrectAnswer;
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
    public String[] getSentences() {
        return mSentences;
    }

    public String[] getChoices() {
        return mChoices;
    }

    public String getTestId(){
        return mId;
    }

    @Override
    public String getCorrectAnswer(){
        return mCorrectAnswer;
    }

    @Override
    public String getType(){
        return DataManager.FILL;
    }
}
