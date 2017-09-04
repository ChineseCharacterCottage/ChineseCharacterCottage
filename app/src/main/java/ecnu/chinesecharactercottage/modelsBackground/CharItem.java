package ecnu.chinesecharactercottage.modelsBackground;



import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * @author 匡申升
 * 汉字实例，表示一个汉字。包含ID、字形、拼音、部首等。
 * @see Readable
 */

public class CharItem implements Readable,Serializable {
    /**
     * 这些是汉字实例的属性的键。在JSON中，用于取出对应的属性值。在数据库中，用于作表的属性名。*/
    private static final String ID="ID";
    public static final String CHARACTER="character_shape";
    public static final String PINYIN="pinyin";
    private static final String WORDS="words";
    public static final String SENTENCE="sentence";
    public static final String EXPLANATION="explanation";
    public static final String RADICAL="radical_id";

    /**
     * 该汉字对应的部首实例。
     * @see RadicalItem*/
    private RadicalItem mRadical;
    /**
     * 该汉字的JSON对象。这里会一直持有一个JSON对象。*/
    private JSONObject mJSON;
    /**
     * 该汉字的ID，该ID在服务器上是唯一的，每个ID仅仅对应一个汉字实例。*/
    private String mId;
    /**
     * 这个汉字所能组成的词语。
     * @see WordItem*/
    private WordItem[] mWords;
    /**
     * 构造函数。封装一个JSON对象为CharItem.
     * 注：做这样的设定是为了兼容以前的代码，重构时可以不要保留这个设定。*/
    public CharItem(JSONObject json) {
        mWords=null;
        mJSON=json;
        mId=get(ID);
    }
    /**
     * 包访问方法。获得这个汉字的部首ID。*/
    String getRadicalId(){
        return get(RADICAL);
    }
    /**
     * 包访问方法，设置这个汉字的部首实例。
     * 注：这里违反了面向对象的单一职责原则，因为DataManager在生成CharItem时发挥了作用。重构时可以修改。
     * @see DataManager
     * @param radical 部首实例。*/
    void setRadical(RadicalItem radical){
        mRadical=radical;
    }
    /**
     * 获取这个汉字的部首实例。*/
    public RadicalItem getRadical(){
        return mRadical;
    }
    /**
     * 获取这个汉字的ID。*/
    public String getId(){
        return mId;
    }
    /**
     * 获取这个汉字JSON里的某个属性。*/
    public String get(String property) {
        String r=null;
        try {
            r=mJSON.getString(property);
        }
        catch (JSONException e){
            Log.d("CharItem",e.toString());
        }
        //当获取的属性是拼音时，对其结果进行处理。
        if(property.equals(PINYIN)){
            return pinyinTranslate(r);
        }
        return r;
    }

