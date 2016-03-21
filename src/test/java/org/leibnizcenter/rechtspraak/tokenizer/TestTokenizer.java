package org.leibnizcenter.rechtspraak.tokenizer;

import org.junit.Test;
import org.leibnizcenter.rechtspraak.markup.docs.nameparser.Names;

import java.util.List;

/**
 * Created by maarten on 15-3-16.
 */
public class TestTokenizer {

    @Test
    public void main() {
        List<Names.Span> tokens = Names.parseTokens("Hallo, ik ben mr. Jan met de Pet. Het is\n warempel echt waar.");
        System.out.println(tokens.size());
    }
}
