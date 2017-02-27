package ecnu.chinesecharactercottage.ModelsBackground;

import java.io.Serializable;

/**
 * Created by Shensheng on 2016/12/9.
 */

public class ComponentItem implements Serializable {
    private String mGId;
    private String mShape;
    private String[] mChars;
    private String mExp;
    private String mVoS;
    public ComponentItem(String shape,String[] chars,String explanation,String voiceOrShape,String globalID){
        mShape=shape;
        mChars=chars;
        mExp=explanation;
        mGId=globalID;
        if(voiceOrShape.equals("shape") || voiceOrShape.equals("voice")) mVoS=voiceOrShape;
        else throw new RuntimeException("ComponentItem voiceOrShape must be \"voice\" or \"shape\".");
    }
    @Override
    public String toString(){
        return mShape+" "+mExp+" "+mVoS;
    }
    public String getShape(){
        return mShape;
    }
    public String getGlobalId(){
        return mGId;
    }
    public String getExplanation(){
        return mExp;
    }

    public String[] getCharacters(){
        return mChars;
    }

    public String getVoiceOrShape(){
        return mVoS;
    }
}
