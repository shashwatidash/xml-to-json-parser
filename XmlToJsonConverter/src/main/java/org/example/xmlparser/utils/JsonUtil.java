package org.example.xmlparser.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil {

    /**
     * Adds a key-value pair to the JSON object. If the key already exists,
     * the values are combined into a JSONArray.
     *
     * @param jsonObject The JSON object to update.
     * @param key        The key to add or update.
     * @param value      The value to associate with the key.
     */
    public static void checkAndPutJson(JSONObject jsonObject, String key, Object value) {
        if (jsonObject.has(key)) {
            JSONArray list = new JSONArray();
            Object obj = jsonObject.get(key);
            if (obj instanceof JSONArray) {
                for (int i = 0; i < ((JSONArray) obj).length(); i++) {
                    list.put(((JSONArray) obj).getJSONObject(i));
                }
            } else {
                list.put(obj);
            }
            list.put(value);
            jsonObject.put(key, list);
        }
        else {
            jsonObject.put(key, value);
        }
    }
}
