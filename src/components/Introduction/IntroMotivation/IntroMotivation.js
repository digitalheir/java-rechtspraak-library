//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

import abbrs  from '../../abbreviations'
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
                in {abbrs.xml} and {abbrs.html} dating back to about <span dateTime="1970">1970</span>. Most documents
                contain little semantic markup,
                such as element tags detailing the structure
                of (sub-)sections in a document.
            </p>
            <p>
                It is useful to have such a section hierarchy, however.
                It is obviously useful for rendering documents to human users: a clear section hierarchy
                allows us to display a table of contents and to style section titles.
                Furthermore, because sections usually chunk similar kinds of information together,
                a good section hierarchy also allows search engines
                to better index
                texts by localizing semantic units,
                which in turn makes these documents better searchable for legal users.
                It is also a stepping
                stone to make the documents machine readable.
                A richly marked up document enables advanced text mining operations,
                such as automatically extracting the final judgment,
                extracting the judge's considerations,
                etcetera.
            </p>

            <p>
                There is a recent trend on Rechtspraak.nl
                towards publishing more richly marked up documents, as we can see in <FigRef
                fig={figs.markupStats}/>.
                Still, an overwhelmingly large portion remains of documents which contain no or
                only sparse markup.
                To illustrate: at the time of writing, 78.7% of all judgment texts on Rechtspraak.nl do not
                contain any <code>section</code> tag, implying that a large number of
                documents are barely marked up.
                These documents are mostly made up of documents from before <span dateTime="2013">2013</span>.
                Older case law documents still produce
                legal knowledge, so it is desirable to have these older documents in good
                shape as well.
            </p>

            <MarkupStatsFigure/>
        </div>;
    }
}
