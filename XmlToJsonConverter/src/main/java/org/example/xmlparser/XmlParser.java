package org.example.xmlparser;

import org.example.xmlparser.exception.XmlParsingException;
import org.example.xmlparser.utils.XmlUtil;
import org.w3c.dom.Document;

public class XmlParser {
    public Document parseXmlInput(String xmlInput) throws XmlParsingException {
        return XmlUtil.parseXml(xmlInput);
    }

    public void cleanXmlTree(Document doc) {
        XmlUtil.processTree(doc);
    }
}
