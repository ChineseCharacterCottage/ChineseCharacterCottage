package ecnu.chinesecharactercottage.modelsBackground;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 匡申升
 * @see TestItem
 * 部件测试题。
 */

public class TestComponentItem extends TestItem {

    private String mId;
    private String mCompId;
    private String mChoiceA;
    private String mChoiceB;
    private String mChoiceC;
    private String mChoiceD;
    private String mCorrectAnswer;
    /**
     * 从一个JSON对象构造一个实例。*/
    public TestComponentItem(JSONObject json) throws JSONException{
        mId = json.getString("ID");
        mCompId = json.getString("comp_id");
        mChoiceA = json.getString("choice_a");
        mChoiceB = json.getString("choice_b");
        mChoiceC = json.getString("choice_c");
        mChoiceD = json.getString("choice_d");
        mCorrectAnswer = json.getString("correct_ans");
    }
    /**
     * 从数据库游标构造实例。
     * @see DataManager,Cursor,android.database.sqlite.SQLiteOpenHelper*/
    public TestComponentItem(Cursor cursor){
        mId = cursor.getString(cursor.getColumnIndex("ID"));
        mCompId = cursor.getString(cursor.getColumnIndex("comp_id"));
        mChoiceA = cursor.getString(cursor.getColumnIndex("choice_a"));
        mChoiceD = cursor.getString(cursor.getColumnIndex("choice_d"));
        mChoiceC = cursor.getString(cursor.getColumnIndex("choice_c"));
        mChoiceB = cursor.getString(cursor.getColumnIndex("choice_b"));
        mCorrectAnswer = cursor.getString(cursor.getColumnIndex("correct_ans"));
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
    public ContentValues toContentValue(){
        ContentValues cv = new ContentValues();
        cv.put("ID",mId);
        cv.put("comp_id",mCompId);
        cv.put("choice_a",mChoiceA);
        cv.put("choice_b",mChoiceB);
        cv.put("choice_c",mChoiceC);
        cv.put("choice_d",mChoiceD);
        cv.put("correct_ans",mCorrectAnswer);
        return cv;
    }
    /**
     * @deprecated
     * 用getTestId代替。*/
    public String getId() {
        return mId;
    }

    public String getCompId() {
        return mCompId;
    }

    public String getChoiceA() {
        return mChoiceA;
    }

    public String getChoiceB() {
        return mChoiceB;
    }

    public String getChoiceC() {
        return mChoiceC;
    }

    public String getChoiceD() {
        return mChoiceD;
    }
    /**
     * @see TestItem*/
    @Override
    public String getTestId() {
        return mId;
    }
    /**
     * @see TestItem*/
    @Override
    public String getType() {
        return DataManager.COMPONENT;
    }
}
