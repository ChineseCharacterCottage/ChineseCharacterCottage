package ecnu.chinesecharactercottage.ModelsBackground;

/**
 * Created by Shensheng on 2017/3/21.
 */

public class ShapeCharItem extends CharItem {
    String mVideo;
    public ShapeCharItem(CharItem charItem){
        super(charItem.toJSON());
    }
    public String getVideo(){
        return mVideo;
    }
}
