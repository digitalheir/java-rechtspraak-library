//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
// import Introduction from './Introduction/Introduction';
import Chapter from '../Chapter/Chapter';
import Methods from './Methods/Methods';
import Evaluation from './Evaluation/Evaluation';
import DeterministicTagger from './Methods/DeterministicTagger/DeterministicTagger';
import CRF from './Methods/CRF/CRF';
import chapters from '../../../chapters';

const taggingSections = {
    // intro: {
    //     id: 'introduction',
    //     title: "Introduction",
    //     component: Introduction
    // },
    // methods: {
    //     id: 'methods',
    //     title: "Methods",
    //     component: Methods
    // },
    methods: {
        id: 'crf',
        title: "Conditional Random Fields",
        component: CRF
    },
    manual: {
        id: 'manual',
        title: 'Deterministic Tagger',
        component: DeterministicTagger
    },
    evaluation: {
        id: 'evaluation',
        title: 'Evaluation',
        component: Evaluation
    }
};

taggingSections.inOrder = [
    taggingSections.methods,
    taggingSections.manual,
    taggingSections.evaluation
];


export default class Tagging extends Component {
    static title() {
        return chapters.tagging.title;
    }

    //noinspection JSUnusedGlobalSymbols
    getSections() {
        return taggingSections;
    }

    render() {
        var relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");
        const urlToRs = relativeToRoot + chapters.rechtspraakNl.route.replace('/', '');
        return <Chapter
            path={this.props.path}
            title={Tagging.title()}
            sections={taggingSections.inOrder}>
            <p>In the <a href={urlToRs}>previous chapter</a> we developed
                a way to import
                Rechtspraak.nl XML documents and distill them into
                a list of text elements, or tokens. In this chapter,
                we consider how to label these tokens with any of four
                labels:
            </p>
            <ol>
                <li><code>numbering</code>, for numbering in a section heading</li>
                <li><code>title text</code>, for text in a section heading</li>
                <li><code>text block</code>, for running text outside of a section heading</li>
                <li><code>newline</code>, for newlines</li>
            </ol>

            <p>
                It may be hard to distinguish what
                should properly be called a section, and so what is a section heading, even as a human reader.
                Consider, for example,
                a numbered enumeration of facts, which might be considered a list or a section sequence.

                We utilize a broad definition of 'section' and consider each titled or numbered text block
                as a section, inspired by the HTML5 definition of <code>section</code>:
            </p>

            <blockquote cite="https://www.w3.org/TR/html5/sections.html#the-section-element">
                A section, in this context, is a thematic grouping of content.
                The theme of each section should be identified, typically by including
                a heading (h1-h6 element) as a child of the section element.
            </blockquote>

            <p>
                Labeling a string of tokens is a task that has been widely covered in literature,
                mostly in the application of part-of-speech tagging in natural language.
                Popular methods include graphical models that model probability distributions
                of labels and observations, such as Hidden Markov Models (HMMs) and Conditional Random
                Fields (CRFs).
                In this chapter, we experiment with Conditional Random Fields
                for labeling our tokens, and we compare the results
                to a hand-written deterministic tagger that utilizes comparable features.
            </p>
            TODO results?
        </Chapter>;
    }
}
