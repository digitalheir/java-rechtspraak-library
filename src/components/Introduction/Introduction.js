//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import IntroRechtspraakNl from '../RechtspraakNl/IntroRechtspraakNl/IntroRechtspraakNl'
import RechtspraakNlMarkup from '../RechtspraakNl/RechtspraakNlMarkup/RechtspraakNlMarkup'
import Importing from '../RechtspraakNl/Importing/Importing'
import Chapter from '../Chapter/Chapter'


const introIntroSections = {
    rechtspraakNl: {
        id: 'rechtspraak-nl',
        title: "Rechtspraak.nl",
        component: IntroRechtspraakNl
    }, rechtspraakNlMarkup: {
        id: 'rechtspraak-nl-markup',
        title: "Rechtspraak.nl markup",
        component: RechtspraakNlMarkup
    },
    importing: {
        id: 'importing',
        title: 'Importing & Tokenizing Data',
        component: Importing
    }
};

introIntroSections.inOrder = [
    introIntroSections.rechtspraakNl,
    introIntroSections.rechtspraakNlMarkup,
    introIntroSections.importing,
    introIntroSections.tokenization,
];


export default class Introduction extends Component {
    static title() {
        return "Introduction"
    }


    //noinspection JSUnusedGlobalSymbols
    static getSections() {
        return introIntroSections;
    }

    render() {
        return <Chapter path={this.props.path} title={Introduction.title()} sections={introIntroSections.inOrder}>
            <p>
                In this thesis, we explore the problem of automatically assigning a document structure
                to sparsely marked up documents in the Dutch case law repository
                of <a href="http://www.rechtspraak.nl/">Rechtspraak.nl</a>.
            </p>
            <p>
                Assigning a document structure
                is obviously useful in rendering the documents to human users (it
                allows us to display a table of content and style section titles), but
                it is also interesting for more advanced text mining
                applications such as topic modeling and information extraction. For example, we may
                want to represent some metadata in
                a machine-readable manner (e.g., the judgment: 'claimant won',
                'claimant lost', etc.). It would then be
                useful to know which text block
                contains the text to this judgment.
            </p>
            <p>
                In this chapter, we provide an introduction to the Rechtspraak.nl data set that we use, with an <a
                href="#markup">introduction
                to the case law XML markup</a>.
                We present Java library
                for querying and parsing the case law register of Rechtspraak.nl.

                TODO ...
            </p>
        </Chapter>
    }
}
