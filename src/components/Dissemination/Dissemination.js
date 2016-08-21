//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Chapter from '../Chapter/Chapter'
import chapters from '../../../chapters'


import dissSections from './sections';


export default class Conclusion extends Component {
    static title() {
        return chapters.conclusion.title;
    }


    //noinspection JSUnusedGlobalSymbols
    static getSections() {
        return dissSections;
    }

    render() {
        return <Chapter id={this.props.id}
                        inline={!!this.props.inline}
                        path={this.props.path}
                        chapter={true}
                        chapterObject={chapters.conclusion}
                        title={Conclusion.title()}
                        sections={dissSections.inOrder}>
            <p>
                We have successfully demonstrated a method to assign
                a section hierarchy to documents of Dutch court judgments.
            </p>

            <p>
                We have described a procedure to assign types to document elements of
                either <code>title</code>, <code>nr</code>, <code>newline</code> or <code>text block</code>
                using Conditional Random Fields,
                reporting an F<sub>1</sub> score of 0.91 and F<sub>0.5</sub> score of 0.91.
            </p>

            <p>
                Afterwards, have reviewed a procedure to organize those elements into a section hierarchy
                using Probabilistic Context Free Grammars,
                reporting an F<sub>1</sub> score of 0.92.
            </p>

            <p>
                Whether these results are good enough to be used in practice depends on one's
                tolerance to inaccuracies.
                As discussed, we rather miss opportunities to enrich data
                rather than to produce false information,
                so a low recall is preferable to low precision.
                The scores obtained for the classifier and parser are promising,
                but the procedures are not optimized extensively to the corpus, and can likely be improved to
                perform within a 5% error margin.
                In any case, mislabelings do not distort the text in such a way to render it illegible, so
                we can be somewhat forgiving of errors.
            </p>
        </Chapter>
    }
}
