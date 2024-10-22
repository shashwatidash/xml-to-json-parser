package org.example.xmlparser.test;

import org.example.xmlparser.JsonHandler;
import org.example.xmlparser.XmlParser;
import org.example.xmlparser.exception.XmlParsingException;
import org.json.JSONObject;
import org.w3c.dom.Document;

public class XmlParserTest {
    public static void main(String[] args) {
        testConvertXmlToJson();
    }

    private static void testConvertXmlToJson() {
        String xmlInput = "<Response><ResultBlock><MatchDetails><Match><Entity>John</Entity><Score>35</Score></Match><Match><Entity>Doe</Entity><Score>50</Score></Match></MatchDetails></ResultBlock></Response>";
        String xmlOutput = "{\"Response\":{\"ResultBlock\":{\"MatchDetails\":{\"Match\":[{\"Entity\":\"John\",\"Score\":\"35\"},{\"Entity\":\"Doe\",\"Score\":\"50\"}]}}}}";
        JsonHandler jsonHandler = new JsonHandler();
        XmlParser parser = new XmlParser();
        try {
            Document document = parser.parseXmlInput(xmlInput);
            parser.cleanXmlTree(document);
            JSONObject jsonObject = jsonHandler.traverseAndConstructJsonFromXml(document);
            if (jsonObject.toString().equals(xmlOutput)) {
                System.out.println("Test passed! Successful conversion");
            }
            else {
                System.out.println("Failed");
            }
        } catch (XmlParsingException e) {
            System.out.println(e.getMessage());
        }
    }
}
