package com.Login.Handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * Created by lee on 2017/6/17.
 *
 * @author: lee
 * create time: 下午2:48
 */
public class MyJsonParser {
    public static String GetSessionError(){
        return "{\"state\":\"error\",\"sessionID\":\"\"}";
    }

    public static String GetSessionSuccess(String sessionID, String OpenID){
        return "{\"state\":\"success\",\"sessionID\":\""+sessionID+"\",\"openID\" : \""+OpenID+"\"}";
    }

    public static String SetUserInfoModifyResult(boolean result, String msg){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("state",result);
        jsonObject.addProperty("message",msg);
        return jsonObject.toString();
    }

    public static String CodeJsonToString(String initialString){
        // return the code inside the request json
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(initialString);
        JsonElement element = jsonObject.get("code");
        String result = element.getAsString();

        if(result == null){
            result = "";
        }

        return result;
    }

    public static JsonObject String2Json(String json){
        JsonParser jsonParser = new JsonParser();
        return (JsonObject) jsonParser.parse(json);
    }
}
