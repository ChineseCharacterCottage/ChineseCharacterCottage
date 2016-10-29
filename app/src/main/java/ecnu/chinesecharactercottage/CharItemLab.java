package ecnu.chinesecharactercottage;

import android.content.Context;
import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shensheng on 2016/9/13.
 * A class keep an ArrayList of CharItem.
 */

public class CharItemLab {

    private static CharItemLab sCharItemLab=null;
    private static final String RESOURCES_FILENAME="resources.json";

    private static final String fileName="userdata.json";
    private ArrayList<CharItem> mCharItems;
    private HashMap<String,CharItem> mHashItems;

    private Context mContext;

    public static CharItemLab getLab(Context c)throws IOException,JSONException{
        if(sCharItemLab==null) {
            sCharItemLab = new CharItemLab(c.getApplicationContext());
        }
        return sCharItemLab;
    }

    public static CharItemLab getLabWithoutContext() throws Exception{
        if(sCharItemLab==null){
            throw new Exception("No lab here.");
        }
        return sCharItemLab;
    }

    public void saveCharItems()throws JSONException,IOException{
        JSONArray array=new JSONArray();
        for(CharItem a:mCharItems){
            array.put(a.toJSON());
        }

        Writer writer=null;
        try{
            OutputStream out=mContext.openFileOutput(fileName,Context.MODE_PRIVATE);
            writer=new OutputStreamWriter(out);
            writer.write(array.toString());
        }finally{
            if(writer!=null){
                writer.close();
            }
        }
    }
    private CharItemLab(Context context) throws IOException,JSONException{
        mContext=context.getApplicationContext();
        loadCharacters();
    }

    public CharItem getCharItem(String id){
        /*
        for(CharItem a:mCharItems){
            if(a.getId().equals(id)){
                return a;
            }
        }*/
        return mHashItems.get(id);
    }



    public CharItem[] findByRadical(String radical){
        ArrayList<CharItem> ret=new ArrayList<>();
        for(CharItem a:mCharItems){
            if(radical.equals(a.getRadical())){
                ret.add(a);
            }
        }
        return ret.toArray(new CharItem[ret.size()]);
    }

    public ArrayList<CharItem> getCharItems(){
        return mCharItems;
    }

    private void loadCharacters()throws IOException,JSONException{
        mCharItems=new ArrayList<>();
        mHashItems=new HashMap<>();
        BufferedReader reader=null;
        try{
            InputStream in=mContext.openFileInput(fileName);
            reader=new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString=new StringBuilder();
            String line=null;
            while((line=reader.readLine())!=null){
                jsonString.append(line);
            }
            JSONArray array=(JSONArray)new JSONTokener(jsonString.toString()).nextValue();
            for(int i=0;i<array.length();i++){
                CharItem ci=new CharItem(array.getJSONObject(i));
                mCharItems.add(ci);
                mHashItems.put(ci.getId(),ci);
            }

        }catch (FileNotFoundException e){
            try {
                InputStream in = mContext.getResources().getAssets().open(RESOURCES_FILENAME);
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder jsonString = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }
                JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
                for (int i = 0; i < array.length(); i++) {
                    CharItem ci=new CharItem(array.getJSONObject(i));
                    mCharItems.add(ci);
                    mHashItems.put(ci.getId(),ci);
                }
            }catch (FileNotFoundException ex){
                System.exit(1);
            }
        }finally {
            if(reader!=null){
                reader.close();
            }
        }
    }

}
