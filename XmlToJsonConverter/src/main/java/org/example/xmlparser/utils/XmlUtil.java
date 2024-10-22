package org.example.xmlparser.utils;


import org.example.xmlparser.exception.XmlParsingException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class XmlUtil {

    public static Document parseXml(String xmlInput) throws XmlParsingException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlInput)));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new XmlParsingException("Error in parsing", e);
        }
    }

    /**
     * Recursively removes unnecessary text nodes (e.g., whitespaces) from the XML document.
     * This method modifies the document tree in place.
     *
     * @param root The root node to process.
     */
    public static void processTree(Node root) {
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
}
