package ecnu.chinesecharactercottage;

/**
 * Created by Shensheng on 2016/12/1.
 */

public class RadicalItem {
    private String[] mExamples;
    private String mShape;
    private String mId;
    public RadicalItem(String[] examples,String shape,String id) {
        mId=id;
        mExamples=examples;
        mShape=shape;
    }
    public String getName(){
        return null;
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
}
