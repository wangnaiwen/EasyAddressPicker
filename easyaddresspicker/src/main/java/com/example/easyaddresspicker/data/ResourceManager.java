package com.example.easyaddresspicker.data;

import android.content.Context;

import com.example.easyaddresspicker.model.ProvinceModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/30 0030.
 */


public class ResourceManager {

    List<ProvinceModel> list = new ArrayList<ProvinceModel>();
    Callable callable;
    Context context;

    public ResourceManager(Context context, Callable callable){
        this.context = context;
        this.callable = callable;
        getDataFromJsonFile();
    }

    /**
     * 从json文件获取数据, 利用多线程获取
     * */
    public void getDataFromJsonFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStreamReader isr = new InputStreamReader(context.getAssets().open("city.json"), "UTF-8");
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    StringBuilder builder = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        builder.append(line);
                    }
                    br.close();
                    isr.close();
                    parseData(builder.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 解析JSON数据
     * */
    private void parseData(String data){
        try {
            if (data.isEmpty()){
                return;
            }
            JSONObject object = new JSONObject(data);
            JSONArray jsonArray = object.getJSONArray("data");
            list = formJsonToArray(jsonArray.toString(), new TypeToken<List<ProvinceModel>>() {
            }.getType());
            callable.onCall(list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 将JSon解析成我们想要的数据类型
     * */
    public static <T> T formJsonToArray(String json, Type t) {
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, t);
    }

    /**
     * 数据回调接口
     * */
    public interface Callable{
        void onCall(List<ProvinceModel> list);
    }
}
