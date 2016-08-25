//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Source from '../../Source/Source'
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';

import abbrs  from '../../abbreviations'
export default class Importing extends Component {
    render() {
        return <div>
            <p>
                Regarding tokenization, we need to do some forward thinking
                in order to determine
                how to split {abbrs.xml} texts from Rechtspraak.nl.
                We assume a text to be decomposable into a list
                of tokens, which correspond to the terminal nodes in a section hierarchy.
                We use the following four terminal types in our section hierarchy:
            </p>

            <ol>
                <li><code>numbering</code>, for numbering in a section heading</li>
                <li><code>title text</code>, for text in a section heading</li>
                <li><code>text block</code>, for running text outside of a section heading</li>
                <li><code>newline</code>, for newlines</li>
            </ol>
            <p>
                The selection of token types is rather arbitrary. These types were inspired
                by the existing XML tags of Rechtspraak.nl, and what is useful for creating a section
                structure. One may, of course, invent any other token type to suits one's needs.
            </p>
            <p>
                We should obviously tokenize the source
                documents to tokens that might be labeled with
                any of the above token types.
                In this regard, newlines are trivial to detect, and we assume
                that Rechtspraak.nl has already done the job of splitting text blocks
                in <code>para</code> tags, which roughly demarcate
                titles and text blocks,
                but numberings often appear within these text nodes
                unannotated. So in our parsing algorithm, we
                assume that numberings occur as the beginning of a text block and
                represent Arabic or Roman
                numerals or an alphabetic numbering. In this manner, we tokenize potential numberings.
            </p>

            <p>
                One complication with creating a list of tokens
                is that Rechtspraak.nl delivers an {abbrs.xml} tree, which is potentially more
                rich than the linear list that we reduce the document to.
                Indeed, it often happens that
                multiple <code>para</code> tags are wrapped in
                a <code>paragroup</code> tag, which sometimes represents a coherent set of paragraphs.

                On the other hand,
                sometimes the specified paragraph grouping makes no sense.
                Classifying a tree structure of tokens instead of a linear list
                can be done efficiently with {abbrs.crfs}, as in {ref.cite(bib.bradley2010learning)},
                but working with tree structures requires
                a much more complicated pipeline.
                So for simplicity we ignore most of those 'higher-level' tags, at the cost of potentially
                losing semantic markup.
            </p>

            <p>
                To summarize: our tokenization algorithm
                returns
                a linear sequence of tokens, which serves as input for our tagging operation.
                It is possible
                that we lose some information in the tokenization process, although this is
                avoidable in theory.
            </p>

            <p>
                In the next chapter, we explore how to tag a list of text elements with the four
                target labels introduced above.
            </p>
        </div>;
    }
}
