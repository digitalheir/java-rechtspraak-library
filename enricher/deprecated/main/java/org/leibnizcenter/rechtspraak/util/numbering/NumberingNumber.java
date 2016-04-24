package org.leibnizcenter.rechtspraak.util.numbering;

import org.leibnizcenter.rechtspraak.util.TextBlockInfo;

import java.util.regex.Pattern;

/**
 * Created by maarten on 16-2-16.
 */
public interface NumberingNumber {
    static Pattern START_WITH_ARABIC = Pattern.compile("^\\s*([0-9]+).*", Pattern.CASE_INSENSITIVE);

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


    static SingleTokenNumbering parseFullInteger(String num) {
        return parseSingleToken(num, null);
    }

    static SingleTokenNumbering parseSingleToken(String num, String terminal) {
        if (TextBlockInfo.Regex.NON_ALPHANUMERIC.matcher(num).find()) {
            return new NonNumericNumbering(num, terminal);
        } else if (terminal != null
                && (terminal.contains(")") || terminal.contains("("))
                && num.matches("[A-Za-z]")) {
            return new AlphabeticNumbering(num, terminal);
        } else if (num.matches("[A-Ha-hJ-Zj-z]")) {
            return new AlphabeticNumbering(num, terminal);
        }
        if (START_WITH_ARABIC.matcher(num).matches()) {
            if (num.length() < 3) {
                return new ArabicNumbering(num, terminal);
            } else {
                throw new TooBigForFormattingException(num);
            }
        } else {
            return new RomanNumeral(num, terminal);
        }
    }

    static boolean isSuccedentOf(NumberingNumber second, NumberingNumber first) {
        return second != null && first != null && second.isSuccedentOf(first);
    }

    /**
     * Whether this numbering is plausibly a succedent to given numbering (e.g., '1.1' to '1'; 'V' to 'IV')
     *
     * @param n2
     * @return
     */
    boolean isSuccedentOf(NumberingNumber n2);

    /**
     * @return The most important numbering as integer. For example, in '2.3.1' it's '2', in 'IX' it's '9'.
     */
    int mainNum();

    boolean isFirstNumbering();

    String getTerminal();


    public static class TooBigForFormattingException extends NumberFormatException {
        public TooBigForFormattingException(String number) {
            super("Number is too big to be a numbering numbering: " + number);
        }
    }
}