    /**
     * 获取这个汉字的组词。
     * @see WordItem*/
    public WordItem[] getWords(){
        if(mWords==null) {
            //数据库上，汉字的组词是以斜杠分割，存在一个字段里的。所以这里把这个字段分开，封装成五个WordItem
            String[] words = get(WORDS).split("/|,");
            ArrayList<WordItem> list = new ArrayList<>();
            for (int i = 0; i < words.length; i += 2) {
                list.add(new WordItem(words[i+1], words[i], "w_" + get("ID") +"_"+ i / 2  + ".mp3"));
            }
            mWords=list.toArray(new WordItem[list.size()]);
        }
        return mWords;
    }
    /**
     * 获得这个汉字的JSON实例。*/
    public JSONObject toJSON(){
        return mJSON;
    }
    /*
    @Override
    public MediaPlayer getMediaPlayer(Context c) {
        MediaPlayer mp=new MediaPlayer();
        try {
            AssetFileDescriptor fd=c.getAssets().openFd(mJSON.getString(PINYIN)+".mp3");
            if(Build.VERSION.SDK_INT<24) {
                mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            }else {
                mp.setDataSource(c.getAssets().openFd(mJSON.getString(PINYIN)+".mp3"));
            }
        }catch (IOException e){
            Log.d("CharItem","Media file not found :"+e.toString());
            return null;
        }catch (JSONException j){
            Log.d("CharItem",j.toString());
        }
        return mp;
    }
    */
    /**
     * 获取媒体文件的键。这个媒体文件是这个汉字的发音。*/
    @Override
    public String getMediaKey(){
        try{
            return mJSON.getString(PINYIN)+".mp3";
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 生成一个匿名的Readable实例，通过这个Readable可以获取该字的例句读音音频文件。
     * @param c 这个参数实际上不需要，但是删除会导致前端大量的修改工作。重构时可以考虑删除。*/
    public Readable getSentenceReadable(Context c){
        return new Readable() {
            /*
            @Override
            public MediaPlayer getMediaPlayer(Context c) {
                MediaPlayer mp=new MediaPlayer();
                try {
                    AssetFileDescriptor fd=c.getAssets().openFd("s_"+get("ID")+".mp3");
                    if(Build.VERSION.SDK_INT<24) {
                        mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                    }else {
                        mp.setDataSource(c.getAssets().openFd("s_"+get("ID")+".mp3"));
                    }
                }catch (IOException e){
                    Log.d("WordItem","Media file not found :"+e.toString());
                    return null;
                }
                return mp;
            }*/
            @Override
            public String getMediaKey(){
                return "s_"+get("ID")+".mp3";
            }
        };
    }
    /**
     * 获取图片的键。可以用它在PictureGetter中获取汉字对应的图片。
     * @see PictureGetter*/
    public String getImageId(){
        return get("ID")+".jpg";
        /*
        Context appContext=context.getApplicationContext();
        AssetManager manager=appContext.getAssets();
        Bitmap image;
        try {
            InputStream stream = manager.open(get("ID")+".jpg");
            image = BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            image = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.imagenotfound);
        }
        return image;*/
    }
    /**
     * 返回ID+拼音。方便调试。*/
    @Override
    public String toString(){
        return get(ID)+" "+get(PINYIN);
    }
    /**
     * 私有静态方法，用于将拼音的格式转换为正常的拼音。
     * 例子：
     *  转换前： wo3 ai4 ni3
     *  转换后： wǒ aì nǐ
     *  根据汉语拼音音调标注的标准：
     *  “a o e i u ü 标调按顺序，
     *  ‘i、u并排标后头’，小ü 碰到j q x y 就脱帽（去掉点）。
     *  a母出现不放过， （即韵母中凡是有a的，标在a上。如lao,标在a上）
     *  没有a母找 o e ， （没有a，但有o 或e的，标在 o 或e 上。如lou标在o上,lei标在e上）
     *  i u并列标在后， （i和 u并列时，标在后面。比如liu,标在u上，gui,标在i 上），
     *  单个韵母不必说。 （单个的韵母，当然就标它上面了）”
     *       （引用自http://blog.sina.com.cn/s/blog_8d8548f50100uxxa.html）
     * */
    private static String pinyinTranslate(String s){
        int d;
        char[] array;
        if(s.charAt(s.length()-1)<='4' && s.charAt(s.length()-1)>='0'){
            d=s.charAt(s.length()-1)-'0';
            array=new char[s.length()-1];
            for(int i=0;i<s.length()-1;++i){
                array[i]=s.charAt(i);
            }
        }
        else
        {
            array=s.toCharArray();
            d=0;
        }
        for(int i=0;i<array.length;++i){
            if(array[i]=='v'){
                if(s.contains("j") || s.contains("q") || s.contains("x")){
                    array[i]='u';
                }
                else
                {
                    array[i]='ü';
                }
            }
        }
        s=new String(array);
        int loc=-1;
        if(s.contains("a")){
            loc=s.indexOf('a');
        }
        else if(s.contains("o"))
        {
            loc=s.indexOf('o');
        }
        else if(s.contains("e"))
        {
            loc=s.indexOf('e');
        }
        else if(s.contains("i") && s.contains("u")){
            int a=s.indexOf('i');
            int b=s.indexOf('u');
            loc=a>b?a:b;
        }
        else if(s.contains("i")){
            loc=s.indexOf('i');
        }
        else if(s.contains("u")){
            loc=s.indexOf('u');
        }
        else if(s.contains("ü")){
            loc=s.indexOf('ü');
        }
        char[][] pylist=
                {
                        {'a','ā','á','ǎ','à'},
                        {'o','ō','ó','ǒ','ò'},
                        {'e','ē','é','ě','è'},
                        {'i','ī','í','ǐ','ì'},
                        {'u','ū','ú','ǔ','ù'},
                        {'ü','ǖ','ǘ','ǚ','ǜ'}
                };
        switch (array[loc]){
            case 'a':
                array[loc]=pylist[0][d];break;
            case 'o':
                array[loc]=pylist[1][d];break;
            case 'e':
                array[loc]=pylist[2][d];break;
            case 'i':
                array[loc]=pylist[3][d];break;
            case 'u':
                array[loc]=pylist[4][d];break;
            case 'ü':
                array[loc]=pylist[5][d];break;
        }
        return new String(array);
    }
}
