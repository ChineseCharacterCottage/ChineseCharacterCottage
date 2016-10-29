package ecnu.chinesecharactercottage;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;



public final class UserFavorite {
    private static final String FILE_NAME="favorite.cccd";
    private static UserFavorite sUser=null;
    private HashMap<String,CharItem> mHashMap;
    private Context mContext;
    public static UserFavorite getInstance(Context context){
        if(sUser==null){
            try {
                sUser = new UserFavorite(context);
            }catch (Exception e){

            }
        }
        return sUser;
    }

    public boolean isFavorite(String id){
        return mHashMap.get(id)!=null;
    }
    public void add(String id){
        CharItemLab lab=null;
        try {
            lab = CharItemLab.getLabWithoutContext();
            CharItem a=lab.getCharItem(id);
            mHashMap.put(a.getId(),a);
        }catch (Exception e){
            Log.e("DEBUG",e.toString());
        }
    }
    public void remove(String id){
        mHashMap.remove(id);
    }

    private UserFavorite(Context context) {
        mHashMap=new HashMap<>();
        mContext=context.getApplicationContext();
        try{
            loadFromFile();
        }catch (Exception e){
            Log.d("DEBUG",e.toString());
        }
    }

    public void saveInFile()throws IOException{
        CharItem[] array=mHashMap.values().toArray(new CharItem[mHashMap.size()]);
        Writer writer=null;
        try {
            OutputStream out=mContext.openFileOutput(FILE_NAME,Context.MODE_PRIVATE);
            writer=new OutputStreamWriter(out);
            StringBuffer a=new StringBuffer();
            for (CharItem c : array) {
                String t=c.getId()+"\r\n";
                a.append(t);
            }
            writer.write(a.toString());
        }catch (Exception e){

        }finally {
            if(writer!=null){
                writer.close();
            }
        }
    }

    private void loadFromFile() throws IOException{
        BufferedReader reader=null;
        try {
            InputStream in=mContext.openFileInput(FILE_NAME);
            reader=new BufferedReader(new InputStreamReader(in));
            String id=null;
            while ((id=reader.readLine())!=null){
                add(id);
            }
        }catch (FileNotFoundException e){
            Log.d("DEBUG",e.toString());
        }finally {
            if(reader!=null){
                reader.close();
            }
        }
    }
}
