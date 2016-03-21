package org.leibnizcenter.rechtspraak.markup.docs.features.patterns;

import org.junit.Test;
import org.leibnizcenter.rechtspraak.features.KnownSurnamesNl;
import org.leibnizcenter.rechtspraak.markup.docs.nameparser.Names;

import java.util.List;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by maarten on 16-3-16.
 */
public class TestPersonNamePatterns {

    @Test
    public void testKnownSurnames() {
        assertTrue(KnownSurnamesNl.matches("'s-Gravesande Pannekoek"));
        assertTrue(KnownSurnamesNl.matches("Suasso de Lima de Prado-Stéhouwer-Aabachrim"));
        assertTrue(KnownSurnamesNl.matches("Aa-AB"));
        assertTrue(KnownSurnamesNl.matches("Živanović-'t Hardt"));
        assertTrue(KnownSurnamesNl.matches("'t Hardt-Živanović-'t Hardt"));
    }

    @Test
    public void testNamePatterns() {
        Pattern knownTitle = Pattern.compile(Names.KNOWN_TITLE);
        assertTrue(knownTitle.matcher("BA.").matches());
        assertTrue(knownTitle.matcher("meneer").matches());
        assertTrue(knownTitle.matcher("mevrouw").matches());
        assertTrue(knownTitle.matcher("dRS.").matches());
        assertTrue(knownTitle.matcher("mr.").matches());
        assertTrue(knownTitle.matcher("heer").matches());
        assertTrue(knownTitle.matcher("MSC.").matches());
        assertTrue(knownTitle.matcher("Professor").matches());
        assertTrue(knownTitle.matcher("prof.").matches());
        assertTrue(knownTitle.matcher("LLM").matches());
        assertTrue(knownTitle.matcher("bacc.").matches());


        Pattern preRoleSing = Pattern.compile(Names.ROLE_SINGULAR);
        assertTrue(preRoleSing.matcher("advocaat").matches());
        assertTrue(preRoleSing.matcher("Advocate").matches());
        assertTrue(preRoleSing.matcher("Advocate-Generaal").matches());
        assertTrue(preRoleSing.matcher("Advocaat generaal").matches());
        assertTrue(preRoleSing.matcher("ambtenaar").matches());
        assertTrue(preRoleSing.matcher("Ambtenaar van de staat").matches());
        assertTrue(preRoleSing.matcher("Ambtenaar Van De Gemeente").matches());
        assertTrue(preRoleSing.matcher("Waarnemend Griffier").matches());
        assertTrue(preRoleSing.matcher("griffier").matches());
        assertTrue(preRoleSing.matcher("raadsvrouw").matches());
        assertTrue(preRoleSing.matcher("raadman").matches());
        assertTrue(preRoleSing.matcher("Raadsheer").matches());
        assertTrue(preRoleSing.matcher("Rechter").matches());
        assertTrue(preRoleSing.matcher("rechter Commissaris").matches());

        Pattern preRoleMult = Pattern.compile(Names.ROLE_MULTIPLE);
        assertTrue(preRoleMult.matcher("advocaten").matches());
        assertTrue(preRoleMult.matcher("Advocates").matches());
        assertTrue(preRoleMult.matcher("Advocaten-Generaal").matches());
        assertTrue(preRoleMult.matcher("Advocaten generaal").matches());
        assertTrue(preRoleMult.matcher("ambtenaren").matches());
        assertTrue(preRoleMult.matcher("Ambtenaren van de staat").matches());
        assertTrue(preRoleMult.matcher("Ambtenaars Van De Gemeente").matches());
        assertTrue(preRoleMult.matcher("Waarnemend Griffiers").matches());
        assertTrue(preRoleMult.matcher("griffiers").matches());
        assertTrue(preRoleMult.matcher("raadsvrouwen").matches());
        assertTrue(preRoleMult.matcher("raadmannen").matches());
        assertTrue(preRoleMult.matcher("Raadsheren").matches());
        assertTrue(preRoleMult.matcher("Rechters").matches());
        assertTrue(preRoleMult.matcher("rechter Commissarissen").matches());

        Pattern strictInitial = Pattern.compile(Names.STRICT_INITIAL);
        Pattern looseInitials = Pattern.compile(Names.LOOSE_INITIALS);
        assertTrue(strictInitial.matcher("M.").matches());
        assertTrue(strictInitial.matcher("Thc.").matches());
        assertFalse(strictInitial.matcher("Thc").matches());
        assertTrue(looseInitials.matcher("Thc").matches());
        assertTrue(looseInitials.matcher("M F A").matches());
        assertTrue(looseInitials.matcher("M F. A.").matches());

        Pattern tolerantSingleName = Pattern.compile(Names.TOLERANTFULLNAME_WITH_OPTIONAL_ROLE);
        assertTrue(tolerantSingleName.matcher("V.D.W. van der Laan-Wijngaerde, advocaat").matches());
        assertTrue(tolerantSingleName.matcher("V D Willem Willem, raadheer").matches());
        assertTrue(tolerantSingleName.matcher("V D. de Beer de Laer Dupont, ambtenaar").matches());

        List<Names.Name> namez = Names.NamePatterns.WasGetekend.getNames("was getekend. A.F.T. de Waal");
        assertEquals(namez.size(), 1);
        assertEquals(namez.get(0).titles.string, null);
        assertEquals(namez.get(0).firstName.string, "A.F.T.");
        assertEquals(namez.get(0).lastName.string, "de Waal");

        namez = Names.NamePatterns.WasGetekend.getNames("w.g mr. Vincent W. Willems");
        assertEquals(namez.size(), 1);
        assertEquals(namez.get(0).titles.string, "mr.");
        assertEquals(namez.get(0).firstName.string, "Vincent W.");
        assertEquals(namez.get(0).lastName.string, "Willems");


        namez = Names.NamePatterns.TitledNameKnownSurname.getNames("w.g. mr. Vincent W. Willems");
        assertEquals(namez.get(0).lastName.string, "Willems");
        assertEquals(namez.get(0).titles.string, "mr.");

        namez = Names.NamePatterns.StrictInitialsTolerantName.getNames("prof. Vincent Willems prof. S.T. R.I. C.T. van der Berg.");
        assertEquals(namez.get(0).lastName.string, "van der Berg");

        namez = Names.NamePatterns.StrictInitialsKnownSurname.getNames("prof. Vincent Willems prof. S.T. R.I. C.T. van der Berg.");
        assertEquals(namez.get(0).lastName.string, "van der Berg");

        namez = Names.NamePatterns.AdvocaatPre.getNames("met advocaat : mr. A.B. van der Berg");
        assertEquals(namez.get(0).titles.string, "mr.");
        assertEquals(namez.get(0).firstName.string, "A.B.");
        assertEquals(namez.get(0).lastName.string, "van der Berg");

        namez = Names.NamePatterns.GewezenDoor.getNames("is gewezen door : mr. A.B. van der Berg");
        assertEquals(namez.get(0).titles.string, "mr.");
        assertEquals(namez.get(0).firstName.string, "A.B.");
        assertEquals(namez.get(0).lastName.string, "van der Berg");

        namez = Names.NamePatterns.GexxxDoorRolePost.getNames("is vertegenwoordigd door : mr. A.B. van der Berg, advocaat");
        assertEquals(namez.get(0).titles.string, "mr.");
        assertEquals(namez.get(0).firstName.string, "A.B.");
        assertEquals(namez.get(0).lastName.string, "van der Berg");
        assertEquals(namez.get(0).role.string, "advocaat");

        namez = Names.NamePatterns.DeXConcludeert.getNames("de rechter mr. A.B. van der Berg heeft concludeerde");
        assertEquals(namez.get(0).titles.string, "mr.");
        assertEquals(namez.get(0).firstName.string, "A.B.");
        assertEquals(namez.get(0).lastName.string, "van der Berg");
        assertEquals(namez.get(0).role.string, "rechter");

        namez = Names.NamePatterns.NameAlsX.getNames("de ambtenaar mr. A.B. van der Berg, advocaat, als rechter heeft concludeerde");
        assertEquals(namez.get(0).titles.string, "mr.");
        assertEquals(namez.get(0).firstName.string, "A.B.");
        assertEquals(namez.get(0).lastName.string, "van der Berg");
        assertEquals(namez.get(0).role.string, "ambtenaar");
        assertEquals(namez.get(0).role2.string, "advocaat");
        assertEquals(namez.get(0).role3.string, "rechter");

//        assertEquals(pttrn.get(0).role.string, "advocaat");

        // Namegroups todo
        namez = Names.NamePatterns.Meesters.getNames("uhh w.g. mrs. Vincent W. Willems en M. Nellen allen als advocaat :-)");
        assertTrue(namez.size() > 0);
        //assertEquals(namez.get(0).lastName.string, "Willems");
        namez = Names.NamePatterns.GexxxDoorMultiple.getNames("uhh vertegenwoordigd door mrs. Vincent W. Willems en M. Nellen allen als advocaat :-)");
        assertTrue(namez.size() > 0);
        namez = Names.NamePatterns.RolePreMultiple.getNames("uhh vertegenwoordigd door rechters mrs. Vincent W. Willems en M. Nellen, advocaten :-)");
        assertTrue(namez.size() > 0);


//        ("advocaat V D Willem Willem, advocaat als raadsheer"));
//        assertTrue(UnnormalizedTextContainsName.HighConfidenceRolePost.find("V D Willem Willem als raadsheer"));

//        Pattern tolerantMultipleNames = Pattern.compile(NamePatterns.TOLERANTFULLNAME_2_TO_4);
//        assertTrue(tolerantSingleName.matcher("V. Willems en C. Willems").find());
//        assertTrue(tolerantSingleName.matcher("V. Willems en C. Willems").find());
//                , ambtenaren"
//        , allen ambtenaren van de provincie"
    }
}
