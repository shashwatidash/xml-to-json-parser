package org.example.parser;

import java.io.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * A class to convert an XML document into a JSON object, process its data,
 * compute scores, and write the output to a file.
 */
public class XmlToJsonConverter {

    public int totalScore = 0;

    /**
     * Recursively removes unnecessary text nodes (e.g., whitespaces) from the XML document.
     * This method modifies the document tree in place.
     *
     * @param root The root node to process.
     */
    public void processTree(Node root) {
        NodeList nodes = root.getChildNodes();

        for (int idx = 0; idx < nodes.getLength(); idx++) {
            Node item = nodes.item(idx);
            if (item.getNodeType() == Node.TEXT_NODE) {
                if (item.getNodeValue().trim().isEmpty()) {
                    root.removeChild(item);
                    idx--;
                    continue;
                }
            }
            if (item.hasChildNodes()) {
                processTree(item);
            }
        }
    }

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
                checkAndPutJson(jsonObj, key, value);
            }
        }

        NodeList nodes = root.getChildNodes();
        for (int idx = 0; idx < nodes.getLength(); idx++) {
            Node item = nodes.item(idx);
            System.out.println("Node name: " + item.getNodeName());

            if (item.getChildNodes().getLength() == 1 &&
                item.getLastChild().getNodeType() == Node.TEXT_NODE) {
                checkAndPutJson(jsonObj, item.getNodeName(), item.getLastChild().getNodeValue());
                continue;
            }

            JSONObject children = traverseAndConstructJsonFromXml(item);
            checkAndPutJson(jsonObj, item.getNodeName(), children);
        }
        return jsonObj;
    }

    /**
     * Adds a key-value pair to the JSON object. If the key already exists,
     * the values are combined into a JSONArray.
     *
     * @param jsonObject The JSON object to update.
     * @param key        The key to add or update.
     * @param value      The value to associate with the key.
     */
    public void checkAndPutJson(JSONObject jsonObject, String key, Object value) {
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

    /**
     * Converts the given XML string into a JSON object and processes its content.
     *
     * @param xmlInput The XML input as a string.
     * @return The resulting JSON Object
     */
    public JSONObject convertXmlToJson(String xmlInput) {
        JSONObject jsonObject = new JSONObject();
        try {
            // Parse the XML document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlInput)));

            processTree(doc);
            jsonObject = traverseAndConstructJsonFromXml(doc);
            
        } catch (Exception e) {
            System.out.println("Error occurred while parsing XML doc: " + e.getMessage());
        }
        return jsonObject;
    }

    /**
     * Writes the given string content to a file.
     *
     * @param content The content to write to the file.
     */
    public static void writeStringToFile(String content) {
        try (FileWriter fileWriter = new FileWriter("output")) {
            fileWriter.write(content);
            System.out.println("Successfully wrote output to file.");
        } catch (IOException e) {
            System.out.println("Error occurred while writing to file: " + e.getMessage());
        }
    }

    /**
     * The main method to initiate XML to JSON conversion.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        String fullText = "";
        try (BufferedReader br = new BufferedReader(new FileReader("input1.xml"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            fullText = sb.toString();
        } catch (IOException e) {
            System.out.println("Error occurred while reading input file: " + e.getMessage());
        }

        XmlToJsonConverter obj = new XmlToJsonConverter();
        JSONObject jsonObject = obj.convertXmlToJson(fullText);

        obj.traverseTreeAndAddScoreTotal(jsonObject);
        obj.addNewJsonNode(jsonObject);

        System.out.println(jsonObject);
        writeStringToFile(jsonObject.toString());
    }
}