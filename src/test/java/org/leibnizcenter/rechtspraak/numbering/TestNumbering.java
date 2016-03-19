package org.leibnizcenter.rechtspraak.numbering;

import org.junit.Assert;
import org.junit.Test;
import org.leibnizcenter.rechtspraak.util.numbering.ArabicSectionNumber;
import org.leibnizcenter.rechtspraak.util.numbering.RomanNumeral;
import org.leibnizcenter.rechtspraak.util.numbering.SubSectionNumber;

/**
 * Created by maarten on 16-2-16.
 */
public class TestNumbering {
    @Test
    public void testIncrementingNumbers() {
//        ArrayList<NumberingNumber> numbers = Lists.newArrayList(
//                TextBlockInfo.startsWithNumber("I. meme"),
//                TextBlockInfo.startsWithNumber("i.1: submeme"),
//                TextBlockInfo.startsWithNumber("i.1.1: subsubmeme"),
//                TextBlockInfo.startsWithNumber("i.2: submeme"),
//                TextBlockInfo.startsWithNumber("i.3: submeme"),
//                TextBlockInfo.startsWithNumber("i.3.1: submeme"),
//                TextBlockInfo.startsWithNumber("i.3.1.1: submeme"),
//                TextBlockInfo.startsWithNumber("i.3.1.2: submeme"),
//                TextBlockInfo.startsWithNumber("i.3.2: submeme"),
//                TextBlockInfo.startsWithNumber("i.3.2.1: submeme"),
//                TextBlockInfo.startsWithNumber("II. a new meme"),
//                TextBlockInfo.startsWithNumber("ii.0. a new subsubmeme"),
//                TextBlockInfo.startsWithNumber("III. a new meme"),
//                TextBlockInfo.startsWithNumber("IV. a new meme"),
//                TextBlockInfo.startsWithNumber("V. a new meme"),
//                TextBlockInfo.startsWithNumber("VI. a new meme")
//        );
//
//        for (int i = 1; i < numbers.size(); i++) {
//            Assert.assertTrue(
//                    numbers.get(i) + ".isSuccedentOf(" + numbers.get(i - 1) + ")",
//                    numbers.get(i).isSuccedentOf(numbers.get(i - 1))
//            );
//        }
    }

    @Test
    public void testSomeSectionNumbering() {
        ArabicSectionNumber an = new ArabicSectionNumber(2);
        RomanNumeral rn = new RomanNumeral(" iii ", ".");

        Assert.assertTrue(!an.isSuccedentOf(rn));
        Assert.assertTrue(!rn.isSuccedentOf(an)); // Don't mix roman and arabic section numbers

        SubSectionNumber ssn;

        ssn = new SubSectionNumber("1.2.3.1", ".");
        Assert.assertTrue(ssn.firstSubsectionOf().size() == 3);
        Assert.assertTrue(ssn.size() == 4);
        Assert.assertTrue(ssn.firstSubsectionOf().get(2).mainNum() == 3);
        Assert.assertTrue(an.isSuccedentOf(ssn));

        ssn = new SubSectionNumber("2.1", ".");
        Assert.assertTrue(ssn.firstSubsectionOf().size() == 1);
        Assert.assertTrue(ssn.size() == 2);
        Assert.assertTrue(!an.isSuccedentOf(ssn));
        Assert.assertTrue(rn.isSuccedentOf(ssn));
        rn = new RomanNumeral(" iI", ".");
        Assert.assertTrue(!ssn.isSuccedentOf(rn));
        Assert.assertTrue(ssn.isSuccedentOf(an));

        ssn = new SubSectionNumber("1.1.1.0", ".");
        Assert.assertTrue(ssn.firstSubsectionOf().size() == 3);
        Assert.assertTrue(ssn.size() == 4);
        Assert.assertTrue(an.isSuccedentOf(ssn));

        ssn = new SubSectionNumber("0.0.00.00", ".");
        Assert.assertTrue(ssn.firstSubsectionOf().size() == 3);
        Assert.assertTrue(ssn.size() == 4);
        Assert.assertTrue(!an.isSuccedentOf(ssn));
        Assert.assertTrue(!ssn.isSuccedentOf(an));

        ssn = new SubSectionNumber("3.10.1", ".");
        SubSectionNumber ssn2 = new SubSectionNumber("3.30.2", ".");
        Assert.assertTrue(!ssn2.isSuccedentOf(ssn));
    }
}
