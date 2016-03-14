package org.leibnizcenter.rechtspraak.markup;

import org.crf.utilities.TaggedToken;
import org.leibnizcenter.rechtspraak.util.Xml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;


/**
 * Created by maarten on 28-2-16.
 */
public class RechtspraakToken extends TaggedToken<RechtspraakElement, Label> {
    // XML tags
    public static final String TAG_SECTION = "section";
    public static final String TAG_TITLE = "title";
    public static final String TAG_TEXT = "text";
    public static final String TAG_NR = "nr";

    public RechtspraakToken(Element token, Label tag) {
        super(new RechtspraakElement(token), tag);
    }

    @Override
    public String toString() {
        return "[" + this.getTag() + "]: " + getToken().getTextContent().trim();
    }

}
