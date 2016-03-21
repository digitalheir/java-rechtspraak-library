package org.leibnizcenter.rechtspraak.markup.docs.nameparser;

import cc.mallet.types.Token;
import gate.util.GateException;
import org.leibnizcenter.rechtspraak.features.KnownSurnamesNl;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.markup.docs.features.Patterns;
import org.leibnizcenter.rechtspraak.util.TextPattern;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by maarten on 14-3-16.
 */
public class Names {
    // Personal titles
    public static final String KNOWN_TITLE =
            "(?:BA\\.?|[BbMm]\\.?[Ss][Cc]\\.?|LLM|MA|MPhil\\.?|P\\.?[Hh]\\.?D\\.?" +
                    "|[Bb]acc\\.?|[Cc]and\\." +
                    "|(?:[Dd]e ?)?[Hh]eer" +
                    "|[Dd][Rr][Ss]\\.?|[Dd]s\\.|[Dd][Hh]?r\\.(?:\\.h\\.c\\.)?|[Dd]?[Hh]r\\.|[Ii]ngr?\\.|[Ii]r\\.|[Kk]and\\.|lic\\." +
                    "|[Mm]eneer|[Mm]evrouw" +
                    "|[mM][Rr][Ss]?\\." +
                    "|[Mm]vr\\.|[Mm]w\\.|[Pp]rof(?:\\.|essor))";
    public static final String TITLELOWCONF = "(?:[\\p{L}]{2,3}\\.)";
    // Person roles
    public static final String ROLE_SINGULAR =
            "(?:[Aa]dv(?:\\.|ocaa?te?)(?:[ -][Gg]eneraal)?"
                    + "|[Aa]mbtenaar(?: [Vv]an(?: [Dd]e)? (?:[Ss]taat|[Gg]emeente|[Pp]rovincie))?"
                    + "|(?:[Ww]aarnemend[ -])?[Gg]riffier"
                    + "|[Gg]emachtigde"
                    + "|[Ll]id(?: [Vv]an [Dd]e(?: (?:enkel|meer)voudige)? kamer)"
                    + "|[Oo]fficier [Vv](?:\\.|an) [Jj]ustitie"
                    + "|[Pp]rocureur(?:[ -][Gg]eneraal)?"
                    + "|[Rr]aads?(?:vrouw|man|heer)"
                    + "|(?:[Rr]echter)(?:[ -][Cc]ommissaris)?" +
                    ")";
    public static final String ROLE_MULTIPLE =
            "(?:[Aa]dv(?:ctn\\.|ocate[sn])(?:[ -][Gg]eneraal)?"
                    + "|[Aa]mbtena(?:ren|ars)(?: [Vv]an(?: [Dd]e)? (?:[Ss]taat|[Gg]emeente|[Pp]rovincie))?"
                    + "|(?:[Ww]aarnemend[ -])?[Gg]riffiers"
                    + "|[Gg]emachtigden"
                    + "|[Ll]eden(?: [Vv]an [Dd]e(?: (?:enkel|meer)voudige)? kamer)"
                    + "|[Oo]fficier(?:s|en) [Vv](?:\\.|an) [Jj]ustitie"
                    + "|[Pp]rocureurs(?:[ -][Gg]eneraal)?"
                    + "|[Rr]aads?(?:vrouwen|mannen|heren)"
                    + "|(?:[Rr]echters)(?:[ -][Cc]ommissaris)?"
                    + "|(?:[Rr]echter(?:[ -]))?(?:[Cc]ommissarissen)"
                    + "|[Vv]oorzitter" +
                    ")";
    /**
     * ex. [A.], [Th.], [C.]
     */
    public static final String INITIAL_WITHOUT_PERIOD = "\\p{Lu}\\p{L}{0,2}";
    public static final String STRICT_INITIAL = INITIAL_WITHOUT_PERIOD + "\\.";
    public static final String STRICT_INITIALS = STRICT_INITIAL + "(?: {0,2}?" + STRICT_INITIAL + "){0,10}";
    public static final String LOOSE_INITIALS = INITIAL_WITHOUT_PERIOD + "(?:[\\. ] ?" + INITIAL_WITHOUT_PERIOD + "){0,10}\\.?";

