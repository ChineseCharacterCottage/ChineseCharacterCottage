package ecnu.chinesecharactercottage.modelsBackground;

/**
 * Created by Shensheng on 2017/5/1.
 */

public class Knowledge {
    public Knowledge(String pId,String pTitle,String pVideo,String pText){
        id=pId;
        title=pTitle;
        video=pVideo;
        text=pText;
    }
    public final String title;
    public final String video;
    public final String text;
    public final String id;

    public String getTitle(){return title;}
}
