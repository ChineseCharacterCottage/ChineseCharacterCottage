package ecnu.chinesecharactercottage.modelsBackground;

import android.content.ContentValues;

/**
 * Created by Shensheng on 2017/3/6.
 */

public abstract class TestItem {
    abstract public Object getCorrectAnswer();
    abstract public ContentValues toContentValue();
    abstract public String getTestId();
    abstract public String getType();
}
