package org.leibnizcenter.rechtspraak.tagging.crf.features;

import com.google.common.io.LineReader;
import org.leibnizcenter.util.TextPattern;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Created by maarten on 15-3-16.
 */
public class EmploysNl {
    public static final HashSet<TextPattern> patterns;

    /**
     * Parse words from file
     */
    static {
        InputStream is = EmploysNl.class
                .getResourceAsStream(
                        "/names/gazetteer/employs/dutch_employs.lst"
                );
        LineReader lr = new LineReader(new InputStreamReader(is));
        patterns = new HashSet<>(1500);
        try {
            String line = lr.readLine();
            while (line != null) {
                String name = line.trim();
                if (name.length() > 0) {
                    patterns.add(new TextPattern(name, Pattern.compile(
                            "\\b" + name + "\\b",
                            Pattern.CASE_INSENSITIVE
                    )));
                }
                line = lr.readLine();
            }
        } catch (IOException e) {
            throw new Error();
        }
    }


    public static boolean findAny(String s) {
        for (TextPattern tp : patterns) {
            if (tp.find(s)) return true;
        }
        return false;
    }
}
