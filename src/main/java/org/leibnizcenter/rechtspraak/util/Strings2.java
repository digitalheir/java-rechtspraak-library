package org.leibnizcenter.rechtspraak.util;

import java.util.*;
import java.util.function.Function;

/**
 * Created by maarten on 31-3-16.
 */
public class Strings2 {
    public static boolean lastCharIs(String s, char c) {
        return s.length() > 0 && s.charAt(s.length() - 1) == c;
    }


    public static boolean firstCharIs(String s, char c) {
        return s.length() > 0 && s.charAt(0) == c;
    }

    public static String appendChars(Collection<Character> chars) {
        StringBuilder sb = new StringBuilder(chars.size());
        chars.forEach(sb::append);
        return sb.toString();
    }

    public static List<Integer> findChars(String s, Collection<Character> chars, int stopAfter) {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            if (chars.contains(s.charAt(i))) l.add(i);
            if (stopAfter <= l.size()) break;
        }
        return l;
    }

    public static int firstNonWhitespaceCharIsAny(String s, Collection<Character> chars) {
        return firstNonWhitespaceCharIsAny(s, chars, 0);
    }

    public static int firstNonWhitespaceCharIsAny(String s, Collection<Character> chars, int from) {
        for (int i = from; i < s.length(); i++) {
            char c = s.charAt(i);
            if (chars.contains(c)) {
                return i;
            } else if (!Character.isWhitespace(c)) {
                break;
            }
        }
        return -1;
    }

    public static boolean hasAtLeastOneLetter(String s) {
        for (int i = 0; i < s.length(); i++) if (Character.isLetter(s.charAt(i))) return true;
        return false;
    }
}
