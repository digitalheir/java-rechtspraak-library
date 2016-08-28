//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

import FigRef from './../../Figures/FigRef'
import FigImg from './../../Figures/Image/Image'
import figs from './../../Figures/figs'
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';
import F from  '../../Math/Math';
import Source from '../../Source/Source.js'
import chapters from '../../../../chapters'
import introSections from './../../ImportingAndTokenizing/sections'
export default class ProblemDescription extends Component {
    static id() {
        return "problem-description";
    }

    render() {
        //const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div>
            <div style={{pageBreakInside: 'avoid'}}>
                <p>
                    The problem that we investigate in this thesis, then, is whether we can
                    enrich the markup of scarcely marked up documents in Rechtspraak.nl by
                    automatically assigning a
                    section hierarchy to the text elements.
                    We divide this problem into the following subtasks:
                </p>
                <ol>
                    <li><a hrefLang="en" href={chapters.pathTo(this.props.path, chapters.importing)+"#"+introSections.importing.id}>Importing documents from the Rechtspraak.nl web
                        service;</a></li>
                    <li><a hrefLang="en" href={chapters.pathTo(this.props.path, chapters.importing)+"#"+introSections.importing.id}>Tokenizing relevant text elements;</a></li>
                    <li><a hrefLang="en" href={chapters.pathTo(this.props.path, chapters.tagging)}>Labeling these text elements
                        with their respective roles (i.e. <code>section title</code>; <code>numbering</code>; <code>text
                            block</code>; <code>newline</code>);</a>
                    </li>
                    <li><a hrefLang="en" href={chapters.pathTo(this.props.path, chapters.documentStructure)}>
                        Combining the tokens in such a way that they represent the
                        most likely section hierarchy</a>
                    </li>
                    <li style={{display:'none'}}>
                        Publishing the resulting documents so that search engines
                        can make use of the enriched markup
                    </li>
                </ol>
            </div>
            <p>
                Tasks 1 and 2 are theoretically straightforward and mostly a
                problem of implementation, and the following chapter touches on
                both of these subjects briefly, mostly through
                a specification of the data set
                of court judgments from <a hrefLang="nl" href="http://www.rechspraak.nl">Rechtspraak.nl</a>.
            </p>

            <p>
                Task 3 describes labeling the text elements with their roles in the text,
                which we translate into the relevant markup tags.
                This is achieved by training a Conditional Random Field on
                a set of manually labeled documents. The trained model
                is then able to correctly label most elements:
                for all labels we report F<sub>1</sub> scores of around
                1.0, except for section titles: for these we 
                report 0.91.
            </p>

            <p>
                Task 4, organizing the tagged elements into a section hierarchy,
                is approached as a probabilistic parsing problem. We create a
                Context-Free Grammar which accepts a list of text elements as tokens
                and creates a parse tree which represents the section hierarchy.
                This approach returns a desirable section hierarchy in most cases:
                in our experiment we report an F<sub>1</sub> score of 0.92.
            </p>

            <p>
                Tasks 3 and 4 require more complicated machinery than
                importing and tokenization do,
                so these topics merit a more comprehensive explication.
                We describe our treatment of tasks 3 and 4 as two
                separate chapters, which
                are similarly structured: first, we introduce the
                problem to solve, then describe the methods used
                to solve the problem,
                and finally report and discuss experimental results.
            </p>
        </div>;
    }
}
