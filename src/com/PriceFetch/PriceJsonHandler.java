package com.PriceFetch;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by lee on 2017/8/21.
 *
 * @author: lee
 * create time: 下午2:38
 */
public class PriceJsonHandler {

    private JsonObject priceJson;
    private JsonArray priceArray;

    public PriceJsonHandler(){
        priceJson = new JsonObject();
        priceArray = new JsonArray();
    }

    public void setResult(Boolean result, String message){
        this.priceJson.addProperty("state",result);
        this.priceJson.addProperty("errMsg",message);
    }

    public void setSiteInfo(String site, String price, String url){
        JsonObject object = new JsonObject();
        object.addProperty("site_name",site);
        object.addProperty("price",price);
        object.addProperty("buy_url",url);
        this.priceArray.add(object);
    }

    public JsonObject getPriceJson(){
        priceJson.add("site_prices",priceArray);
        return this.priceJson;
    }

    public static JsonObject getErrorJson(String message){
        PriceJsonHandler jsonHandler = new PriceJsonHandler();
        jsonHandler.setResult(false,message);
        return jsonHandler.getPriceJson();
    }

}
