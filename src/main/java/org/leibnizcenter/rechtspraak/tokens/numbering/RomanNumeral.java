package org.leibnizcenter.rechtspraak.tokens.numbering;

import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.FullNumber;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.FullSectionNumber;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.NumberingNumber;

import java.security.InvalidParameterException;
import java.util.Objects;

/**
 * An object of type RomanNumeral is an integer between 1 and 3999.  It can
 * be constructed either from an integer or from a string that represents
 * a Roman numeral in this range.  The function toString() will return a
 * standardized Roman numeral representation of the numbering.  The function
 * toInt() will return the numbering as a value of type int.
 */
public abstract class RomanNumeral extends Number implements FullSectionNumber {

    private static int[] numbers = {1000, 900, 500, 400, 100, 90,
            50, 40, 10, 9, 5, 4, 1};

    /* The following arrays are used by the toString() function to construct
       the standard Roman numeral representation of the numbering.  For each i,
       the numbering numbers[i] is represented by the corresponding string, letters[i].
    */
    private static String[] letters = {"M", "CM", "D", "CD", "C", "XC",
            "L", "XL", "X", "IX", "V", "IV", "I"};
    private final int num;   // The numbering represented by this Roman numeral.
    private String roman;
    private String terminal;

    /**
     * Constructor.  Creates the Roman numbering with the int value specified
     * by the parameter.  Throws a NumberFormatException if arabic is
     * not in the range 1 to 3999 inclusive.
     */
    public RomanNumeral(int arabic) {
        if (arabic < 1)
            throw new NumberFormatException("Value of RomanNumeral must be positive.");
        if (arabic > 3999)
            throw new NumberFormatException("Value of RomanNumeral must be 3999 or less.");
        num = arabic;
    }


    /*
     * Constructor.  Creates the Roman numbering with the given representation.
     * For example, RomanNumeral("xvii") is 17.  If the parameter is not a
     * legal Roman numeral, a NumberFormatException is thrown.  Both upper and
     * lower case letters are allowed.
     */
    public RomanNumeral(String romanNumber, String terminal) {
        if (terminal != null) {
            terminal = terminal.trim();
            if (terminal.length() <= 0) terminal = null;
        }
        this.terminal = terminal;

        if (romanNumber.length() == 0)
            throw new NumberFormatException("An empty string does not define a Roman numeral.");

        roman = romanNumber.trim().toUpperCase();  // Convert to upper case letters.

        int i = 0;       // A position in the string, roman;
        int arabic = 0;  // Arabic numeral equivalent of the part of the string that has
        //    been converted so far.

        while (i < roman.length()) {

            char letter = roman.charAt(i);        // Letter at current position in string.
            int number = letterToNumber(letter);  // Numerical equivalent of letter.

            i++;  // Move on to next position in the string

            if (i == roman.length()) {
                // There is no letter in the string following the one we have just processed.
                // So just add the numbering corresponding to the single letter to arabic.
                arabic += number;
            } else {
                // Look at the next letter in the string.  If it has a larger Roman numeral
                // equivalent than numbering, then the two letters are counted together as
                // a Roman numeral with value (nextNumber - numbering).
                int nextNumber = letterToNumber(roman.charAt(i));
                if (nextNumber > number) {
                    // Combine the two letters to get one value, and move on to next position in string.
                    arabic += (nextNumber - number);
                    i++;
                } else {
                    // Don't combine the letters.  Just add the value of the one letter onto the numbering.
                    arabic += number;
                }
            }

        }  // end while

        if (arabic > 3999)
            throw new NumberFormatException("Roman numeral must have value 3999 or less.");

        num = arabic;

    } // end constructor


    /**
     * Find the integer value of letter considered as a Roman numeral.  Throws
     * NumberFormatException if letter is not a legal Roman numeral.  The letter
     * must be upper case.
     */
    private int letterToNumber(char letter) {
        switch (letter) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                throw new NumberFormatException(
                        "Illegal character \"" + letter + "\" in Roman numeral " + roman);
        }
    }


    /**
     * Return the standard representation of this Roman numeral.
     */
    public String toString() {
        return canonicalRepresentation() + (terminal == null ? "" : terminal);
    }


    /**
     * Return the value of this Roman numeral as an int.
     */
    @Override
    public int intValue() {
        return num;
    }

    @Override
    public long longValue() {
        return num;
    }

    @Override
    public float floatValue() {
        return num;
    }

    @Override
    public double doubleValue() {
        return num;
    }

    @Override
    public boolean isSuccedentOf(NumberingNumber precedent) {
        if (precedent instanceof SubSectionNumber) precedent = ((SubSectionNumber) precedent).get(0);

        if (precedent instanceof FullNumber) {
            if (((FullNumber) precedent).mainNum() + 1 != num) return false;
            // We can't mix Arabics and Romans
            return precedent instanceof RomanNumeral;
        } else if (precedent instanceof SingleCharNumbering) {
            return this instanceof AmbiguousAlphabeticOrRomanNumeral
                    && ((SingleCharNumbering) this).getCharacter() == ((SingleCharNumbering) precedent).nextChar();
        } else {
            throw new InvalidParameterException();
        }
    }

    @Override
    public int mainNum() {
        return num;
    }

    @Override
    public boolean isFirstNumbering() {
        return num == 1;
    }

    @Override
    public String getTerminal() {
        return terminal;
    }

    @Override
    public String canonicalRepresentation() {
        return toString(num);
    }

    public static String toString(int n) {
        String roman = "";  // The roman numeral.
        // N represents the part of num that still has
        //   to be converted to Roman numeral representation.
        for (int i = 0; i < numbers.length; i++) {
            while (n >= numbers[i]) {
                roman += letters[i];
                n -= numbers[i];
            }
        }
        return roman;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RomanNumeral
                && equalsSansTerminal(obj)
                && Objects.equals(((RomanNumeral) obj).getTerminal(),terminal);
    }

    @Override
    public boolean equalsSansTerminal(Object obj) {
        return obj instanceof RomanNumeral && ((RomanNumeral) obj).mainNum() == this.mainNum();
    }

    @Override
    public FullSectionNumber succ() {
        return RomanNumeral.from(toString(num + 1), terminal);
    }

    public static RomanNumeral from(String num, String terminal) {
        if (num.trim().length() == 1) return new AmbiguousAlphabeticOrRomanNumeral(num, terminal);
        else return new MultiCharRomanNumeral(num, terminal);
    }
}