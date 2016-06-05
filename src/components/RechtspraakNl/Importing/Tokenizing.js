//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Source from '../../Source/Source'
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';

export default class Importing extends Component {
    render() {
        return <div>
            <p>
                Regarding tokenization, we need to do some forward thinking
                in order to determine
                how to split XML texts from Rechtspraak.nl.
                We assume a text to be decomposable into a list
                of tokens, which correspond to the terminal nodes in a section hierarchy.
                We use the following four terminal nodes in our section hierarchy:
            </p>

            <ol>
                <li><code>numbering</code>, for numbering in a section heading</li>
                <li><code>title text</code>, for text in a section heading</li>
                <li><code>text block</code>, for running text outside of a section heading</li>
                <li><code>newline</code>, for newlines</li>
            </ol>
            <p>
                One
                may of course invent any other combination of token types.
            </p>
            <p>
                We should obviously tokenize the source
                documents to tokens that might be labeled
                any of the above token types.
                In this regard, newlines are trivial to detect, and we assume
                that Rechtspraak.nl has already done a job of splitting text blocks in <code>para</code> tags,
                but numberings are often not annotated as such, and so
                are part of a running text. In our parsing algorithm, we
                assume that
                numberings occur as the beginning of a text block and represents Arabic or Roman
                numerals, alphabetic numbering, and
                so potential numberings are tokenized.
            </p>

            <p>
                One complication with creating a list of tokens
                is that Rechtspraak.nl delivers an XML tree, which is potentially more
                rich than the linear list that we reduce the document to.
                Indeed, it often happens that
                multiple <code>para</code> tags are wrapped in
                a <code>paragroup</code> tag, which sometimes represents a coherent set of paragraphs.

                On the other hand,
                sometimes the specified paragraph grouping makes no sense.
                Classifying a tree structures of tokens instead of a linear list
                can be done efficiently with CRFs, as in {ref.cite(bib.bradley2010learning)},
                but working with tree structures requires
                a much more complicated pipeline.
                So we ignore most of those 'higher-level' tags.

            </p>
            <p>
                To summarize: our tokenization algorithm
                returns
                a linear sequence of tokens, which serves as input for our tagging operation.
                It is possible
                that we lose some information in the tokenization process, although this may be
                avoidable.
            </p>

            <p>
                In the next chapter, we explore how to tag a list of text elements with our four
                target labels.
            </p>
        </div>;
    }
}
