package org.leibnizcenter.rechtspraak.features.title;

import cc.mallet.types.Token;
import org.leibnizcenter.rechtspraak.markup.RechtspraakElement;
import org.leibnizcenter.rechtspraak.markup.features.Patterns;
import org.leibnizcenter.rechtspraak.util.TextPattern;

import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by maarten on 17-3-16.
 */
public class TitlePatterns {
    //
    // TITLES
    //
    public enum TitlesUnnormalizedContains implements Patterns.UnnormalizedTextContains {
        ENDS_W_COLON(new TextPattern(Pattern.compile(":\\s*$")));
        public static Set<TitlesNormalizedMatches> set = EnumSet.allOf(TitlesNormalizedMatches.class);
        private final TextPattern pattern;

        TitlesUnnormalizedContains(TextPattern pattern) {
            this.pattern = pattern;
        }

        public static void setFeatureValues(Token t, RechtspraakElement token) {
            set.forEach((p) -> {
                if (Patterns.matches(p, token)) t.setFeatureValue(p.toString(), 1.0);
            });
        }

        public boolean matches(String s) {
            return this.pattern.find(s);
        }

    }

    public enum TitlesNormalizedMatches implements Patterns.NormalizedTextMatches {

        //"bewijs"));
        //"bewijsmiddelen"));
        //"bewezenverklaring en bewijsvoering"));
        //"bewezenverklaring"));
        PROVE(new TextPattern(Pattern.compile("((de )?kwalificatie van )?" +
                "((het|de) )?" +
                "bew(ijs|ezen)(verklaa?r(de|ing)|middel|voering|aanbod)?(en)?( en bewijsvoering)?", Pattern.CASE_INSENSITIVE)

        )),
        QUALIFICATION(
                new TextPattern(Pattern.compile("(de )?kwalificaties?",
                        Pattern.CASE_INSENSITIVE))),

        FACTS(new TextPattern(Pattern.compile("(t(en)? ?\\.? ?a(anzien)? ?\\.? ?v(an)? ?\\.? ?)?" +
                "(de )?" +
                "(tussen (de )?partijen )?" +
                "(vaststaande )?" +
                "(feiten|uitgangs?punten)" +
                "( en)?" +
                "( (de|het))?" +
                "( omstandigheden|geding)?" +
                "( in( (de|het))? feitelijke instanties)?" +
                "( in (re)?conventie en (in )?(re)?conventie)?" +
                "\\s*", Pattern.CASE_INSENSITIVE)

        )),

        INVESTIGATION(new TextPattern(
                Pattern.compile("(het )?onderzoek( ((van de|der) zaak|([dt]er|op de) terechtzitting|([^\\p{L}]+\\b[\\p{L}]+\\b){0,3}))\\s*",
                        Pattern.CASE_INSENSITIVE))),

        DEMAND(new TextPattern(Pattern.compile(
                "((de|het) )?" +
                        "(bespreking (van )?)?" +
                        "((de|het) )?" +
                        "(beklaa?g(de)?|grie(f|ven)|eis|klacht)" +
                        "( van)?( de)?( (benadeelden?|officier(s|en)? van justitie))?"))),

        APPENDIX(new TextPattern(Pattern.compile("(de )?" +
                "bijlage(n|s)?"))),

        CASE(new TextPattern(Pattern.compile("(([0-9]|i{0,3}v?i{0,3}) ?)?" +
                        "((de )?(((mondelinge )?behandeling|ontvankelijkheid|omvang|stukken) van|uitspraak in) )?" +
                        "((de|het) feiten en )?" +
                        "((de|het) )?" +
                        "((bestreden|eerste|tweede) )?" +
                        "(aanvraag|vonnis|verweer|hoger beroep|vordering|uitspraak|ge(ding|schil)|(wrakings?)?verzoek)" +
                        "( (in)( (feitelijke|eerste))?( (instanties?)))?" +
                        "( (in)( het)?( (hoger|eer(ste|dere?)|tweede))? (incident|princip(i?aal|e)))?" +
                        "( (in|waarvan)( (hoger|eer(ste|dere?)|tweede|incidentee?le?))? ((re)?con?ventie|cassatie|beroep|aanleg))?" +
                        "(( waarvan)?( tot)? herziening(( is)? gevraagd( is)?)?)?" +
                        "( (alsmede|en)( het)?( verweer)?([^\\p{L}]+\\b[\\p{L}]+\\b){0,4})?",
                Pattern.CASE_INSENSITIVE))),

