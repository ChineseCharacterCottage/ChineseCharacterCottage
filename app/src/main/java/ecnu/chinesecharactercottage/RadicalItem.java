package ecnu.chinesecharactercottage;

import java.io.Serializable;

/**
 * Created by Shensheng on 2016/12/1.
 */

public class RadicalItem implements Serializable {
    private String[] mExamples;
    private String mShape;
    private String mId;
    private String mName;
    public RadicalItem(String[] examples,String shape,String id,String name) {
        mId=id;
        mExamples=examples;
        mShape=shape;
        mName=name;
    }
    public String getName(){
        return mName;
    }
    public String getRadical(){
        return mShape;
    }
    public String getId(){
        return mId;
    }
    public String[] getExamples(){
        return mExamples;
    }
    @Override
    public String toString(){
        return mShape+" "+mName;
    }
}
