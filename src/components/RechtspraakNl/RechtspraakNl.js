//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Markup from './Markup/Markup'

import Chapter from '../Chapter/Chapter'
import chapters from '../../../chapters'

const rechtspraakNlSections = {
    //metadata: {
    //    id: 'metadata',
    //    title: "Metadata"
    //},
    // markup: {
    //     id: 'markup',
    //     title: "Markup",
    //     component: Markup
    // },

};

rechtspraakNlSections.inOrder = [
    //sections.metadata,
    // sections.markup,
    rechtspraakNlSections.importing
];


export default class RechtspraakNl extends Component {
    static title() {
        return chapters.rechtspraakNl.title;
    }

    //noinspection JSUnusedGlobalSymbols
    static getSections() {
        return rechtspraakNlSections;
    }

    render() {

        return <Chapter
            path={this.props.path}
            title={RechtspraakNl.title()}
            sections={rechtspraakNlSections.inOrder}>
            <p>
                In this chapter, we consider the Rechtspraak.nl data service, present a Java library
                for working with it, and align our goals of enriching markup with existing
                Rechtspraak.nl document.
            </p>
            

        </Chapter>;
    }
}