    // Initials
    // First names
    public static final String TOLERANT_FIRSTNAME_SINGLE = "\\p{Lu}\\p{Ll}{0,10}\\p{Ll}";
    public static final String TOLERANT_FIRSTNAME = TOLERANT_FIRSTNAME_SINGLE
            + "(?:-" + TOLERANT_FIRSTNAME_SINGLE + ")?";
    // Last names
    public static final String VANDE = "[\\p{L}']{1,3}";
    /**
     * ex. van der Laan
     */
    public static final String SINGLETOLERANTLASTNAME = "(?:(?:" + VANDE + " ){0,3}(?:\\p{L}{0,5}\\p{Lu}(?:\\p{Ll}[\\p{L}-]{0,15})?\\p{Ll}))";
    /**
     * ex. 'van der Laan-Wijngaerde'
     * ex. 'de Beer de Laer Dupont'
     */
    public static final String TOLERANTLASTNAME = "(?:(?:" + SINGLETOLERANTLASTNAME + ")+"
            // Arbitrary amount of hyphens
            + "(?:-" + SINGLETOLERANTLASTNAME + "){0,5})";
    //Token.string ==~ ".*[aeiouy].*", //At least one vowel...
    public static final String STRICTINITIALSNAMEWITHMULTIPLEINITIALS = "((" +
            KNOWN_TITLE +
            ")*(" +
            STRICT_INITIAL +
            ")(" +
            STRICT_INITIAL +
            ")+(" +
            TOLERANTLASTNAME +
            "))";
    /**
     * Matches any non-space strings
     */
    public static final Pattern TOKEN_REGEX = Pattern.compile("[^\\s]+");
    public static final String TOLERANTFULLNAME_WITH_OPTIONAL_ROLE = "(?:"
            + TOLERANTFULLNAME
            + "(?:, {0,2}(" + ROLE_SINGULAR + "))?"
            + ")";
    public static final Pattern TITLED_NAME = Pattern.compile(TOLERANT_TITLED_NAME);
    public static final String TOLERANTFULLNAME_2_TO_4 =
            "" + TOLERANTFULLNAME_WITH_OPTIONAL_ROLE + ""
                    + "(?:[,;] {0,2}" + TOLERANTFULLNAME_WITH_OPTIONAL_ROLE + "){0,3}"
                    + "[,;]? {0,2}en[,;]? {0,2}\\b"
                    + TOLERANTFULLNAME_WITH_OPTIONAL_ROLE + "";
    private static final String TOLERANT_FIRSTNAMES = "(?:\\b" + TOLERANT_FIRSTNAME + "(?: {1,2}" + TOLERANT_FIRSTNAME + "){0,5})";
    public static final String TOLERANT_FIRST_NAME_AND_OR_INITIALS =
            "(?:" + TOLERANT_FIRSTNAMES
                    + "(?: {0,2}\\b" + LOOSE_INITIALS + ")?"
                    + "|" + LOOSE_INITIALS + ")";
    /////////////////////////////////////////
    private static final String KNOWN_TITLES = "(?:" + KNOWN_TITLE + "(?: {0,2}" + KNOWN_TITLE + "){0,4})";
    public static final String TOLERANTFULLNAME_STRICT_INITIALS =
            "(?:(" + KNOWN_TITLES + ") {0,2})?"
                    + "(?:(" + STRICT_INITIALS + ") {0,2})"
                    + "(" + TOLERANTLASTNAME + ")";
    /**
     * Ex. [mr. Vox], [mr. A.D.W. de Heyde]
     */
    public static final String TOLERANT_TITLED_NAME =
            "(" + KNOWN_TITLES + ") {0,2}"
                    + "(" + TOLERANT_FIRST_NAME_AND_OR_INITIALS + " {0,2})"
                    + "(" + TOLERANTLASTNAME + ")";
    /**
     * [Vincent Willems]
     */
    public static final String TOLERANTFULLNAME =
            "(?:(" + KNOWN_TITLES + ") {0,2})?"
                    + "(?:(" + TOLERANT_FIRST_NAME_AND_OR_INITIALS + ") {0,2})"
                    + "(" + TOLERANTLASTNAME + ")";
    private static final String VERTEGENWOORDIGD_DOOR = "(?:(?:" +
            "(?:vert(?:eg)?enwoordigd|bijgestaan|laten vertegenwoordigen|laten bijstaan)" +
            "|" +
            "(?:\\p{L}{0,5}ge\\p{L}{3,}(?:en|d|t)$))" +
            " {1,2}door)";
    private static final String ALLEN_ALS = "[,: ]{0,2}(?:alle(?:n|maal)?)?[,: ]{0,2}?(?:als)?";
    private final static String ALS = "(?:[^\\p{L}]{0,2}als[^\\p{L}]{1,3}|,[^\\p{L}]{0,3})";