        CASSATIE(new TextPattern(Pattern.compile("(bespreking van (het|de) )?" +
                        "(uitgangspunt(en)? )?(in )?" +
                        "cassatie(middel)?",
                Pattern.CASE_INSENSITIVE)
        )),

        PUNISHMENT(new TextPattern(Pattern.compile("(kwalificatie en )?((de|het) )?" +
                        "((op te leggen|oplegging) )?(van )?(de )?" +
                        "(straf(baarheid|oplegging)?|(straf)?maatregel)" +
                        "( (en|of|enof) (maatregel|straf))?" +
                        "((( en)?( van)?( (de|het))? (bewezen ?verklaarde|daders?|verdachten?|feit(en)?))+)?" +
                        "(vermeld )?( op)?( bijlage)?" +
                        "([^\\p{L}0-9]+\\b[\\p{L}0-9]+\\b){0,4}\\s*",
                Pattern.CASE_INSENSITIVE)

        )),
        QUESTIONS(new TextPattern(Pattern.compile("(de )?(voor)?vragen",
                Pattern.CASE_INSENSITIVE
        )
        )),
        LAWS_APPLICABLE(new TextPattern(Pattern.compile("((de|het) )?" +
                        "((toepass(elijke?(heid)?|ing)|toegepaste|relevanti?e) )?" +
                        "( van)?((de|het) )?" +
                        "( juridische?)?" +
                        "(juridische?|literatuur|wet)s?((telijke )?(voorschrift|kader|bepaling|artikel)(en)?)?" +
                        "(en regelgeving)?",
                Pattern.CASE_INSENSITIVE
        )
        )),
        INDICTMENT(new TextPattern(
                Pattern.compile("(de )?(inhoud van de )?tenlasten?legging",
                        Pattern.CASE_INSENSITIVE
                )
        )),


        // BESLAG
        BESLAG(new TextPattern(Pattern.compile("((de|het) )?beslag", Pattern.CASE_INSENSITIVE)

        )),

        // Grondslag (van het geschil)
        GRONDSLAG(new TextPattern(Pattern.compile("((de|het) )?grond(slag)?(en)?( van het (geschil|(hoger )?beroep))?",
                Pattern.CASE_INSENSITIVE)
        )),

        STANDPUNT(new TextPattern(Pattern.compile("(het geschil )?((de|het) )?(standpunt|griev)(en)?( van(\\s+\\p{L}+){0,3})?",
                Pattern.CASE_INSENSITIVE)
        )),
        VORDERING(new TextPattern(Pattern.compile(
                "(ten aanzien van )?" +
                        "((de|het) )?vordering(en)?" +
                        "( van( de)?)?" +
                        "(( tot)? tenuitvoerlegging)?" +
                        "( (officier van justitie|benadeelde partij(en)?))?" +
                        "( +(en )?(de )?( immateriële)?schadevergoedings?maatregel(en)?)?" +
                        "( in (re)?conventie)?" +
                        "( benadeelde.?)?",
                Pattern.CASE_INSENSITIVE)
        )),
        DEFENSE(new TextPattern(Pattern.compile(
                "((de )?vordering (en )?)?" +
                        "((de|het) )?" +
                        "(onschulds?)?verwe?er(en)?" +
                        "( en( (het|de))?( tegen)?verzoek(en)?)?" +
                        "( van( de)? [\\p{L}]+\\b)?",
                Pattern.CASE_INSENSITIVE)
        )),
        DAMAGE(new TextPattern(Pattern.compile(
                "(ten aanzien van )?" +
                        "((de|het) )" +
                        "?(schade(vergoeding)?|benadeelde partij)" +
                        "( (van|voor) (de )?benadeelden?)?" +
                        "(en (de )?schadevergoedings?maatregel)?",
                Pattern.CASE_INSENSITIVE)
        )),
        EAB(new TextPattern(Pattern.compile("((de )?((inhoud|grondslag) en )?(inhoud|grondslag) van het )?eab",
                Pattern.CASE_INSENSITIVE)
        )),
        PARTY(new TextPattern(Pattern.compile(
                "(ten aanzien van )?" +
                        "((het )?geschil (alsmede )?)?" +
                        "(" +
                        "((de|het) )?(standpunt|stelling)(en)?( en conclusies?)? van )?" +
                        "((de|het) )?(benadeelde )?(verdediging|partij|officier van justitie)(en)?" +
                        "(en ((de|het) )?(benadeelde )?(verdediging|partij|officier van justitie)(en)?)?" +
                        "( in( hoger)? beroep)?" +
                        "( en (de )?schadevergoedings?maatregel(en)?)?",
                Pattern.CASE_INSENSITIVE)
        )),
        INTRO(new TextPattern(Pattern.compile("inleiding|overzicht",
                Pattern.CASE_INSENSITIVE)
        )),

