package org.leibnizcenter.rechtspraak.tokens.numbering.interfaces;

import org.leibnizcenter.rechtspraak.tokens.numbering.*;
import org.leibnizcenter.util.Regex;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by maarten on 16-2-16.
 */
public interface NumberingNumber {
    Pattern START_WITH_ARABIC = Pattern.compile("^\\s*([0-9]+).*", Pattern.CASE_INSENSITIVE);


    /**
     * Whether this numbering is plausibly a succedent to given numbering (e.g., '1.1' to '1'; 'V' to 'IV')
     *
     * @param n2
     * @return
     */
    boolean isSuccedentOf(NumberingNumber n2);

    boolean isFirstNumbering();

    boolean equalsSansTerminal(Object obj);

    String getTerminal();

    static boolean isFirstNumberInSequence(int checkFor) {
        return checkFor == 1 || checkFor == 0;
    }


    static NumberingNumber m() {
        return new ArabicNumbering(3);
    }

    /**
     * @param num      String for numbering
     * @param terminal Trailing character, may be null
     * @return
     * @throws NumberFormatException If the given numbers is probably not a numbering numbering, often because it is too
     *                               big (docs don't really grow into thousands of sections.)
     */
    static NumberingNumber parse(String num, String terminal) throws NumberFormatException {
        if (terminal != null) terminal = terminal.trim();
        if (terminal != null && terminal.length() <= 0) terminal = null;

        num = num.trim();
        if (num.contains(".")) {
            return new SubSectionNumber(num, terminal);
        } else {
            return parseSingleToken(num, terminal);
        }
    }

    static Function<NumberingNumber, Boolean> isPlausiblySuccedent(NumberingNumber startsWithNumbering) {
        return (precedingNumber) -> startsWithNumbering.isSuccedentOf(precedingNumber) || (
                startsWithNumbering instanceof SubSectionNumber
                        && startsWithNumbering.isFirstNumbering()
                        && ((SubSectionNumber) startsWithNumbering).firstSubsectionOf().isSuccedentOf(precedingNumber));
    }

    static NumberingNumber startsWithNumbering(String textContent) {
        Matcher numberMatcher = Regex.START_WITH_NUM.matcher(textContent);
        if (numberMatcher.find()) {
            if (!Regex.YYYY_MM_DD.matcher(textContent).find()
                    && !Regex.DD_MON_YYYY.matcher(textContent).find()
                    ) {
                // If it's not a date...
                String num = numberMatcher.group(2);
                String terminal = numberMatcher.group(4);
//                System.out.println("Num: "+num);
//                System.out.println("Terminal: "+terminal);
                if (num.charAt(0) == '(') {
                    terminal = '(' + terminal;
                    num = num.substring(1);
                }
                return NumberingNumber.parse(num, terminal);
            }
        }


        return null;
    }


    static SingleTokenNumbering parseFullInteger(String num) {
        return parseSingleToken(num, null);
    }

    static SingleTokenNumbering parseSingleToken(String num, String terminal) {
        if (ListMarking.startsWithListMarking(num)) return new NonNumericNumbering(num, terminal);
        else if (START_WITH_ARABIC.matcher(num).matches()) {
            return new ArabicNumbering(num, terminal);
        } else if (num.length() == 1) {
            if (AmbiguousAlphabeticOrRomanNumeral.isAmbiguousCharacter(num.charAt(0))) {
                return new AmbiguousAlphabeticOrRomanNumeral(num, terminal);
            } else return new AlphabeticNumberingImpl(num, terminal);
        } else return RomanNumeral.from(num, terminal);
    }

    static boolean isSuccedentOf(NumberingNumber second, NumberingNumber first) {
        return second != null && first != null && second.isSuccedentOf(first);
    }
//
//    class TooBigForFormattingException extends NumberFormatException {
//        public TooBigForFormattingException(String number) {
//            super("Number is too big to be a numbering numbering: " + number);
//        }
//    }


}
