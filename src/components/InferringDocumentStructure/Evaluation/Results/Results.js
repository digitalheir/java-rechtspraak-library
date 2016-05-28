//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import ref from '../../../Bibliography/References/references'
import bib from  '../../../Bibliography/bib';

export default class ParsingEval extends Component {
    render() {
        return <div>
            <p>
                Over a set of 10 random documents, we report an average F<sub>1</sub>-score of 0.92 (precision 0.93;
                recall 092).
            </p>
            <p>
                Delving deeper into problematic parses, we see that there are a number of recurring types
                of errors that our parsing grammar makes. Firstly, it often occurs that subsections are not preceded
                by a full numbering. For example, consider the following section sequence:
            </p>
<pre>
1.<br/>
2.<br/>
3.1<br/>
3.2<br/>
</pre>
            <p>
                Our grammar assumes that section 3.1 is a subsection of section 2, since section 2
                is the first preceding supersection to 3.1. However, this not
                the desired result. The desired result would be to wrap the 3.X subsections in a
                section that represents section 3, even though there is no explicit
                numbering for section 3. This
                could be achieved with an extension to the section grammar.
            </p>
            <p>
                Another issue is that the grammar has difficulty in deciding whether non-numbered sections
                should be subsections or not. Indeed, this can be difficult to determine purely
                on typography.

                One possible method to resolve this is to incorporate some extra domain-specific knowledge
                on sections. For example, sections with titles like 'OVERWEGINGEN' (considerations) and
                'CONCLUSIE' (conclusion) are almost never subsections.
            </p>

            <p>
                A note also on runtime complexity: although the algorithm is polynomial,
                it is slow, even after optimizing the algorithm for resource re-use
                and parallellizing calculation of the table cells.
                Parsing the longest document in the corpus, which consists of
                more than a 1000 elements, took about 10 minutes
                on an Intel i7 quad-core CPU from 2010 running at 2.67GHz.
                These are not fundamental problems, however, and we now discuss some
                likely fruitful avenues for future work.
            </p>
        </div>;
    }
    }
