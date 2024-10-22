package org.example.parser;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class XmlToJsonConverterTest {

    public static void main(String[] args) {
        testConvertXmlToJson();
    }

    /**
     *  Read output file and verify content
     *
     */
    public static void testConvertXmlToJson() {
        String xmlInput = "<Response><ResultBlock><MatchDetails><Match><Entity>John</Entity><Score>35</Score></Match><Match><Entity>Doe</Entity><Score>50</Score></Match></MatchDetails></ResultBlock></Response>";

        XmlToJsonConverter xmlToJsonConverter = new XmlToJsonConverter();
        JSONObject jsonObject = xmlToJsonConverter.convertXmlToJson(xmlInput);
        xmlToJsonConverter.traverseTreeAndAddScoreTotal(jsonObject);
        xmlToJsonConverter.addNewJsonNode(jsonObject);

        try (FileWriter fileWriter = new FileWriter("testOutput")) {
            fileWriter.write(jsonObject.toString());
            System.out.println("Successfully wrote to test output file.");
        } catch (IOException e) {
            System.out.println("Error occurred while writing to test output file: " + e.getMessage());
        }

        try {
            String result = new String(Files.readAllBytes(Paths.get("testOutput")));
            System.out.println("Testing convertXmlToJson: ");
            if (result.contains("\"TotalMatchScore\":\"85\"")) {
                System.out.println("Test Passed: TotalMatchScore found.");
            } else {
                System.out.println("Test Failed: TotalMatchScore not found.");
            }
        } catch (IOException e) {
            System.out.println("Error occurred while reading output file: " + e.getMessage());
        }
    }
}
