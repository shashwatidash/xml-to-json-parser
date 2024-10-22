package org.example.xmlparser;

import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        String xmlInput = readFile("input1.xml");
        XmlParser xmlParser = new XmlParser();
        JsonHandler jsonHandler = new JsonHandler();
        try {
            Document document = xmlParser.parseXmlInput(xmlInput);
            xmlParser.cleanXmlTree(document);

            JSONObject jsonObject = jsonHandler.traverseAndConstructJsonFromXml(document);
            jsonHandler.calculateTotalScore(jsonObject);
            jsonHandler.addNewJsonNode(jsonObject);

            writeStringToFile(jsonObject.toString());
        } catch (Exception e) {
            System.out.println("Error parsing:" + e.getMessage());
        }
    }

    public static String readFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            System.out.println("Error occurred while reading input file: " + e.getMessage());
        }
        return sb.toString();
    }

    public static void writeStringToFile(String content) {
        try (FileWriter fileWriter = new FileWriter("output.json")) {
            fileWriter.write(content);
        } catch (IOException e) {
            System.out.println("Error occurred while writing to file: " + e.getMessage());
        }
    }
}
