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
                is obviously useful for rendering the documents to human users: it
                allows us to display a table of contents and to style section titles. But
                it is also interesting for more advanced text mining
                applications such as topic modeling and information extraction. For example, we may
                want to assign sections with some metadata (such as a summary). It would then be
                useful to have a section hierarchy ready.
            </p>
            <p>
                In this chapter, we provide an introduction to the Rechtspraak.nl data set that we use, with an
                introduction
                to the case law XML markup.

                In the final section of this chapter, we present a Java library
                that contains all the work that we have undertaken in this thesis, which effectively is
                a pipeline for enriching Dutch case law markup. In this chapter, we also make some remarks on
                importing and tokenization of Dutch case law documents. Element tagging and section parsing
                both require more complicated machinery than tokenization, so these topics merit their own chapter.
            </p>
        </Chapter>
    }
}
