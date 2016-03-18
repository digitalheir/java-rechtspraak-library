package org.leibnizcenter.rechtspraak.features;

import com.google.common.io.LineReader;
import org.leibnizcenter.rechtspraak.util.TextPattern;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Created by maarten on 15-3-16.
 */
public class PlaceNamesInNL {

    public static final HashSet<TextPattern> patterns;
    private static final Pattern S_PREFIX = Pattern.compile("^[^\\p{L}]s.");

    static {
        InputStream is = PlaceNamesInNL.class
                .getResourceAsStream(
                        "/jape-select-names/gazetteer/dutch-place-names/dutch_place_names.lst"
                );
        LineReader lr = new LineReader(new InputStreamReader(is));
        patterns = new HashSet<>(6500);
        try {
            String line = lr.readLine();
            while (line != null) {
                String placename = line.trim();
                if (placename.length() > 0) {
                    Pattern pattern = Pattern.compile(
                            regexPlacename(placename),
                            // A long name probably doesn't collide with homonyms
                            placename.length() < 6 ? 0 : Pattern.CASE_INSENSITIVE
                    );
                    patterns.add(new TextPattern(placename, pattern));
                }
                line = lr.readLine();
            }
        } catch (IOException e) {
            throw new Error();
        }
    }

    private static String regexPlacename(String placename) {
        return (Character.isLetter(placename.charAt(0)) ?
                "\\b" : "[^\\p{L}]?")
                + S_PREFIX.matcher(placename)
                .replaceAll("s.") + "\\b";
    }

    public static boolean findAny(String s) {
        for (TextPattern tp : patterns) {
            if (tp.find(s)) return true;
        }
        return false;
    }
}
