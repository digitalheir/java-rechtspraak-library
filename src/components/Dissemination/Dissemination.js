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
        return <Chapter path={this.props.path} title={Conclusion.title()} sections={dissSections.inOrder}>
            We have successfully demonstrated a method for assigning
            a section hierarchy to Dutch case law documents, reporting F<sub>1</sub> scores
            of 0.95 and 0.91 for tagging and parsing, respectively. Although these scores are good,
            they can likely be improved.
        </Chapter>
    }
}
