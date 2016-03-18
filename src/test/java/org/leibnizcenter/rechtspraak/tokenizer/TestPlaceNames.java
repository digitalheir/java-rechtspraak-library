package org.leibnizcenter.rechtspraak.tokenizer;

import org.junit.Assert;
import org.junit.Test;
import org.leibnizcenter.rechtspraak.features.PlaceNamesInNL;

/**
 * Created by maarten on 15-3-16.
 */
public class TestPlaceNames {
    @Test
    public void main() {
        Assert.assertTrue(PlaceNamesInNL.findAny("Ik woon in den Haag."));
        Assert.assertTrue(PlaceNamesInNL.findAny("Ik woon in s gravenhage."));
        Assert.assertTrue(PlaceNamesInNL.findAny("Ik woon in 's gravenhage."));
        Assert.assertTrue(PlaceNamesInNL.findAny("Ik woon in 's-Gravenhage."));
        Assert.assertTrue(PlaceNamesInNL.findAny("Ik woon in 'S-Gravenhage."));
        Assert.assertTrue(PlaceNamesInNL.findAny("Ik woon in utrecht."));
        Assert.assertTrue(PlaceNamesInNL.findAny("Ik woon in As."));
        Assert.assertFalse(PlaceNamesInNL.findAny("Ik woon in as."));
        Assert.assertTrue(PlaceNamesInNL.findAny("Ik woon in ROTTERDAM."));
    }
}
