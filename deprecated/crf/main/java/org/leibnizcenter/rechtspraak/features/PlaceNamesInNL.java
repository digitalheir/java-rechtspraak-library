package org.leibnizcenter.rechtspraak.tagging.crf.features;

import cc.mallet.types.Token;
import com.google.common.io.LineReader;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Created by maarten on 15-3-16.
 */
@Deprecated
public class PlaceNamesInNL {

    private static final Pattern NON_LETTER_NON_BRACKET = Pattern.compile("[^\\p{L}\\[\\]]");
    private static final Pattern S_PREFIX = Pattern.compile("^[^\\p{L}]s[^\\p{L}]");
    private static final Pattern T_PREFIX = Pattern.compile("^[^\\p{L}]t[^\\p{L}]");

    private static final Pattern pattern;

    static {
        InputStream is = PlaceNamesInNL.class
                .getResourceAsStream(
                        "/jape-select-names/gazetteer/dutch-place-names/dutch_place_names.lst"
                );
        LineReader lr = new LineReader(new InputStreamReader(is));
        HashSet<String> patterns = new HashSet<>(12500);
        int i = 0;
        try {
            String line = lr.readLine();
            while (line != null && i < 10) {
                String placename = line.trim();
                if (placename.length() > 0) {
                    patterns.add(regexPlacename(placename));
                }
                line = lr.readLine();
                i++;
            }
            pattern = Pattern.compile("\\b(" + String.join("|", patterns) + ")\\b");
        } catch (IOException e) {
            throw new Error();
        }
    }

    private static String regexPlacename(String placename) {

        // Also accept uppercase
        String uppercase = NON_LETTER_NON_BRACKET.matcher(placename).replaceAll("[^\\p{L}]?");
        // For longer placenames, we accept lowercase as well
        String lowercase;
        if (placename.length() > 5) {
            lowercase = NON_LETTER_NON_BRACKET.matcher(placename).replaceAll("[^\\p{L}]?");
        } else {
            lowercase = NON_LETTER_NON_BRACKET
                    .matcher(placename.replaceAll("(\\p{Lu})", ""))
                    .replaceAll("[^\\p{L}]?");
        }
        return "(" + lowercase + ")|(" + uppercase + ")";
    }

    public static void setFeatureValues(Token t, RechtspraakElement token) {
        if (find(token.getTextContent())) {
            t.setFeatureValue("CONTAINS_PLACENAME", 1.0);
        }
    }

    public static boolean find(String s) {
        return pattern.matcher(s).find();
    }
}
