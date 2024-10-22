package org.example.xmlparser;

import org.example.xmlparser.utils.JsonUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JsonHandler {
    private int totalScore = 0;

    /**
     * Traverses an XML node tree and constructs a corresponding JSON object.
     *
     * @param root The root node to traverse.
     * @return A JSON object representation of the XML tree.
     */
    public JSONObject traverseAndConstructJsonFromXml(Node root) {
        JSONObject jsonObj = new JSONObject();

        if (root.hasAttributes()) {
            NamedNodeMap nodeMap = root.getAttributes();
            for (int k = 0; k < nodeMap.getLength(); k++) {
                String key = nodeMap.item(k).getNodeName();
                Object value = nodeMap.item(k).getNodeValue();
                JsonUtil.checkAndPutJson(jsonObj, key, value);
            }
        }

        NodeList nodes = root.getChildNodes();
        for (int idx = 0; idx < nodes.getLength(); idx++) {
            Node item = nodes.item(idx);
            System.out.println("Node name: " + item.getNodeName());

            if (item.getChildNodes().getLength() == 1 &&
                    item.getLastChild().getNodeType() == Node.TEXT_NODE) {
                JsonUtil.checkAndPutJson(jsonObj, item.getNodeName(), item.getLastChild().getNodeValue());
                continue;
            }

            JSONObject children = traverseAndConstructJsonFromXml(item);
            JsonUtil.checkAndPutJson(jsonObj, item.getNodeName(), children);
        }
        return jsonObj;
    }

    /**
     * Adds a "MatchSummary" node with the total score under the "ResultBlock" node in the JSON object.
     *
     * @param jsonObject The JSON object to modify.
     */
    public void addNewJsonNode(JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (key.equals("ResultBlock") && value instanceof JSONObject) {
                JSONObject resultBlock = jsonObject.getJSONObject("ResultBlock");
                JSONObject matchSummary = new JSONObject();
                matchSummary.put("TotalMatchScore", totalScore + "");
                resultBlock.put("MatchSummary", matchSummary);
            }
            // If the value is another JSONObject, recurse into it
            if (value instanceof JSONObject) {
                addNewJsonNode((JSONObject) value);
            }
        }
    }

    public void calculateTotalScore(JSONObject jsonObject) {
        traverseTreeAndAddScoreTotal(jsonObject);
    }

    /**
     * Recursively traverses the JSON object and sums up the values of nodes labeled "Score".
     *
     * @param jsonObject The JSON object to traverse.
     */
    public void traverseTreeAndAddScoreTotal(JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            Object obj = jsonObject.get(key);
            if (key.equals("Score")) {
                totalScore += Integer.parseInt(obj.toString());
            }
            if (obj instanceof JSONArray) {
                for (int i = 0; i < ((JSONArray) obj).length(); i++) {
                    if (((JSONArray) obj).opt(i) instanceof JSONObject) {
                        traverseTreeAndAddScoreTotal(((JSONArray) obj).getJSONObject(i));
                    }
                }
            }
            if (obj instanceof JSONObject) {
                traverseTreeAndAddScoreTotal((JSONObject) obj);
            }
        }
    }
}
