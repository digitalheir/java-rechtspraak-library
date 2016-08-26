package org.leibnizcenter.rechtspraak.enricher;

import org.junit.Assert;
import org.junit.Test;
import org.leibnizcenter.cfg.Grammar;
import org.leibnizcenter.cfg.earleyparser.ParseTreeWithScore;
import org.leibnizcenter.cfg.earleyparser.Parser;
import org.leibnizcenter.cfg.token.Token;
import org.leibnizcenter.cfg.token.Tokens;
import org.leibnizcenter.rechtspraak.tagging.Label;
import org.leibnizcenter.rechtspraak.tokens.LabeledToken;
import org.leibnizcenter.rechtspraak.tokens.text.TextElement;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
//[<start>
//        [#root
//            [DocumentContent
//                [COMPLETE_SECTION_BLOB
//                    [Sections
//                        [Section
//                            [SectionTitle
//                                [SECTION_TITLE_TEXT
//                                    [SECTION_TITLE]
//                                ]
//                            ]
//                            [COMPLETE_SECTION_CONTENT
//                                [SectionContent
//                                    [COMPLETE_SECTION_BLOB
//                                        [Sections
//                                            [Text
//                                                [Text
//                                                    [Text[NEWLINE]]
//                                                    [Text[NEWLINE]]
//                                                ]
//                                                [Text[NEWLINE]]
//                                            ]
//[Section
//        [SectionTitle
//            [NR]
//            [SECTION_TITLE_TEXT[SECTION_TITLE]]
//        ]
//        [COMPLETE_SECTION_CONTENT[SectionContent[
//                                    COMPLETE_SECTION_BLOB[Sections[Text[Text[Text[NEWLINE]]
//                                    [Text[Text[TEXT_BLOCK]][Text[TEXT_BLOCK]]]][Text[TEXT_BLOCK]]][Section[SectionTitle[SECTION_TITLE_TEXT[SECTION_TITLE]]]
//                                    [COMPLETE_SECTION_CONTENT[SectionContent[Text[NEWLINE]]]]
//        ]]]]
//        ]
//]
//                                        ]
//                                    ]
//                                ]
//                            ]
//                        ]
//                    ]
//                ]
//            ]
//        ]
//]
/**
 * Created by maarten on 21-4-16.
 */
public class DocumentGrammarTest {
    @Test
    public void test() throws ParserConfigurationException, IOException, SAXException {
        Grammar grammar = DocumentGrammar.grammar;

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        InputSource is = new InputSource(new StringReader("<root></root>"));
        Document doc = dBuilder.parse(is);

        Element uitspraak = doc.getDocumentElement();
        Assert.assertNotNull(uitspraak);
        TokenTreeLeaf ttl = new TextElement(uitspraak);

        List<Token<LabeledToken>> sentence = Tokens.tokenize(
                new LabeledToken(ttl, Label.SECTION_TITLE),
                new LabeledToken(ttl, Label.NEWLINE),
                new LabeledToken(ttl, Label.NEWLINE),
                new LabeledToken(ttl, Label.NEWLINE),
                new LabeledToken(ttl, Label.NR),
                new LabeledToken(ttl, Label.SECTION_TITLE),
                new LabeledToken(ttl, Label.NEWLINE),
                new LabeledToken(ttl, Label.TEXT_BLOCK),
                new LabeledToken(ttl, Label.TEXT_BLOCK),
                new LabeledToken(ttl, Label.TEXT_BLOCK),
                new LabeledToken(ttl, Label.SECTION_TITLE),
                new LabeledToken(ttl, Label.NEWLINE)
        );

        ParseTreeWithScore chart = Parser.getViterbiParseWithScore(DocumentGrammar.DOCUMENT, grammar, sentence);
        System.out.println(chart);
    }
}