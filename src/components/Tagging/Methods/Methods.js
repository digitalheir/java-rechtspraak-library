//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import chapters from '../../../../chapters';
import ChapterSectionContent from '../../Chapter/ChapterSectionContent';
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';
import abbrs from  '../../abbreviations';
import methodsSections from  './sections';


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
                Conditional Random Fields ({abbrs.crfs}). We use this technique because {abbrs.crfs} tend
                to have state-of-the-art performance
                in sequenced pattern recognition tasks, such
                as {abbrs.dna}/{abbrs.rna} sequencing ({ref.cite(bib.lafferty2001conditional)}), shallow
                parsing ({ref.cite(bib.sha2003shallow)}) and
                named entity recognition ({ref.cite(bib.settles2004biomedical)}).
            </p>
        </ChapterSectionContent>;
    }
}
