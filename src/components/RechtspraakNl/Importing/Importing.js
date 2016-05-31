//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Source from '../../Source/Source'
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';

export default class Importing extends Component {
    render() {
        return <div>
            
            <p>
                As noted above, we have a separate library for importing
                mirroring the Rechtspraak.nl corpus
                as a CouchDB database.

                The mirroring of data was done in order to facilitate the collection
                of statistics of case law documents used in this thesis. The Java project
                includes a module to convert XML to JSON and a number of MapReduce functions
                to generate the statistics used in this thesis.
                Source code for this project is available <a
                href="https://github.com/digitalheir/dutch-case-law-to-couchdb">on GitHub</a>.
            </p>

            <p>
                Regarding tokenization, we need some forward thinking to determine
                how to split Rechtspraak.nl XML texts.
                We are going to use four terminal nodes in our section hierarchy:
            </p>

            <ol>
                <li><code>numbering</code>, for numbering in a section heading</li>
                <li><code>title text</code>, for text in a section heading</li>
                <li><code>text block</code>, for running text outside of a section heading</li>
                <li><code>newline</code>, for newlines</li>
            </ol>

            <p>
                So we should tokenize XML documents into portions that could be labeled any of the above.
                In this regard, newlines are trivial to detect, and we assume
                that Rechtspraak.nl has already done a job of splitting text blocks through <code>para</code> tags,
                but still there are a number of arbitrary decisions we need to make about how granular a token must be.
            </p>

            <p>
                One noteworthy issue is that numberings are often not annotated as such, and so
                are part of a running text. In our parsing algorithm, we
                assume that
                potential numberings occur as the beginning of a text block and represents Arabic or Roman
                numerals, alphabetic numbering or a list marking (i.e. some symbol such
                as '-' or '*').
            </p>

            <p>
                Another complication is that Rechtspraak.nl delivers an XML tree, which is potentially more
                rich than the linear list of tokens that we are creating. Indeed, it often happens that
                multiple <code>para</code> tags are wrapped in
                a <code>paragroup</code>, which sometimes represents a coherent set of paragraphs. On the other hand,
                sometimes the specified paragraph grouping makes no sense. Although it is possible to efficiently tag tree
                structures instead of a linear list of tokens ({ref.cite(bib.bradley2010learning)}), this requires
                a somewhat more complicated pipeline, and may also be problematic when creating the document hierarchy.
            </p>
            <p>
                So the tagging algorithm that we use supposes
                a linear sequence of tokens. This means that it is possible
                that we lose some information in the process, because we coerce a tree structure into a linear
                sequence, and then build a tree structure which may clash with the original tree structure.
                Merging the two resulting tree structures is possible, but not currently implemented.
            </p>

            <p>
                In the next chapter, we explore how to tag the tokens that we have created from source XML.
                That is: given a list of text elements, we would like to assign each element a label.
            </p>
        </div>;
    }
}
