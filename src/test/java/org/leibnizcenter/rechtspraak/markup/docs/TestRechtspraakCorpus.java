package org.leibnizcenter.rechtspraak.markup.docs;

import cc.mallet.types.Instance;
import cc.mallet.types.Token;
import cc.mallet.types.TokenSequence;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.leibnizcenter.rechtspraak.TrainWithMallet;
import org.leibnizcenter.rechtspraak.features.info.InfoPatterns;
import org.leibnizcenter.rechtspraak.features.title.TitlePatterns;
import org.leibnizcenter.rechtspraak.util.Xml;
import org.leibnizcenter.rechtspraak.util.numbering.ArabicSectionNumber;
import org.leibnizcenter.rechtspraak.util.numbering.NumberingNumber;
import org.leibnizcenter.rechtspraak.util.numbering.SubSectionNumber;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.leibnizcenter.rechtspraak.features.info.InfoPatterns.InfoPatternsUnormalizedContains.CONTAINS_BRACKETED_TEXT;
import static org.leibnizcenter.rechtspraak.features.Features.LIKELY_PART_OF_LIST;

/**
 * Tests for correct feature vector sequence initialization
 * Created by maarten on 13-3-16.
 */
public class TestRechtspraakCorpus {

    @Test
    public void main() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Collection<List<LabeledToken>> corpus = new HashSet<>(2);
        //List<LabeledToken> richDoc = getTokenList(builder, "ECLI:NL:CBB:2013:345");
        // TODO assert stuff
//        Assert.assertTrue();richDoc
        //corpus.add(richDoc);

        LabeledTokenList paraDoc = getTokenList(builder, "ECLI:NL:RVS:2015:3394");

        corpus.add(paraDoc);
        // TODO assert stuff
        for (LabeledToken token : paraDoc) {
            Assert.assertEquals(token.getTag(), Label.OUT);
        }

        // Test the features that our tokens are assigned
        Instance instance = TrainWithMallet.getInstance(paraDoc, true);
        TokenSequence ts = (TokenSequence) instance.getData();

        for (int i = 0; i < 5; i++) {
            assertFeature(ts.get(i), "WITHIN_FIRST_5_BLOCKS", 1.0);
        }
        // para: 201404465/1/R3.

        assertFeature(ts.get(0), "IS_ALL_CAPS", 1.0);
        assertFeature(ts.get(0), "CONTAINS_LIKELY_ZAAKNR", 1.0);
        //Assert.assertEquals(ts.get(0).getFeatureValue("LESS_THAN_5_WORDS"),1.0,0.001);
        //Assert.assertEquals(ts.get(0).getFeatureValue("FIRST_QUARTILE"), 1.0, 0.001);


        Assert.assertTrue(InfoPatterns.InfoPatternsNormalizedContains.START_W_DATUM.matches(paraDoc.get(1).getToken().normalizedText));
        Assert.assertTrue(InfoPatterns.InfoPatternsNormalizedContains.START_W_AFDELING.matches(paraDoc.get(2).getToken().normalizedText));


        Assert.assertTrue(TitlePatterns.TitlesNormalizedMatchesHighConf.PROCEEDINGS.matches(paraDoc.get(29).getToken().normalizedText));

        /////////////////////////////////////
        //checkContainsInfoPatterns(ts);//todo
        //checkContainsTitlePatterns(ts);//todo

        checkContainsBracketedText(ts);
        checkIsLikelyPartOfList(ts);


        /////////////////////////////////////

        NumberingNumber numbering = paraDoc.get(5).getToken().numbering;
        Assert.assertEquals(numbering.getTerminal(), ".");
        Assert.assertEquals(numbering.mainNum(), 1);
        Assert.assertTrue((InfoPatterns.InfoPatternsNormalizedMatches.EN_VS_CONTRA.matches(paraDoc.get(26).getToken().normalizedText)));
        Assert.assertTrue((InfoPatterns.InfoPatternsNormalizedContains.END_W_ROLE.matches(paraDoc.get(28).getToken().normalizedText)));
        Assert.assertTrue(TitlePatterns.TitlesNormalizedMatchesHighConf.PROCEEDINGS.matches(paraDoc.get(29).getToken().normalizedText));
        Assert.assertTrue(TitlePatterns.TitlesNormalizedMatchesHighConf.CONSIDERATIONS.matches(paraDoc.get(36).getToken().normalizedText));

        numbering = paraDoc.get(42).getToken().numbering;
        Assert.assertTrue(numbering instanceof SubSectionNumber);
        Assert.assertTrue(numbering.equals(new SubSectionNumber(
                Lists.newArrayList(new ArabicSectionNumber(2), new ArabicSectionNumber(2)),
                ".")
        ));
        Assert.assertFalse(numbering.equals(new SubSectionNumber(
                        Lists.newArrayList(new ArabicSectionNumber(2), new ArabicSectionNumber(2)), ""
                )
        ));

        RechtspraakCorpus rsCorpus = new RechtspraakCorpus(corpus);
        Assert.assertTrue(rsCorpus.size() > 0);
    }

    private void checkContainsBracketedText(TokenSequence ts) {
        for (int i = 0; i <= 5; i++) assertFeature(ts.get(i), CONTAINS_BRACKETED_TEXT.name(), 0.0);
        for (int i = 6; i <= 15; i++) assertFeature(ts.get(i), CONTAINS_BRACKETED_TEXT.name(), 1.0);
        for (int i = 16; i <= 17; i++) assertFeature(ts.get(i), CONTAINS_BRACKETED_TEXT.name(), 0.0);
        for (int i = 18; i <= 25; i++) assertFeature(ts.get(i), CONTAINS_BRACKETED_TEXT.name(), 1.0);
    }

    private void assertFeature(Token token, String name, double val) {
        assertTrue(token.getFeatureValue(name) == val);
    }

    private void checkIsLikelyPartOfList(TokenSequence ts) {
        for (int i = 5; i <= 25; i++) if (i != 9) assertFeature(ts.get(i), LIKELY_PART_OF_LIST, 1.0);
        assertFeature(ts.get(37), LIKELY_PART_OF_LIST, 1.0);
        assertFeature(ts.get(38), LIKELY_PART_OF_LIST, 0.0);
        assertFeature(ts.get(39), LIKELY_PART_OF_LIST, 1.0);
        for (int i = 40; i <= 106; i++) {
            assertFeature(ts.get(i), LIKELY_PART_OF_LIST, 0.0);
        }
        assertFeature(ts.get(107), LIKELY_PART_OF_LIST, 1.0);
        assertFeature(ts.get(108), LIKELY_PART_OF_LIST, 0.0);
        assertFeature(ts.get(109), LIKELY_PART_OF_LIST, 1.0);
        assertFeature(ts.get(110), LIKELY_PART_OF_LIST, 1.0);
        for (int i = 111; i <= 246; i++) {
            if (i != 180 && i != 182)
                assertTrue("Failed on " + i, ts.get(i).getFeatureValue(LIKELY_PART_OF_LIST) == 0.0);
        }
        for (int i : new int[]{180, 182, 247, 248})
            assertTrue("Failed on " + i, ts.get(i).getFeatureValue(LIKELY_PART_OF_LIST) == 1.0);
    }

    public LabeledTokenList getTokenList(DocumentBuilder builder, String ecli) throws SAXException, IOException {
        InputStream is = TestRechtspraakCorpus.class.getResourceAsStream("/docs/" + ecli.replaceAll(":", ".") + ".xml");
        Document doc = builder.parse(new InputSource(new InputStreamReader(is)));
        return LabeledTokenList.from(ecli, doc, Xml.getContentRoot(doc));
    }
}
