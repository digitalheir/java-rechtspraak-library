//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';
import sections from  './sections';
import F from  '../../Math/Math';

export default class Evaluation extends Component {
    //noinspection JSUnusedGlobalSymbols
    static getSections() {
        return sections;
    }

    render() {
        return <div>
            <p>
                There is much room for
                improvement of the parser that we have described in this section.
                We might improve both
                running time and parsing quality
                by using a less naive parsing algorithm, such as
                the a shift-reduce parser, which runs linearly for any CFG
                ({ref.cite(bib.zhu2013fast)}).
            </p>

            <p>
                One way to improve parse quality is to incorporate more domain-specific knowledge
                in the grammar. For example, sections
                with titles like 'OVERWEGINGEN' (considerations) and
                'CONCLUSIE' (conclusion) almost always appear as the first level of sectioning.
            </p>
            <p>
                Another possibility to improve the grammar is for the grammar
                to recognize different 'modes': section siblings often share a typography
                that is internally consistent, but not the same between documents. For example:
                all section are bold and capitalized, sub-sections are italic and sub-sub-sections
                are underlined.
            </p>
            <p>
                Owing to the brittleness of the current grammar,
                we might benefit from implementing a
                Conditional Probabilistic Context Free Grammar (Conditional CFG),
                as introduced in {ref.cite(bib.sutton2004conditional)}.
                Conditional CFGs are similar to
                Conditional CRFs in
                that we describe a conditional
                model instead of a generative one
                (so the probability distribution <F l="P(\mathbf y|\mathbf x)"/> instead
                of <F l="P(\mathbf x,\mathbf y)"/>), and this allows us to use a large
                feature set.
            </p>
        </div>;
    }
}