        //
        // beslissing
        //
        JUDGMENT(new TextPattern(Pattern.compile(
                /**
                 * "beoordeling van het geschil en de motivering van de beslissing" //46
                 * "beoordeling van het verzoek en de motivering van de beslissing" //41
                 */
                "(([0-9]|i{0,3}v?i{0,3}) ?)?" +
                        "((de|het) )?" +
                        "((beoordeling van het (geschil|verzoek) en )?" +
                        /**
                         * "feiten het geschil en de beslissing in eerste aanleg" //49
                         * "feiten het geschil en de motivering van de beslissing" //6
                         */
                        /**
                         * "geschil en de beslissing in eerste aanleg" //125
                         * "geschil en de beslissing van de kantonrechter" //10
                         * "geschil en de beslissing van de rechtbank" //24
                         * "geschil en de beslissing van de voorzieningenrechter" //11
                         * "geschil in eerste aanleg en de beslissing van de voorzieningenrechter" //8
                         */
                        "((feiten )?(het )?(geschil )?(in (de )?)?(eerste aanleg )?(en )?)?" +
                        /**
                         * "motivering van de beslissing" //820
                         * "motivering van de beslissing in het incident" //31
                         * "motivering van de beslissing in hoger beroep" //679
                         * "motivering van de beslissing na voorwaardelijke veroordeling" //37
                         */
                        /**
                         * "gronden van de beslissing" //694
                         * "gronden voor de beslissing" //8
                         */
                        "(de )?(((motivering|gronden) (van|voor) )?)?" + // <- 'motivering' could also be for consideration
                        /**
                         * "bestreden beslissing op bezwaar" //7
                         * "bewijsbeslissing" //7
                         * "bewijsbeslissingen" //39
                         */
                        /**
                         * "beslissing" //49530
                         * "beslissing afwijzing vordering verlenging terbeschikkingstelling" //6
                         * "beslissing de rechtbank" //8
                         * "beslissing het gerechtshof" //28
                         * "beslissing in conventie en in reconventie" //9
                         * "beslissing in eerste aanleg" //17
                         * "beslissing in het incident" //15
                         * "beslissing in kort geding" //56
                         * "beslissing in reconventie" //7
                         * "beslissing inzake het bewijs" //93
                         * "beslissing met betrekking tot de voorlopige hechtenis" //8
                         * "beslissing na voorwaardelijke veroordeling" //17
                         * "beslissing op de vordering van de benadeelde partij" //44
                         * "beslissing op de vordering van de benadeelde partij feit" //23
                         * "beslissing op de vordering van de benadeelde partij slachtoffer" //9
                         * "beslissing op het hoger beroep" //221
                         * "beslissing op het principale en het incidentele hoger beroep" //25
                         * "beslissing van de kantonrechter" //15
                         * "beslissing van de rechtbank" //198
                         * "beslissing van de voorzitter" //11
                         * "beslissing verlenging terbeschikkingstelling" //178
                         * "beslissing voorwaardelijk einde verpleging van overheidswege" //18
                         * "beslissingen" //49
                         * "beslissingen in eerste aanleg" //6
                         * "beslissingen op de vorderingen van de benadeelde partijen" //6
                         */
                        "(" +
                        "(((de )?(bestreden )?(bewijs)?beslissing(en)?)([^\\p{L}]+\\b[\\p{L}]+\\b){0,8})" +
                        /**
                         * "slotsom" //2423
                         * "slotsom en conclusie" //8
                         * "slotsom en kosten" //25
                         * "slotsom en proceskosten" //32
                         * "uitspraak"
                         */
                        "|(((de|het) )?((uit|vrij)spraak|(hoofd)?zaak|afronding|slotsom|conclusies?)( en (uitspraak|slotsom|conclusies?|(proces)?kosten))?)" +
                        /**
                         * "verdere beoordeling van het geschil en de gronden van de beslissing" //7
                         * "verdere motivering van de beslissing" //7
                         * "verdere motivering van de beslissing in hoger beroep" //53
                         * "voortgezette motivering van de beslissing in hoger beroep" //12
                         */
                        "|((verdere|voortgezette) (motivering|beoordeling|beoordeling) van (de|het) (geschil|beslissing)( in (hoger )?beroep)?( en de gronden van de beslissing)?)" +
                        /**
                         * "vordering en de beslissing daarop in eerste aanleg" //9
                         * "vordering en de beslissing in eerste aanleg" //28
                         * "vordering in eerste aanleg en de beslissing daarop" //10
                         * "vorderingen en de beslissing in eerste aanleg" //10
                         * "vorderingen en de beslissingen in eerste aanleg" //6
                         * "vorderingen in eerste aanleg en de beslissing daarop" //6
                         **/
                        "|(vordering(en)?( in eerste aanleg)? en de beslissing(en)?( daarop)?( in eerste aanleg)?)" +
                        // oordeel van de rechtbank
                        "|((conclusie|reactie|oordeel|(uit|vrij)spraa?k(en)?)( van (de|het) (accountants?(kamer)?|officier van justitie|advocaatgeneraal|recht(ers?|bank)|[a-z]{0,10}hof|[a-z]{0,10}rechter))?)" +
                        "|((tenuitvoerlegging )?(van )?( de)?(voorwaardelijke )?veroordeling)" +
                        "|(d[eé]cision)" +
                        "))\\s*"
        )

        )),

