package org.leibnizcenter.rechtspraak.tokens.features;

import com.google.common.io.LineReader;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.util.TextPattern;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Created by maarten on 15-3-16.
 */
public class KnownSurnamesNl {
    public static final HashSet<TextPattern> patterns;
    public static final HashSet<String> names;
    private static final Pattern S_PREFIX = Pattern.compile("^[^\\p{L}]s.");

    static {
        InputStream is = KnownSurnamesNl.class
                .getResourceAsStream(
                        "/jape-select-names/gazetteer/dutch-surnames/family_names_common.lst"
                );
        LineReader lr = new LineReader(new InputStreamReader(is));
        patterns = new HashSet<>(125000);
        names = new HashSet<>(125000);
        try {
            String line = lr.readLine();
            while (line != null) {
                String name = line.trim();
                if (name.length() > 0) {
                    patterns.add(new TextPattern(name, Pattern.compile(regexName(name))));
                    names.add(name);
                }
                line = lr.readLine();
            }
        } catch (IOException e) {
            throw new Error();
        }
    }

    private static String regexName(String placename) {
        return (Character.isLetter(placename.charAt(0)) ?
                "\\b" : "[^\\p{L}]?")
                + S_PREFIX.matcher(placename)
                .replaceAll("s.") + "\\b";
    }

    public static boolean findAny(String s) {
        if (names.contains(s)) return true;
        for (TextPattern tp : patterns) {
            if (tp.find(s)) return true;
        }
        return false;
    }

    public static boolean matches(String s) {
        if (names.contains(s)) return true;
        /**
         * Married ppl may have a double name such as  mr. E.J. de Lange-Bekker, which does not appear in the
         * lookup list for last names.
         */
        if (s.contains("-")) {
            String[] subNames = s.split("-");
            for (String name : subNames) if (!names.contains(name)) return false;
            return true;
        }
        return false;
    }

    public static boolean startsWithKnownName(RechtspraakElement token) {
        String text = token.getTextContent();
        for (String name : names) if (text.startsWith(name)) return true;
        return false;
    }
}