    ////////////
//// Rules
////////////


    ///////////////////////////////////////////////////
    /* `!Sentence` was added to most macros so that last names don't flow over into new sentences.
     * If we don't provision for this, we get cases like
     * <p>
     * ```
     * w.g [van de Berg
     * Voorzitter] ambtenaar van Staat
     * ```
     * <p>
     * i.e., we should ignore the word 'Voorzitter'
     */
    private static final String INTEGENWOORDIGHEIDVAN = "in(?: (?:het|de))? (?:bijzijn|tegenwoordigheid) van";


    public static List<Name> getNames(Matcher matcher, boolean checkSurname) {
        List<Name> names = new ArrayList<>();
        while (matcher.find()) {
            String lastName = matcher.group(3);
            if (!checkSurname || KnownSurnamesNl.matches(lastName)) {
                names.add(
                        new Name(
                                matcher.start(),
                                matcher.end(),
                                new Span(matcher.start(1), matcher.end(1), matcher.group(1)),
                                new Span(matcher.start(2), matcher.end(2), matcher.group(2)),
                                new Span(matcher.start(3), matcher.end(3), lastName)
                        ));
            }
        }
        return names;
    }

    public static void main(String[] args) throws GateException, IOException, URISyntaxException {

    }

    @Deprecated
    public static List<Span> parseTokens(String s) {
        Matcher m = TOKEN_REGEX.matcher(s);
        List<Span> spans = new ArrayList<>(s.length() / 3);
        while (m.find()) {
            spans.add(new Span(m.start(), m.end()));
        }
        return spans;
    }

    public static boolean nameFound(String s) {
        for (NamePatterns p : NamePatterns.values()) {
            if (p.getNames(s).size() > 0) {
                return true;
            }
        }
        return false;
    }


