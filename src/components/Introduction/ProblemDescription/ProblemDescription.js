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
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div>
            <div style={{pageBreakInside: 'avoid'}}>
                <p>
                    The problem that we investigate in this thesis, then, is whether we can
                    enrich the markup of scarcely marked up documents in Rechtspraak.nl by
                    automatically assigning a
                    section hierarchy to the text elements.
                    We divide this problem in the following subtasks:
                </p>
                <ol>
                    <li><a href={"#"+introSections.importing.id}>Importing documents from the Rechtspraak.nl web
                        service;</a></li>
                    <li><a href={"#"+introSections.importing.id}>Tokenizing relevant text elements;</a></li>
                    <li><a href={chapters.pathTo(this.props.path, chapters.tagging)}>Labeling these text elements
                        with their respective roles (i.e. <code>section title</code>; <code>numbering</code>; <code>text
                            block</code>; <code>newline</code>);</a>
                    </li>
                    <li><a href={chapters.pathTo(this.props.path, chapters.documentStructure)}>
                        Combining the tokens in such a way that they represent the
                        most likely section hierarchy;</a>
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
                these subject briefly, prefaced by
                a specification of the data set
                of <a href="http://www.rechspraak.nl">Rechtspraak.nl</a> that we use.
            </p>
            <p>
                Tasks 3 and 4 require more complicated machinery than
                importing and tokenization does,
                so these topics merit a more comprehensive explication.
                We describe our treatment of tasks 3 and 4 as two
                separate chapters that
                are similarly structured: we introduce the
                problem to solve, describe the methods used
                to solve the problem,
                and report and discuss experimental results.
            </p>
        </div>;
    }
}
