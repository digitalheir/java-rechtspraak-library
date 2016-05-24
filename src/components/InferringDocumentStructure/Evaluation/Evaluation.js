//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';

export default class Evaluation extends Component {
    render() {
        return <div>
            <p>
                Evaluating performance on a parse tree is not as straightforward as it is for classification.
                Like in the previous chapter, we evaluate our grammar using an F-score, except the notions
                of precision and recall are harder to define.
                For evaluating the parser, we use a metric known as
                PARSEVAL (due to {ref.cite(bib.abney1991procedure)}) with labelled precision and labelled recall (as in
                {ref.cite(bib.collins1997three)}).
            </p>
            <p>
                In this metric, precision and recall are defined as follows:
            </p>
            <ul>
                <li>Precision is the fraction of correct constituents out of the total number of constituents in the
                    candidate parse
                </li>
                <li>Recall is the fraction of correct constituents out of the total number of constituents in the
                    gold standard
                </li>
            </ul>
            <p>
                Where 'correct constituent' means that each non-terminal node has the same label and the same yield
                (where
                'yield' means: the ordered list of leaf nodes).
            </p>

            <p>
                On a set of 10 random documents, we report an average F<sub>1</sub>-score of XXX (precision XXX,
                recall XXX).
            </p>
            <p>
                Delving deeper into problematic parses, we see that there are a number of recurring types
                of errors that our parsing grammar makes. Firstly, it often occurs that subsections are not preceded
                by a full numbering. For example, consider the following sequence:
            </p>
                <pre>
                    1.
                    2.
                    3.1
                    3.2
                </pre>
            <p>
                Our grammar assumes that section 3.1 is a subsection of section 2, since section 2
                is the first preceding supersection to 3.1. However, we would like to wrap the 3.X subsections in a
                section that represents section 3, even though there is no explicit numbering for section 3. This
                could be achieved with a better grammar.
            </p>
            <p>
                Another issue is that the grammar has difficulty in deciding whether non-numbered sections
                should be subsections or not. Indeed, this can be difficult to determine purely
                on typography.

                One possible method to resolve this is to incorporate some extra domain-specific knowledge
                on sections. For example, sections with titles like 'OVERWEGINGEN' (considerations) and
                'CONCLUSIE' (conclusion) are almost never subsections.
            </p>
        </div>;
    }
}
