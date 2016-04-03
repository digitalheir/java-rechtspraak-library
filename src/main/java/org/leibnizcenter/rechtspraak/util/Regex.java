package org.leibnizcenter.rechtspraak.util;

import java.util.regex.Pattern;

/**
 * Created by maarten on 31-3-16.
 */
public class Regex {
    public static final Pattern ENDS_WITH_NON_LETTER = Pattern.compile("[^\\p{L}]$");
    public static final Pattern ENDS_WITH_LETTER = Pattern.compile("[\\p{L}]$");
    public static final Pattern S_P_A_C_E_D = Pattern.compile("(?:\\p{L} {1,3}){2,100}\\p{L}");
    public static final Pattern CONSECUTIVE_WHITESPACE = Pattern.compile("\\s+");
    public static final Pattern ALL_DIGITS = Pattern.compile("^[0-9 ]*[0-9][0-9 ]*$");
    public static final Pattern START_W_LOWERCASE_LETTER = Pattern.compile("^\\p{Ll}");
    public static final Pattern START_W_UPPERCASE_LETTER = Pattern.compile("^\\p{Lu}");
    public static final Pattern NON_WHITESPACE = Pattern.compile("[^\\s]");
    public static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^\\p{L} 0-9]");
    public static final Pattern YYYY_MM_DD = Pattern.compile("^[0-9]{4}-[01]?[0-9]-[0-3]?[0-9]\\b");
    public static final Pattern DD_MON_YYYY = Pattern.compile("^[0-3]?[0-9]\\s{0,3}(jan|feb|maart|apr|mei|jun|jul|aug|sep|okt|nov|dec)\\w{0,20}(?:[0-9]{4})?\\b");
    public static final Pattern DEAFDELING = Pattern.compile("^(de )?afdeling", Pattern.CASE_INSENSITIVE);
    public static final Pattern START_W_SECTOR = Pattern.compile("^(de )?sector", Pattern.CASE_INSENSITIVE);
    public static final Pattern START_W_HIERNA = Pattern.compile("^hierna", Pattern.CASE_INSENSITIVE);
    public static final Pattern END_W_ROLE = Pattern.compile("(((ge)(daagd|machtigd)e?)|appelante?|verweerders?|advocaa?t(en)?)" +
            "( te \\p{L}+\\b)?\\s{0,3}[^\\p{L}]{0,20}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern END_W_KAMER = Pattern.compile("kamer$", Pattern.CASE_INSENSITIVE);
    public static final Pattern INZAKE = Pattern.compile("^in ?(de )?zaa?ke?( van)?\\b", Pattern.CASE_INSENSITIVE);
    public static final Pattern INDEZAAK = Pattern.compile("^(?:in|op) (?:de|het) (?:zaak|beroep)", Pattern.CASE_INSENSITIVE);
    public static final Pattern START_W_DATUM = Pattern.compile("^datum", Pattern.CASE_INSENSITIVE);
    public static final Pattern START_W_LOCATIE = Pattern.compile("^locatie", Pattern.CASE_INSENSITIVE);
    public static final Pattern START_W_ZAAK_ROL_NR = Pattern.compile("^(?:kenmerk|(?:(?:zaak|rol).{0,15})?n(?:umme)?[ro]\\.?)",
            Pattern.CASE_INSENSITIVE);
    public static final Pattern START_W_ZAAKNR = Pattern.compile("^(?:(?:zaak|rol|parket|ro).{0,15})?n(?:umme)?[ro]\\.?",
            Pattern.CASE_INSENSITIVE);
    public static final Pattern START_W_GEVESTIGD = Pattern.compile("^.{0,15}(wonend|gevestigd)", Pattern.CASE_INSENSITIVE);

    /**
     * Starts with whitespace, then a decimal or roman numeral
     */
    public static final Pattern START_WITH_NUM = Pattern.compile(
            "^(" +
                    // Leading parenthesis
                    "(?:\\( {0,2})?" +
                    "(" +
                    // Leading numbers
                    "(?:" +
                    "(?:([0-9]+)" +
                    "|(?:i{1,3}\\b)" +
                    "|(?:i?vi{0,3}\\b)" +
                    "|(?:i?xi{0,3}\\b))\\.)*" +
                    //
                    "(?:(?:[0-9]+)" +
                    "|(?:i{1,3}\\b)" +
                    "|(?:i?vi{0,3}\\b)" +
                    "|(?:[a-z]\\b)" +
                    ")" +
                    ")" +
                    // Whitespace
                    "\\s{0,3}" +
                    // Terminal character
                    "([^\\p{L}]{0,2}))",
            Pattern.CASE_INSENSITIVE);
    public static final Pattern START_W_RAAD = Pattern.compile(
            "^(?:de|het)?\\s{0,3}(?:hoge|centrale)?\\s{0,3}(?:raad|college)" +
                    "(?:\\s{0,3}(van|voor))?" +
                    "(?:\\s{0,3}(de|het))?" +
                    "(?:\\s{1,3}\\p{L}{0,20}){0,3}",
            Pattern.CASE_INSENSITIVE);
    public static final Pattern START_W_COURT = Pattern.compile(
            "^(de|het)?\\s{0,3}(?:ge)?rechts?(?:bank|hof)?",
            Pattern.CASE_INSENSITIVE);
    public static final Pattern DE_RECHTBANK = Pattern.compile("de rechtbank", Pattern.CASE_INSENSITIVE);
    public static final Pattern VENNOOTSCHAP = Pattern.compile("((de|het) )?(naamloze|besloten )?vennootschap(pen)?", Pattern.CASE_INSENSITIVE);
    public static final Pattern EN_VS_CONTRA = Pattern.compile("(en|vs|tegen|contra)", Pattern.CASE_INSENSITIVE);

    //
    // Subsection
    //
    public static final Pattern REGEX_SUBSECTION = Pattern.compile("(?:([0-9]+)|(i{1,3}\\b)" +
            "|(i?vi{0,3}\\b)" +
            "|(i?xi{0,3}\\b))(?:\\.(?:([0-9]+)" +
            "|[a-z\\*\\-]{0,3}" +
            "|(i{1,3}\\b)" +
            "|(i?vi{0,3}\\b)" +
            "|(i?xi{0,3}\\b)))+", Pattern.CASE_INSENSITIVE);

    //
    // Date
    //
    private static final String YEAR = "(?:[12][90])?[0-9]{2}";
    private static final String DAY_MONTH = "[0-9]{1,2}";

    public static final Pattern CONTAINS_DATE = Pattern.compile("(?:" + YEAR + "-" + DAY_MONTH + "-" + DAY_MONTH +
                    "|" + DAY_MONTH + "-" + DAY_MONTH + "-" + YEAR +
                    "|" + DAY_MONTH + " {0,3}" +
                    "(" +
                    "jan(\\.|uari)?|feb(\\.|ruari)?" +
                    "|m(aa)?rt\\.?|apr(\\.|il)?|mei|ju[nl](\\.|i)?" +
                    "|aug(\\.|ustus)?|okt(\\.|ober)?" +
                    "|nov(\\.|ember)?|sept?(\\.|t?ember)?|dec(\\.|ember)?" +
                    ")" +
                    " {0,3}" + YEAR +
                    ")",
            Pattern.CASE_INSENSITIVE);
    public static final Pattern CONTAINS_BRACKETED_TEST = Pattern.compile("\\[[\\p{L}0-9 ]{0,25}\\]");
    public static final Pattern CONTAINS_LIKELY_ZAAKNR = Pattern.compile(
            "[0-9]{0,15}(/[A-Z]{0,2}[0-9]{1,5}){1,3}");
    public static final Pattern CONTAINS_REF_TO_WOONPLAATS = Pattern.compile(
            "\\bwoo?n(plaats|ende|achtig)\\b"
    );
    public static final Pattern CONTAINS_REF_TO_PERSONAGE = Pattern.compile(
            "\\b" +
                    "ge[iï]ntimeerden?" +
                    "|belanghebbenden?" +
                    "|vaders?" +
                    "|moeders?" +
                    "|ver(?:weerder|zoeker)s?" +
                    "|man(?:nen)?" +
                    "|vrouw(?:en)?" +
                    "|naam" +
                    "|namen" +
                    "|eiser(?:e?s)?" +
                    "|gedaagden?" +
                    "|app?ell?ante?(?:n|s)?" +
                    "\\b"
    );

    private static final String VERGEMELD = "(?:(?:ge|ver)meld)";
    private static final String VERGEZONDEN = "(?:(?:ge|ver)zonden)";
    private static final String HET_VOLGENDE = "(?: (?:als|het) volg(t|ende))";
    private static final String IN_XXX = "(?: (?:in)(?: (?:de|het))?(?: \\p{L}{3,20}))";

    public static final Pattern HET_VOLGENDE_BERICHT = Pattern.compile("het volgende bericht" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern DE_HET_VOLGENDE = Pattern.compile("((het|de) )volgende", Pattern.CASE_INSENSITIVE);
    public static final Pattern INHOUDT = Pattern.compile("inhoudt" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern GEGEVENS = Pattern.compile("de volgende gegevens" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VERZONDEN = Pattern.compile(VERGEZONDEN + IN_XXX + "?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VOLGENDE = Pattern.compile(HET_VOLGENDE + "" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern ZAKELIJKWEERGEGEVEN = Pattern.compile("zakelijk weergegeven" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern LUIDT_ALS_VOLGT = Pattern.compile("luid(dde|t)? als volgt$", Pattern.CASE_INSENSITIVE);
    public static final Pattern LUIDT = Pattern.compile("luid(t|en)( \\((?: \\p{L}{0,20}){0,10}\\))?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern IS_BEPAALD = Pattern.compile("is (?:als|het) volg(t|ende)? bepaald" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern HET_VOLGENDE_BEPAALD = Pattern.compile(HET_VOLGENDE + " bepaald" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern MEEGEDEELD = Pattern.compile("med?e(?:ge)?deelde?" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern HET_VOLGENDE_MEEGEDEELD = Pattern.compile(HET_VOLGENDE + " med?e(?:ge)?deelde?" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern HET_VOLGENDE_GESCHREVEN = Pattern.compile(HET_VOLGENDE + " geschreven" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern STAAT_VERMELD = Pattern.compile("(staa[nt]|volgende) " + VERGEMELD + IN_XXX + "?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern ALS_VOLGT_BEANTWOORD = Pattern.compile("als volgt( werd(en)?) [gb]e(antwoord|schreven)" + IN_XXX + "?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern TE_LEZEN = Pattern.compile("(is|valt).*te lezen$", Pattern.CASE_INSENSITIVE);
    public static final Pattern BEANTWOORD_ALS_VOLGT = Pattern.compile("beantwoord(dde|t) als volgt$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VERMELD = Pattern.compile("\\b" + VERGEMELD + "$", Pattern.CASE_INSENSITIVE);
    public static final Pattern STAAT = Pattern.compile("in.*\\bstaat\\b( onder meer)?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern DAARIN_STAAT = Pattern.compile("(daar(in)? )?staa[nt]( " + VERGEMELD + ")?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern OVERWOGEN = Pattern.compile("overwogen$", Pattern.CASE_INSENSITIVE);
    public static final Pattern OVERWOOG = Pattern.compile("overwoog( (de|het))?( [\\p{L}]{1,20})?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern GEWIJZIGD = Pattern.compile("als volgt.*(ge)?wijzig(en|d)$", Pattern.CASE_INSENSITIVE);
    public static final Pattern HOUDT_IN = Pattern.compile("houd(t|en)" +
            "( (v(oorts|erder)))?" +
            "( onder meer)?" +
            HET_VOLGENDE + "?" +
            " in( als volgt)?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VOORTS = Pattern.compile("^(en )?voorts$", Pattern.CASE_INSENSITIVE);
    public static final Pattern CONTRACT = Pattern.compile("contract", Pattern.CASE_INSENSITIVE);
    public static final Pattern OVEREENKOMST = Pattern.compile("overeenkomst", Pattern.CASE_INSENSITIVE);
    public static final Pattern IS_BEOORDEELD = Pattern.compile("^in.*artikel.*is (?:als|het) volg(t|ende)? (?:[gb]e)oordeelde?$", Pattern.CASE_INSENSITIVE);

}
