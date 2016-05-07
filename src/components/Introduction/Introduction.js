//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import IntroRechtspraakNl from '../RechtspraakNl/IntroRechtspraakNl/IntroRechtspraakNl'
import RechtspraakNlMarkup from '../RechtspraakNl/RechtspraakNlMarkup/RechtspraakNlMarkup'
import Importing from '../RechtspraakNl/Importing/Importing'
import Chapter from '../Chapter/Chapter'


import introIntroSections from './sections';


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
                In this thesis, we explore the problem of automatically assigning a section hierarchy 
                to sparsely marked up documents in the Dutch case law repository
                of <a href="http://www.rechtspraak.nl/">Rechtspraak.nl</a>.
            </p>
            <p>
                Having a section hierarchy
                is obviously useful when rendering the documents to human users: it
                allows us to display a table of contents and to style section titles. But
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

                In the final section of this chapter, we present the Java library
                that contains all the work we have undertaken in this thesis, which effectively is
                a pipeline for  enriching Dutch case law markup. We make some remarks on
                importing and tokenization of Dutch case law documents, followed by chapters
                on element tagging and parsing a section tree, which both require more complicated 
                machinery than tokenization.
            </p>
        </Chapter>
    }
}