        RECONVENTION(new TextPattern(Pattern.compile("(in )?(re)?conventie( en (in )?(re)?conventie)?"))),

        //
        // overwegingen
        //
        CONSIDERATIONS(new TextPattern(Pattern.compile(
                "(i{0,3}v?i{0,3} ?)?" +
                        "((de|het) )?(" +
                        "(waardering van het bewijs)" +
                        /**
                         * "achtergronden van de zaak" //7
                         */
                        "|(achtergronden van de zaak)" +
                        /**
                         * "bewijs en bewijsoverwegingen" //7
                         * "bewijsmiddelen en de beoordeling daarvan" //204
                         * "bewijsmotivering" //153
                         * "bewijsoverwegingen" //540
                         * "bewijsoverwegingen met betrekking tot zaaksdossier adres" //7
                         * "bewijsoverwegingen ten aanzien van feit" //6
                         *              * "algemene bewijsoverwegingen" //6
                         * "algemene overwegingen in alle zaken" //6
                         *
                         * * "bijzondere overwegingen omtrent het bewijs" //14
                         * * "inleidende overwegingen" //6
                         *      * "nadere bewijsmotivering" //22
                         * "nadere bewijsoverwegingen" //23
                         *
                         *      * "overwegingen" //14304
                         * "overwegingen en oordeel van het hof" //6
                         * "overwegingen met betrekking tot het bewijs" //37
                         * "overwegingen omtrent het geschil" //6
                         * "overwegingen ten aanzien van het bewijs" //64
                         * "overwegingen ten aanzien van straf enof maatregel" //190
                         * "overwegingen van de kantonrechter" //35
                         * "overwegingen van de rechtbank" //107
                         * "overwegingen van het hof" //9
                         * "rechtsoverwegingen" //40
                         * "vaststellingen en overwegingen" //19
                         */
                        "|(((bewijs|vaststellingen) en )?((nadere|algemene|bijzondere|inleidende) )?(bewijs|rechts?)?(overwegingen|middelen|motivering)([^\\p{L}]+\\b[\\p{L}]+\\b){0,6})" +
                        /**
                         * "beoordeling" //13418
                         * "beoordeling door ..." //43
                         * "beoordeling en ..."
                         * "beoordeling in ..."
                         * "beoordeling rechtbank" //7
                         * "beoordeling van ..."
                         * "beoordeling van de door belanghebbende voorgestelde middelen" //31
                         * "beoordeling van de door de staatssecretaris voorgestelde middelen" //6
                         * "beoordeling van de in het incidentele beroep voorgestelde middelen" //10
                         * "beoordeling van de in het principale beroep voorgestelde middelen" //18
                         * "beoordeling van de middelen in het principale en in het incidentele beroep" //9
                         * "beoordeling van de ontvankelijkheid van het verzoek tot herziening" //39
                         * "beoordeling van het zevende middel" //12
                         * "beoordeling vaststaande feiten" //6
                         * "beschouwing en beoordeling" //15
                         * "beschouwing en beoordeling van de middelen" //12
                         * "beschouwing en beoordeling van het middel" //6
                         *
                         *              * "verdere beoordeling" //1563
                         * "verdere beoordeling in conventie en in reconventie" //18
                         * "verdere beoordeling in de hoofdzaak" //7
                         * "verdere beoordeling in hoger beroep" //35
                         * "verdere beoordeling in reconventie" //7
                         * "verdere beoordeling van de zaak" //15
                         * "verdere beoordeling van het geschil" //56
                         * "verdere beoordeling van het geschil in hoger beroep" //10
                         * "verdere beoordeling van het hoger beroep" //107
                         * "verdere beoordeling van het middel" //8
                         * "verdere beoordeling vaststaande feiten" //9
                         *
                         *  * "geschil en beoordeling" //21
                         * "geschil en de beoordeling" //61
                         * "geschil en de beoordeling daarvan" //100
                         * "geschil en de beoordeling in eerste aanleg" //37
                         *
                         *      * "grieven en beoordeling in hoger beroep" //7
                         * "grieven en de beoordeling in hoger beroep" //23
                         *      * "inhoudelijke beoordeling" //8
                         *
                         *      * "ambtshalve beoordeling van de bestreden uitspraak" //178
                         * "nadere beoordeling" //30
                         * "nadere beoordeling van het hoger beroep" //21
                         * "nadere beoordeling van het middel" //7
                         * * "standpunten van partijen en de beoordeling daarvan" //15
                         *
                         * * "verzoek en de beoordeling" //53
                         * "verzoek en de beoordeling daarvan" //14
                         * * "uitgangspunten voor de beoordeling" //10
                         * "vordering en beoordeling daarvan in eerste aanleg" //9
                         * "vordering en beoordeling in eerste aanleg" //55
                         * "vordering en de beoordeling in eerste aanleg" //16
                         * "vorderingen en beoordeling in eerste aanleg" //27
                         * */
                        "|(((standpunt(en)? van( de)? partij(en)?)|beschouwing|verzoek|geschil|grieven|vordering(en)?) en )?(uitgangspunten (van|voor) )?(de )?((ambtshalve|verdere|inhoudelijke|nadere) )?(beoordeling(([^\\p{L}]+\\b[\\p{L}]+\\b){0,11})?)" +
                        /**
                         * "feiten en achtergronden" //24
                         * * "gronden" //695
                         * "gronden van het hoger beroep" //861
                         * "gronden van het verzet" //14
                         * "gronden van het verzoek" //19
                         * "gronden van het wrakingsverzoek" //13
                         * "gronden van het wrakingsverzoek en het standpunt van verzoeker" //7
                         * "gronden van wraking" //7
                         * "op te leggen straf of maatregel en de gronden daarvoor" //14
                         *  * "verzoek en de gronden daarvan" //78
                         */
                        "|(((op te leggen (straf|maatregel) (of (maatregel|straf) )?en (de )?)|(feiten en achter)|(verzoek en de ))?gronden([^\\p{L}]+\\b[\\p{L}]+\\b){0,8})" +
                        /**
                         * "motivering" //266
                         * "motivering maatregel" //7
                         * "motivering straf" //42
                         * "motivering van de bewezenverklaring" //9
                         * "motivering van de hoofdelijkheid" //41
                         * "motivering van de maatregel" //20
                         * "motivering van de maatregel onttrekking aan het verkeer" //9
                         * "motivering van de sanctie" //159
                         * "motivering van de sancties" //409
                         * "motivering van de straf" //156
                         * "motivering van de straf en maatregel" //26
                         * "motivering van de straf en maatregelen" //17
                         * "motivering van de straffen" //48
                         * "motivering van de straffen en maatregel" //7
                         * "motivering van de straffen en maatregelen" //768
                         * "motivering van de verbeurdverklaring" //6
                         * "motivering van straf of maatregel" //89
                         * "motivering vrijspraak" //35
                         * "strafmotivering" //577
                         * "tenlastelegging en motivering van de gegeven vrijspraak" //6
                         */
                        "|(tenlastelegging en )?((straf)?motivering(van )?([^\\p{L}]+\\b[\\p{L}]+\\b){0,8})" +
                        ")"
        )

        )),


