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
        return <Chapter chapter={true} inline={!!this.props.inline} path={this.props.path} title={Introduction.title()}
                        sections={introIntroSections.inOrder}>
            <p>
                In this thesis, we explore the problem of automatically assigning a section hierarchy
                to sparsely marked up documents in the Dutch case law repository
                of <a href="http://www.rechtspraak.nl/">Rechtspraak.nl</a>.
            </p>
            <p>
                Having a section hierarchy
                is obviously useful for rendering the documents to human users: it
                allows us to display a table of contents and to style section titles. But
                it is also interesting for more advanced text mining
                applications, such as topic modeling and information extraction. For example, we may
                want to supply sections with some metadata (such as a summary). It would then be
                useful to have the section hierarchy available.
            </p>
            <p>
                In this chapter, we provide an introduction to the Rechtspraak.nl data set that we
                experiment on, with an
                introduction to the case law XML markup.
            </p>
            <p>
                Near the end of this chapter, we present a Java library
                that bundles all the work that we have undertaken in this thesis. This library is effectively
                a pipeline for enriching Dutch case law markup with a section hierarchy.
            </p>
        </Chapter>
    }
}
