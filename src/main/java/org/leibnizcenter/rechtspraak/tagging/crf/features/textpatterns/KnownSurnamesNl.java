package org.leibnizcenter.rechtspraak.tagging.crf.features.textpatterns;

import com.google.common.io.LineReader;
import org.leibnizcenter.rechtspraak.tagging.crf.features.elementpatterns.interfaces.NamedElementFeatureFunction;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.util.StringFeature;
import org.leibnizcenter.util.TextPattern;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import static org.leibnizcenter.rechtspraak.tagging.crf.features.textpatterns.KnownSurnamesNl.Constants.S_PREFIX;


/**
 * Created by maarten on 15-3-16.
 */
//Todo generate tests
public enum KnownSurnamesNl implements StringFeature, NamedElementFeatureFunction {
    containsAnyName(s -> {
        if (Constants.names.contains(s)) return true;
        for (TextPattern tp : Constants.patterns) if (tp.find(s)) return true;
        return false;
    }),
    matchesAnyName(s -> {
        if (Constants.names.contains(s)) return true;
        /**
         * Married ppl may have a double name such as  mr. E.J. de Lange-Bekker, which does not appear in the
         * lookup list for last names.
         */
        if (s.contains("-")) {
            String[] subNames = s.split("-");
            for (String name : subNames) if (!Constants.names.contains(name)) return false;
            return true;
        }
        return false;
    }),
    startsWithName(s -> {
        for (String name : Constants.names) if (s.startsWith(name)) return true;
        return false;
    });

    private final Function<String, Boolean> f;

    static final class Constants {
        static final HashSet<TextPattern> patterns;
        static final HashSet<String> names;
        static final Pattern S_PREFIX = Pattern.compile("^[^\\p{L}]s.");

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
    }

    private static String regexName(String placename) {
        return (Character.isLetter(placename.charAt(0)) ?
                "\\b" : "[^\\p{L}]?")
                + S_PREFIX.matcher(placename)
                .replaceAll("s.") + "\\b";
    }

    @Override
    public boolean apply(String s) {
        Boolean bool = f.apply(s);
        if (bool == null) throw new NullPointerException();
        return bool;
    }

    @Override
    public boolean apply(List<TokenTreeLeaf> element, int ix) {
        Boolean bool = f.apply(element.get(ix).getTextContent());
        if (bool == null) throw new NullPointerException();
        return bool;
    }

    KnownSurnamesNl(Function<String, Boolean> f) {
        this.f = f;
    }
}
