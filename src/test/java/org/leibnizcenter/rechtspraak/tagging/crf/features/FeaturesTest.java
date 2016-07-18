package org.leibnizcenter.rechtspraak.tagging.crf.features;

import org.junit.Test;
import org.leibnizcenter.rechtspraak.tagging.crf.features.elementpatterns.ElementFeature;
import org.leibnizcenter.rechtspraak.tagging.crf.features.elementpatterns.NumberingFeature;
import org.leibnizcenter.rechtspraak.tagging.crf.features.textpatterns.GeneralTextFeature;
import org.leibnizcenter.rechtspraak.tagging.crf.features.textpatterns.KnownSurnamesNl;
import org.leibnizcenter.rechtspraak.tagging.crf.features.textpatterns.TitlePatterns;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by maarten on 1-4-16.
 */
public class FeaturesTest {
    @Test
    public void allFeaturesHaveDifferentNames() {
        Set<String> names = new HashSet<>();

        System.out.println(
                "checking "+(ElementFeature.values().length+
                NumberingFeature.values().length+
                GeneralTextFeature.values().length+
                //KnownSurnamesNl.values().length+
                TitlePatterns.General.values().length+
                TitlePatterns.TitlesNormalizedMatchesLowConf.values().length+
                TitlePatterns.TitlesNormalizedMatchesHighConf.values().length)+"features "
        );
        addNames(names, ElementFeature.values());
        addNames(names, NumberingFeature.values());
        addNames(names, GeneralTextFeature.values());
        addNames(names, KnownSurnamesNl.values());
        addNames(names, TitlePatterns.General.values());
        addNames(names, TitlePatterns.TitlesNormalizedMatchesLowConf.values());
        addNames(names, TitlePatterns.TitlesNormalizedMatchesHighConf.values());
    }

    private void addNames(Set<String> names, Enum[] values) {
        for (Enum p : values) {
            String name = p.name();
            assertFalse("Clashing name " + name, names.contains(name));
            names.add(name);
        }
    }
}