    public enum NamePatterns implements Patterns.UnnormalizedTextContains {
        /**
         * All spans that sort-of look like a name
         */
        LowConfidenceName(Pattern.compile(TOLERANTFULLNAME)),
        HighConfidenceInTegenwoordigheidVanAls(Pattern.compile(
                "(" + INTEGENWOORDIGHEIDVAN + ")"
                        + "(" + TOLERANTFULLNAME + ")"
                        + "(als|,)"
                        + ROLE_SINGULAR
        )),
        HighConfidenceInTegenwoordigheidVanDe(Pattern.compile(
                "(" + INTEGENWOORDIGHEIDVAN + " de)"
                        + ROLE_SINGULAR
                        + "(" + TOLERANTFULLNAME + ")")),
        /**
         * Was getekend. FirstName de la LooksLikeLastname
         */
        WasGetekend(Pattern.compile("(?:(?:(?:w(?:as)? {1,2})?(?:get(?:ekend)?\\.?))|w\\.?g\\.?)\\s{0,3}" + TOLERANTFULLNAME + "")),
        /**
         * prof. FirstName de la FamiliarLastname
         */
        TitledNameKnownSurname(TITLED_NAME, true),
        /**
         * prof. S.T. R.I. C.T. de la TolerantLastName.
         */
        StrictInitialsTolerantName(Pattern.compile(TOLERANTFULLNAME_STRICT_INITIALS)),
        /**
         * prof. S.T. R.I. C.T. de la FamiliarLastName.
         */
        StrictInitialsKnownSurname(Pattern.compile(TOLERANTFULLNAME_STRICT_INITIALS), true),
        /**
         * advocaat [mr. A.B. van Wiel]
         */
        AdvocaatPre(Pattern.compile(
                "(?:[Aa]dvocaat)"
                        + "[:; ]{0,3}\\b"
                        + TOLERANTFULLNAME
        )),
        /**
         * gewezen door [mr. A.B. van Wiel]
         */
        GewezenDoor((Pattern.compile(
                "gewezen {0,2}door" +
                        "[:;, ]{0,3}(?:" + TOLERANTFULLNAME + ")"
        ))),
        /**
         * HighConfidence bijgestaan door mr. A. B. van der Werf, gemachtigde
         */
        GexxxDoorRolePost((Pattern.compile(
                VERTEGENWOORDIGD_DOOR
                        + "(?:[:;, ]{0,3}" + TOLERANTFULLNAME + ")"
                        + ALS
                        + "(?:[:; ]{0,3}(\\b" + ROLE_SINGULAR + "))")

        ), Constants.MATCHER_TOLERANT_NAME_WITH_ROLE),
        /**
         * HighConfidence: (?:de {1,2})?rechter mr. A. B. van der Werf heeft concludeerde
         */
        DeXConcludeert(Pattern.compile("(" + ROLE_SINGULAR + ")"
                + "[:; ]{0,3}" + TOLERANTFULLNAME_WITH_OPTIONAL_ROLE
                + "(?:[:; ]{0,3}heeft)?"
                + "[:; ]{0,3}(?:ge)?concludeer(?:t|d)e?"), (matcher) -> {
            List<Name> names = new ArrayList<>();
            while (matcher.find()) {
                String lastName = matcher.group(4);
                Name name = new Name(
                        matcher.start(),
                        matcher.end(),
                        new Span(matcher.start(2), matcher.end(2), matcher.group(2)),
                        new Span(matcher.start(3), matcher.end(3), matcher.group(3)),
                        new Span(matcher.start(4), matcher.end(4), lastName),
                        new Span(matcher.start(1), matcher.end(1), matcher.group(1))
                );
                names.add(name);
            }
            return names;
        }),
        /**
         * Medium conf
         */
        RolePre(Pattern.compile(
                "" + ROLE_SINGULAR + ""
                        + "[:; ]{1,3}"
                        // Having two or more initials makes the name more probable, or alternatively a title
                        + TOLERANTFULLNAME),
                true),
        /**
         * high confidence
         * mr A de B als X
         */
        NameAlsX(
                Pattern.compile("(?:(" + ROLE_SINGULAR + ")[;: ]{0,2})?\\b" + TOLERANTFULLNAME_WITH_OPTIONAL_ROLE
                        + "[,:; ]{0,2}" + ALS + "[:; ]{0,2}\\b(" + ROLE_SINGULAR + ")"), (matcher) -> {
            List<Name> names = new ArrayList<>();
            while (matcher.find()) {
                String lastName = matcher.group(4);
                Name name = new Name(
                        matcher.start(),
                        matcher.end(),
                        new Span(matcher.start(2), matcher.end(2), matcher.group(2)),
                        new Span(matcher.start(3), matcher.end(3), matcher.group(3)),
                        new Span(matcher.start(4), matcher.end(4), lastName),
                        new Span(matcher.start(1), matcher.end(1), matcher.group(1)),
                        new Span(matcher.start(5), matcher.end(5), matcher.group(5)),
                        new Span(matcher.start(6), matcher.end(6), matcher.group(6))
                );
                names.add(name);
            }
            return names;
        }),


