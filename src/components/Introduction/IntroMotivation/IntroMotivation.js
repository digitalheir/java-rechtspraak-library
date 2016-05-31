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
                Having a section hierarchy in case law documents
                is obviously useful for rendering the documents to human users: it
                allows us to display a table of contents and to style section titles.
                It also allows search engines to better index the subsections of a
                text to make it better searchable for legal users, and is a stepping
                stone to more advanced text mining operations,
                such as singling out the final judgment, perform information extraction,
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
                documents are barely marked up. Even older documents produce
                legal knowledge, so it is desirable to have these documents in good
                shape.
            </p>

            <MarkupStatsFigure/>
        </div>;
    }
}
