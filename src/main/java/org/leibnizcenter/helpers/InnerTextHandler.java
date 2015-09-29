package org.leibnizcenter.helpers;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.DomHandler;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

public class InnerTextHandler implements DomHandler<String, StreamResult> {
    private static final String PARAMETERS_START_TAG = "<rs:inhoudsIndicatie>";
    private static final String PARAMETERS_END_TAG = "</rs:inhoudsIndicatie>";
    private StringWriter xmlWriter = new StringWriter();

    public StreamResult createUnmarshaller(ValidationEventHandler errorHandler) {
        return new StreamResult(xmlWriter);
    }

    public String getElement(StreamResult rt) {
        String xml = rt.getWriter().toString();
        int beginIndex = xml.indexOf(PARAMETERS_START_TAG) + PARAMETERS_START_TAG.length();
        int endIndex = xml.indexOf(PARAMETERS_END_TAG);
        return xml.substring(beginIndex, endIndex);
    }

    public Source marshal(String n, ValidationEventHandler errorHandler) {
        try {
            String xml = PARAMETERS_START_TAG + n.trim() + PARAMETERS_END_TAG;
            StringReader xmlReader = new StringReader(xml);
            return new StreamSource(xmlReader);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