        // Name groups
        /**
         * mrs. [TOLERANT_NAMES_WITH_OPTIONAL_ROLE]
         */
        Meesters(Pattern.compile(
                "([Mm][Rr][Ss][\\.: ]{0,2})"
                        + "(" + TOLERANTFULLNAME_2_TO_4 + ")"
                        //Followed by the group role possibly
                        + "(?:" + ALLEN_ALS
                        + "[:;, ]{0,2}(" + ROLE_MULTIPLE + "|" + ROLE_SINGULAR + "))?"
        ), Constants.MATCHER_TODO),
        GexxxDoorMultiple(Pattern.compile(
                "" + VERTEGENWOORDIGD_DOOR + ""
                        + "[:;, ]{0,2}(" + TOLERANTFULLNAME_2_TO_4 + ")"
                        + "(?:" + ALLEN_ALS + ""
                        //Followed by the group role possibly
                        + "[:;, ]{0,2}(" + ROLE_MULTIPLE + "|" + ROLE_SINGULAR + "))?"
        ), Constants.MATCHER_TODO),
        RolePreMultiple((Pattern.compile(
                "(" + ROLE_MULTIPLE + ")"
                        + "[:;, ]{0,2}" + TOLERANTFULLNAME_2_TO_4
        )),
                Constants.MATCHER_TODO);

//        //// Ex. [mr. Vox], [mr. A.D.W. de Heyde], [Vincent Willems]
//        public static final String SEMITOLERANTFULLNAME = "((" +
//                KNOWN_TITLE +
//                ")*((" +
//                TOLERANT_FIRSTNAME +
//                ")*|(" +
//                STRICT_INITIAL +
//                ")*)(" +
//                TOLERANTLASTNAME +
//                "))";

        ///////////////////


        public static Set<NamePatterns> set = EnumSet.allOf(NamePatterns.class);
        private final TextPattern pattern;
        private final boolean checkSurname;
        private final Function<Matcher, List<Name>> handleMatcher;


        NamePatterns(Pattern pattern) {
            this(pattern, false);
        }


        NamePatterns(Pattern pattern, boolean checkIfSurnameIsKnown) {
            this.checkSurname = checkIfSurnameIsKnown;
            this.handleMatcher = null;
            this.pattern = new NameTextPattern(this.name(), pattern);
        }

        NamePatterns(Pattern pattern, Function<Matcher, List<Name>> handleMatcher) {
            this.pattern = new NameTextPattern(this.name(), pattern);
            this.handleMatcher = handleMatcher;
            this.checkSurname = false;
        }

        public static void setFeatureValues(Token t, RechtspraakElement token) {
            set.forEach((p) -> {
                if (Patterns.matches(p, token))
                    t.setFeatureValue(p.name(), 1.0);
            });
        }

        public List<Name> getNames(String s) {
            Matcher matcher = pattern.pattern.matcher(s);
            if (handleMatcher != null) {
                return handleMatcher.apply(matcher);
            } else {
                return Names.getNames(matcher, checkSurname);
            }
        }

        @Override
        public boolean matches(String s) {
            List<Name> names = getNames(s);
            return names.size() > 0;
        }

        private static class Constants {
            public static final Function<Matcher, List<Name>> MATCHER_TODO = (matcher) -> {
                ArrayList<Name> names = new ArrayList<>();
                while (matcher.find()) {
//                    System.out.println(matcher.group(1));
//                    System.out.println(matcher.group(2));
//                    System.out.println(matcher.group(matcher.groupCount()));
                    names.add(new Name(-1, -1, Name.NON_MATCHING_SPAN, Name.NON_MATCHING_SPAN, Name.NON_MATCHING_SPAN));
                }
                return names;//todo
            };
            public static final Function<Matcher, List<Name>> MATCHER_TOLERANT_NAME_WITH_ROLE = (matcher) -> {
                List<Name> names = new ArrayList<>();
                while (matcher.find()) {
                    String lastName = matcher.group(3);
                    Name name = new Name(
                            matcher.start(),
                            matcher.end(),
                            new Span(matcher.start(1), matcher.end(1), matcher.group(1)),
                            new Span(matcher.start(2), matcher.end(2), matcher.group(2)),
                            new Span(matcher.start(3), matcher.end(3), lastName),
                            new Span(matcher.start(4), matcher.end(4), matcher.group(4))
                    );
                    names.add(name);
                }
                return names;
            };
        }

        private static class NameTextPattern extends TextPattern {
            public NameTextPattern(String name, Pattern compile) {
                super(name, compile);
            }

            @Override
            public boolean matches(String text) {
                return super.matches(text);
            }
        }
    }