        LEGAL_MEANS(new TextPattern(Pattern.compile("((de|het) )?(bespreking van )?" +
                "((de|het) )?" +
                "(cassatie|rechts?)?middel(en)?"))),
        COSTS(new TextPattern(Pattern.compile("(i{0,3}v?i{0,3} ?)?"
                + "([0-9]? ?)" +
                "((de|het) )?(proces)?kosten( en griffierecht)?(veroordeling)?"))),
        PROCEEDINGS(new TextPattern(Pattern.compile(
                "(i{0,3}v?i{0,3} ?)?" +
                        "((de|het) )?" +
                        /**
                         * "feiten en procesverloop" 250,
                         * "feiten en het procesverloop" 121,
                         * "inleiding feiten en procesverloop" 10,
                         * "procesgang" 489,
                         * "procesverloop" 9511,
                         * "procesverloop en de processtukken" 93,
                         * "procesverloop in hoger beroep" 529,
                         * "procesverloop in eerste aanleg en vaststaande feiten" 226,
                         * "verdere procesverloop" 98,
                         * "verder procesverloop in hoger beroep" 19,
                         * "verdere procesverloop in hoger beroep" 16,
                         * "voorgeschiedenis en het procesverloop" 111
                         */
                        "((((voor)?geschiedenis)|((inleiding )?feiten)) en )?" +
                        "(" + "(de|het) )?" +
                        "("
                        + "(((verdere?|voortgezet(te)?) )?" +
                        "proce(s|dure)(gang|verloop)?" +
                        "(( [ei]n)( de)?([^\\p{L}]+\\b[\\p{L}]+\\b){0,2}\\s*)?" +
                        "(en vaststaande feiten)?)" +

                        "|((voor)?geschiedenis)" +
                        "|proce(s|dure)" +
                        /**
                         * "ontstaan en loop van het geding" 2384,
                         * "ontstaan en loop van de procedure" 45,
                         * "ontstaan en loop van de gedingen" 31
                         * "ontstaan en loop van het geding voor verwijzing" 6
                         **/
                        "|(" +
                        "(ontstaan en )?((verdere?|voortgezet(te)?) )?(ver)?loop(( van( (de|het) )?)?(procedures?|geding(en)?))?" +
                        ")" +
                        "|(gang van zaken)" +
                        ")" +
                        "( in( hoger)? beroep)?",
                Pattern.CASE_INSENSITIVE
        )
        ));

        public static Set<TitlesNormalizedMatches> set = EnumSet.allOf(TitlesNormalizedMatches.class);
        private final TextPattern pattern;

        TitlesNormalizedMatches(TextPattern pattern) {
            this.pattern = pattern;
        }

        public static void setFeatureValues(Token t, RechtspraakElement token) {
            set.forEach((p) -> {
                if (Patterns.matches(p, token)) t.setFeatureValue(p.toString(), 1.0);
            });
        }

        public boolean matches(String s) {
            return this.pattern.matches(s);
        }
    }
}
