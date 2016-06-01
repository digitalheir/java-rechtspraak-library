//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

import MarkupStatsFigure from '../../Figures/MarkupStatsFigure/MarkupStatsFigure'
import FigRef from '../../Figures/FigRef'
import FigImg from '../../Figures/Image/Image'
import figs from '../../Figures/figs'
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';
import F from  '../../Math/Math';

export default class IntroMotivation extends Component {
    static id() {
        return "motivation";
    }

    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div>
            <p>
                Rechtspraak.nl publishes an open data set of Dutch case law
                in XML and HTML
                dating back to about 1970. Many of these documents
                contain little more information than the words in the
                document and some limited
                metadata. Most documents have limited semantic markup,
                such as element tags detailing the structure
                of (sub-)sections in a document.
            </p>
            <p>
                Having a section hierarchy in these documents
                is obviously useful to have, however.
                It is obviously useful for rendering documents to human users: it
                allows us to display a table of contents and to style section titles.
                Because sections usually chunk similar kinds of information together,
                a good section hierarchy also allows search engines
                to better index the
                texts by localizing semantic units,
                which in turn makes these documents better searchable for legal users.
                It is also a stepping
                stone to make the documents machine readable.
                A richly marked up document enables advanced text mining operations,
                such as automatically extracting the final judgment,
                extracting the judge's considerations,
                etc.
            </p>

            <p>
                There is a recent trend among the court judgments on Rechtspraak.nl
                towards more richly marked up documents, as we can see in <FigRef
                fig={figs.markupStats}/>.
                However,
                an overwhelmingly large portion of documents remains which contain no or
                only sparse markup.
                To illustrate: at the time of writing, 78.7% of all judgment texts on Rechtspraak.nl do not
                contain any <code>section</code> tag, implying that a large amount of
                documents are barely marked up. Older case law documents still produce
                legal knowledge, so it is still desirable to have these
                older documents in good
                shape.
            </p>

            <MarkupStatsFigure/>
        </div>;
    }
}
