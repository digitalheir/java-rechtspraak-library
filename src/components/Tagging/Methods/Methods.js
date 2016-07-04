//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import chapters from '../../../../chapters';
import ChapterSectionContent from '../../Chapter/ChapterSectionContent';
import ref from '../../../../Bibliography/References/references'
import bib from  '../../../../Bibliography/bib';

const methodsSections = {
    featureSelection: {
        id: 'feature-selection',
        title: "Feature Selection"
    },
    crf: {
        id: 'crf',
        title: "Conditional Random Fields"
    },
    deterministic: {
        id: 'deterministic-tagger',
        title: "Deterministic tagger"
    }
};

methodsSections.inOrder = [
    methodsSections.featureSelection,
    methodsSections.crf
    // methodsSections.deterministic
];


export default class Tagging extends Component {
    static title() {
        return chapters.tagging.title;
    }

    //noinspection JSUnusedGlobalSymbols
    static getSections() {
        return methodsSections;
    }

    render() {
        return <ChapterSectionContent {...this.props} sections={methodsSections.inOrder}>
            <p>
                For the purpose of tagging, we use a class of statistical classifiers called
                Conditional Random Fields (CRFs). We use this technique because CRFs
                tend to have state-of-the-art performance
                in sequenced pattern recognition tasks, such as
                DNA/RNA sequencing ({ref.cite(bib.lafferty2001conditional)}), shallow
                parsing ({ref.cite(bib.sha2003shallow)})
                and named entity recognition ({ref.cite(bib.settles2004biomedical)}). One
                downside to CRFs is that models commonly overfit to a single corpus.
                In our case, this is not a problem because
                we expressly train for only one particular corpus.
            </p>
        </ChapterSectionContent>;
    }
}
