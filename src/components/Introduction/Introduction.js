//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Introduction from '../RechtspraakNl/IntroRechtspraakNl/IntroRechtspraakNl'
import Chapter from '../Chapter/Chapter'
const introIntroSections = {
    rechtspraakNl: {
        id: 'rechtspraak-nl',
        title: "Rechtspraak.nl",
        component: Introduction
    },
};

introIntroSections.inOrder = [
    introIntroSections.rechtspraakNl
];


export default class extends Component {
    title() {
        return "Introduction"
    }


    //noinspection JSUnusedGlobalSymbols
    getSections() {
        return introIntroSections;
    }

    render() {
        return <Chapter path={this.props.path} title={this.title()} sections={introIntroSections.inOrder}/>
    }
}
