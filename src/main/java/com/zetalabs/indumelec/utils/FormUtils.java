package com.zetalabs.indumelec.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FormUtils {
    public static Map<String, String> getMap(JSONArray array){
        Map<String, String> map = new HashMap<>();

        for (Object obj: array){
            JSONObject jsonObject = (JSONObject) obj;
            map.put(jsonObject.get("name").toString(), jsonObject.get("value").toString());
        }

        return map;
    }
}
