package org.leibnizcenter.rechtspraak.markup.features.patterns;

import com.google.common.collect.Maps;
import org.crf.crf.CrfFeature;
import org.leibnizcenter.rechtspraak.markup.Label;
import org.leibnizcenter.rechtspraak.markup.RechtspraakElement;
import org.leibnizcenter.rechtspraak.util.Doubles;
import org.leibnizcenter.rechtspraak.util.Labels;
import org.leibnizcenter.rechtspraak.util.TextBlockInfo;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

/**
 * Created by Maarten on 3/7/2016.
 */
public enum Patterns {
    //"bewijs"));
    //"bewijsmiddelen"));
    //"bewezenverklaring en bewijsvoering"));
    //"bewezenverklaring"));
    PROVE(new TextBlockInfo.TextPattern(Pattern.compile("bew(ijs|ezen)(verklaring|middel|voering|aanbod)?(en)?( en bewijsvoering)?", Pattern.CASE_INSENSITIVE))),

    FACTS(new TextBlockInfo.TextPattern(Pattern.compile("(vaststaande )?feiten([^\\p{L}]+\\b[\\p{L}]+\\b){0,8}\\s*", Pattern.CASE_INSENSITIVE))),
    GEDING(new TextBlockInfo.TextPattern(Pattern.compile("ge(ding|schil)([^\\p{L}]+\\b[\\p{L}]+\\b){0,5}\\s*", Pattern.CASE_INSENSITIVE))),
    STRAFBAARHEID(new TextBlockInfo.TextPattern(Pattern.compile("straf(baarheid|oplegging)((( en)?( van)?( (de|het))? (bewezen ?verklaarde|daders?|verdachten?|feit(en)?))+)?", Pattern.CASE_INSENSITIVE))),

    // BESLAG
    BESLAG(new TextBlockInfo.TextPattern(Pattern.compile("beslag", Pattern.CASE_INSENSITIVE))),
    //
    // beslissing
    //
    JUDGMENT(new TextBlockInfo.TextPattern(Pattern.compile(
            /**
             * "beoordeling van het geschil en de motivering van de beslissing" //46
             * "beoordeling van het verzoek en de motivering van de beslissing" //41
             */
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
                    "((feiten )?(het )?(geschil (in eerste aanleg )?)?en )?" +
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
                    "(de )?((motivering|gronden)( (van|voor) )?)?" + // <- 'motivering' could also be for consideration
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
                    "((de )?(bestreden )?(bewijs)?beslissing(en)?)([^\\p{L}]+\\b[\\p{L}]+\\b){0,8})" +
                    /**
                     * "slotsom" //2423
                     * "slotsom en conclusie" //8
                     * "slotsom en kosten" //25
                     * "slotsom en proceskosten" //32
                     */
                    "|((slotsom|conclusies?)( en (slotsom|conclusies?|(proces)?kosten))?)" +
                    /**
                     * "verdere beoordeling van het geschil en de gronden van de beslissing" //7
                     * "verdere motivering van de beslissing" //7
                     * "verdere motivering van de beslissing in hoger beroep" //53
                     * "voortgezette motivering van de beslissing in hoger beroep" //12
                     */
                    "|((verdere|voortgezette) (motivering|beoordeling|beoordeling) van (de|het) (geschil|beslissing)( in hoger beroep)?( en de gronden van de beslissing)?)" +
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
                    "|(oordeel( van (de|het) (rechtbank|[a-z]{0,10}hof|[a-z]{0,10}rechter))?)" +
                    "\\s*"
    ))),
    //
    // overwegingen
    //
    CONSIDERATIONS(new TextBlockInfo.TextPattern(Pattern.compile(
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
                    "|(tenlastelegging en )?((straf)?motivering(van )?([^\\p{L}]+\\b[\\p{L}]+\\b){0,8})"
    ))),
    PROCEEDINGS(new TextBlockInfo.TextPattern(Pattern.compile(
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
            "(((((voor)?geschiedenis)|((inleiding )?feiten)) en( het)? )?(verdere? )?proces(gang|verloop)?( [ei]n)?( de)?([^\\p{L}]+\\b[\\p{L}]+\\b){0,8}\\s*)|" +
                    "((voor)?geschiedenis)|procedure|" +
                    /**
                     * "ontstaan en loop van het geding" 2384,
                     * "ontstaan en loop van de procedure" 45,
                     * "ontstaan en loop van de gedingen" 31
                     * "ontstaan en loop van het geding voor verwijzing" 6
                     **/
                    "((ontstaan en )?(ver)?loop (van ((de|het) )?)?(procedures?|geding(en)?)([^\\p{L}]+\\b[\\p{L}]+\\b){0,3}\\s*)",
            Pattern.CASE_INSENSITIVE
    )));


    private final TextBlockInfo.TextPattern pattern;

    Patterns(TextBlockInfo.TextPattern pattern) {
        this.pattern = pattern;
    }

    public boolean matches(String s) {
        return this.pattern.matches(s);
    }

    public static final Map<Patterns, Map<Label, Feature>> features;
    public static final Map<Patterns, Map<Label, Filter>> filters;
    static {
        Map<Patterns, Map<Label, Feature>> featuresMap = Maps.newEnumMap(Patterns.class);
        Map<Patterns, Map<Label, Filter>> filtersMap = Maps.newEnumMap(Patterns.class);
        for (Patterns wc : Patterns.values()) {
            Map<Label, Filter> filterMap = Maps.newEnumMap(Label.class);
            Labels.set.forEach(label -> filterMap.put(label, new Filter(label, wc)));
            filtersMap.put(wc, filterMap);

            Map<Label, Feature> featureMap = Maps.newEnumMap(Label.class);
            Labels.set.forEach(label -> featureMap.put(label, new Feature(label, wc)));
            featuresMap.put(wc, featureMap);
        }
        features = Maps.immutableEnumMap(featuresMap);
        filters = Maps.immutableEnumMap(filtersMap);
    }

    public static class Feature extends CrfFeature<RechtspraakElement, Label> {
        private final Patterns enumType;
        private final Label label;

        public Feature(Label l, Patterns enumType) {
            this.label = l;
            this.enumType = enumType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Feature feature = (Feature) o;

            return enumType.equals(feature.enumType) && label == feature.label;
        }

        @Override
        public int hashCode() {
            int result = enumType.hashCode();
            result = 31 * result + label.hashCode();
            return result;
        }

        @Override
        public double value(RechtspraakElement[] sequence, int indexInSequence, Label currentTag, Label previousTag) {
            boolean isTrue = Patterns.matches(enumType, sequence[indexInSequence]);
            return Doubles.asDouble(isTrue);
        }

    }

    public static boolean matches(Patterns pattern, RechtspraakElement element) {
        return pattern.matches(element.normalizedText);
    }

    public static class Filter extends org.crf.crf.filters.Filter<RechtspraakElement, Label> {
        private final Patterns enumType;
        private final Label label;

        public Filter(Label l, Patterns enumType) {
            this.label = l;
            this.enumType = enumType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Filter feature = (Filter) o;
            return enumType.equals(feature.enumType) && label == feature.label;
        }

        @Override
        public int hashCode() {
            int result = enumType.hashCode();
            result = 31 * result + label.hashCode();
            return result;
        }
    }
}
