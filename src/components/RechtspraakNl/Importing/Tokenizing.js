//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Source from '../../Source/Source'
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';

export default class Importing extends Component {
    render() {
        return <div>
            <p>
                Regarding tokenization, we need some forward thinking to determine
                how to split Rechtspraak.nl XML texts.
                We assume a text to be decomposable into a list
                of tokens, which correspond to the terminal nodes in a section hierarchy.
                We use the following four terminal nodes in our section hierarchy (one
                may of course invent any other combination of token types):
            </p>

            <ol>
                <li><code>numbering</code>, for numbering in a section heading</li>
                <li><code>title text</code>, for text in a section heading</li>
                <li><code>text block</code>, for running text outside of a section heading</li>
                <li><code>newline</code>, for newlines</li>
            </ol>

            <p>
                We should then obviously tokenize the source
                documents into elements that could be labeled with any of the above token types.
                In this regard, newlines are trivial to detect, and we assume
                that Rechtspraak.nl has already done a job of splitting text blocks through <code>para</code> tags,
                but numberings are often not annotated as such, and so
                are part of a running text. In our parsing algorithm, we
                assume that
                numberings occur as the beginning of a text block and represents Arabic or Roman
                numerals, alphabetic numbering or a list marking (i.e. some symbol such
                as '-' or '*'), and we apply some arbitrary rules to determine whether a
                potential number in actually a numbering or just a number in the text.
            </p>

            <p>
                One complication with creating a list of tokens
                is that Rechtspraak.nl delivers an XML tree, which is potentially more
                rich than the linear list that we reduce the document to.
                Indeed, it often happens that
                multiple <code>para</code> tags are wrapped in
                a <code>paragroup</code> tag, which sometimes represents a coherent set of paragraphs. On the other hand,
                sometimes the specified paragraph grouping makes no sense. Although it is possible to efficiently tag
                tree
                structures instead of a linear list of tokens ({ref.cite(bib.bradley2010learning)}), this requires
                a much more complicated pipeline, and may also be problematic when creating the document hierarchy.
                So we ignore most of those 'higher-level' tags.
            </p>
            <p>
                To summarize: our tagging algorithm
                receives as input
                a linear sequence of tokens. This means that it is possible
                that we lose some information in the tokenization process.
            </p>

            <p>
                In the next chapter, we explore how to tag the list of tokens that we have created using our four
                target labels.
                That is: given a list of text elements (tokens), we would like to assign each token a label.
            </p>
        </div>;
    }
}
