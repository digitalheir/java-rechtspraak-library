//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Source from '../../Source/Source'
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';

export default class Importing extends Component {
    render() {
        return <div>
            <p>
                Importing and tokenizing documents on Rechtspraak.nl is
                theoretically straightforward, so a couple of short comments on this.
            </p>

            <p>
                We have developed two
                separate Java libraries: one for importing
                (and enriching) documents from Rechtspraak.nl (
                <a href="https://github.com/digitalheir/java-rechtspraak-library">source
                    code on GitHub</a>), and one for mirroring the Rechtspraak.nl corpus
                as a CouchDB database.
            </p>

            <p>
                The latter project was created in order to facilitate the collection
                of statistics of case law documents used in this document,
                includes a module to convert XML to JSON and a number of MapReduce functions
                to generate statistics.
                The source code is available <a
                href="https://github.com/digitalheir/dutch-case-law-to-couchdb">on GitHub</a>.
            </p>

            <section>
                <h4>Tokenizing text elements</h4>
                <p>
                    In the next chapter, we explore a method to tag text elements
                    as one of four labels:
                </p>

                <ol>
                    <li><code>numbering</code>, for numbering in a section heading</li>
                    <li><code>title text</code>, for text in a section heading</li>
                    <li><code>text block</code>, for running text outside of a section heading</li>
                    <li><code>newline</code>, for newlines</li>
                </ol>
                <p>
                    Of course, first we need to identify the shape of these text elements.
                    In this regard, newlines and text blocks are trivial to detect. We assume
                    that these already been set by Rechtspraak.nl.
                </p>

                <p>
                    One complication is that Rechtspraak.nl delivers an XML tree, which is more rich than a linear
                    array of words. Indeed, it often happens that multiple <code>para</code> tags are wrapped in
                    a <code>paragroup</code>, which sometimes represents a coherent set of paragraphs and sometimes
                    makes no sense. In general, some arbitrary decisions must be made about the granularity of 
                    text blocks.
                </p>

                <p>
                    Another note is that numberings are often not annotated. In tokenization, we assume that
                    a potential numbering occurs as the beginning of a text block and represents Arabic or Roman
                    numerals, alphabetic numbering or a list marking (some symbol such
                    as <code>-</code> or <code>*</code>).
                </p>
            </section>
        </div>;
    }
}
