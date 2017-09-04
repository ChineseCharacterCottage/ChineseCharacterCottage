package ecnu.chinesecharactercottage.modelsBackground;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @author 匡申升
 * 获取用户的一些全局的设置。这个类已经废弃了，建议删除。本文件不作具体注释。
 * @deprecated
 */

public class UserConfigSetter {
    private static final String FILENAME="userConfig.txt";
    private static JSONObject sJSON=null;
    private static void initJSON(Context context)throws IOException,JSONException{
        if(sJSON==null) {
            FileInputStream fis = context.getApplicationContext().openFileInput(FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            sJSON = new JSONObject(sb.toString());
            fis.close();
        }
    }
    public static String getValue(Context context,String key){
        String ret;
        try {
            initJSON(context);
            ret=sJSON.getString(key);
        }catch (FileNotFoundException e){
            sJSON=new JSONObject();
            return null;
        }catch (IOException e){
            Log.d("UserConfigSetter",e.toString());
            return null;
        }catch (JSONException e){
            Log.d("UserConfigSetter",e.toString());
            return null;
        }
        return ret;
    }
    public static void setValue(Context context,String key,String value){
        try {
            initJSON(context);
            sJSON.put(key,value);
            FileOutputStream fos=context.getApplicationContext().openFileOutput(FILENAME,Context.MODE_PRIVATE);
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(sJSON.toString());
            fos.close();
        }catch (Exception e){
            Log.d("UserConfigSetter",e.toString());
        }
    }
}