    @Deprecated
    public enum PreRole {
        adv("adv.", "advctn."),
        advocaat("advocaat", "advocaten"),
        advocaatGeneraal("advocaat-generaal", "advocaten-generaal"),
        griffier("griffier", "griffiers"),
        officier("officier van justitie", "officieren van justitie"),
        procureur("procureur", "procureurs"),
        raadman("raadman", "raadmannen"),
        raadsheer("raadsheer", "raadsheren"),
        raadsman("raadsman", "raadsmannen"),
        raadsvrouw("raadsvrouw", "raadsvrouwen"),
        raadvrouw("raadvrouw", "raadvrouwen"),
        rechterCommissaris("rechter-commissaris", "rechter-commissarissen"),
        rechter("rechter", "rechters"),
        waarnemend("waarnemend-griffier", "waarnemend-griffiers");

        public final String multiple;
        public final String singular;

        PreRole(String singular, String multiple) {
            this.singular = singular;
            this.multiple = multiple;
        }
    }

    @Deprecated
    public enum AlsRole {
        adv("adv.", "advctn."),
        advocaat("advocaat", "advocaten"),
        advocaatGeneraal("advocaat-generaal", "advocaten-generaal"),
        ambtenaar("ambtenaar", "ambtenaren"),
        ambtenaarVanStaat("ambtenaar van Staat", "ambtenaren van Staat"),
        ambtenaarVanGemeente("ambtenaar van de gemeente", "ambtenaren van de gemeente"),
        ambtenaarVanPro("ambtenaar van de provincie", "ambtenaren van de provincie"),
        gemachtigde("gemachtigde", "gemachtigden"),
        griffier("griffier", "griffiers"),
        lid("lid", "leden"),
        lidVanEnkelvoudigeKamer("lid van de enkelvoudige kamer", "leden van de enkelvoudige kamer"),
        lidVanMeervoudigeKamer("lid van de meervoudige kamer", "leden van de meervoudige kamer"),
        officier("officier van justitie", "officieren van justitie"),
        procureur("procureur", "procureurs"),
        raad("raad", "raadmannen"),
        raadman("raadman", "raadsheren"),
        raadsheer("raadsheer", "raadsmannen"),
        raadsman("raadsman", "raadsvrouwen"),
        raadsvrouw("raadsvrouw", "raadvrouwen"),
        raadvrouw("raadvrouw", "raden"),
        rechter("rechter", "rechters"),
        rechterCommissaris("rechter-commissaris", "rechter-commissarissen"),
        voorzitter("voorzitter", "voorzitters");

        public final String multiple;
        public final String singular;

        AlsRole(String singular, String multiple) {
            this.singular = singular;
            this.multiple = multiple;
        }
    }

    public static class Span {
        /**
         * inclusive start
         */
        public final int start;
        /**
         * exclusive start
         */
        public final int end;
        public final String string;

        @Deprecated
        public Span(int start, int end) {
            this(start, end, null);
        }

        public Span(int start, int end, String string) {
            this.start = start;
            this.end = end;
            this.string = string;
        }
    }

    public static class Name {
        public static final Span NON_MATCHING_SPAN = new Span(-1, -1, null);
        public final int start;
        public final int end;
        public final Span firstName;
        public final Span titles;
        public final Span lastName;
        public final Span role;
        public final Span role2;
        public final Span role3;

        public Name(int start, int end, Span titles, Span firstName, Span lastName) {
            this(start, end, titles, firstName, lastName, NON_MATCHING_SPAN);
        }

        public Name(int start, int end, Span titles, Span firstName, Span lastName, Span role) {
            this(start, end, titles, firstName, lastName, role, NON_MATCHING_SPAN);
        }

        public Name(int start, int end, Span titles, Span firstName, Span lastName, Span role, Span role2) {
            this(start, end, titles, firstName, lastName, role, role2, NON_MATCHING_SPAN);
        }

        public Name(int start, int end, Span titles, Span firstName, Span lastName, Span role, Span role2, Span role3) {
            this.start = start;
            this.end = end;
            this.titles = titles.start >= 0 ? titles : NON_MATCHING_SPAN;
            this.firstName = firstName.start >= 0 ? firstName : NON_MATCHING_SPAN;
            this.lastName = lastName.start >= 0 ? lastName : NON_MATCHING_SPAN;
            this.role = role.start >= 0 ? role : NON_MATCHING_SPAN;
            this.role2 = role2.start >= 0 ? role2 : NON_MATCHING_SPAN;
            this.role3 = role3.start >= 0 ? role3 : NON_MATCHING_SPAN;
        }
    }
}
