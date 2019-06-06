package io.referrals.lib.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
    public static String getSafeString(JSONObject json, String key) {
        try {
            if (json.has(key)) {
                return json.getString(key);
            } else {
                return "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static JSONArray getSafeJSONArray(JSONObject json, String key) {
        try {
            if (json.has(key)) {
                return json.getJSONArray(key);
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
