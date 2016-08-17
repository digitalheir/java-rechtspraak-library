//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import chapters from '../../../chapters'
import {getHandler} from '../../Routes.jsx'
import Introduction from '../Introduction/Introduction';
import Tagging from '../Tagging/Tagging';
import InferringDocumentStructure from '../InferringDocumentStructure/InferringDocumentStructure';
import ToC from '../ToC/ToC'
import AbstractContent from '../Abstract/AbstractContent'

const title = require('../../../title');

export default class FullThesis extends Component {
    //<p>
    //    The source code referred to in this thesis can be found in two repositories on GitHub:
    //</p>
    //<ul>
    //    <li><a href="https://github.com/digitalheir/java-rechtspraak-library"
    //    >https://github.com/digitalheir/java-rechtspraak-library</a>
    //    </li>
    //    <li><a href="https://github.com/digitalheir/dutch-case-law-to-couchdb"
    //    >https://github.com/digitalheir/dutch-case-law-to-couchdb</a>
    //    </li>
    //</ul>
    render() {
        return <div className="full-thesis reset-counter numbered-section">
            <section style={{}} id="title-page">
                <div style={{}} id="title-page-university-name">Utrecht University</div>
                <div style={{}} id="title-page-faculty-name">Faculty of Humanities</div>
                <div style={{}} id="title-page-msc-thesis">Master of Science Thesis</div>
                <h1 style={{}}>{title}</h1>
                <div style={{}} id="title-page-author">
                    <div style={{}} id="title-page-author-author">by</div>
                    <div style={{}} id="title-page-author-name">Maarten Trompper</div>
                </div>
                <div style={{}} className="title-page-supervisor">
                    <div style={{}} id="title-page-supervisor-supervisor">Supervisor Utrecht University:</div>
                    <div style={{}} className="name">Ad Feelders</div>
                </div>
                <div style={{}} className="title-page-supervisor external">
                    <div style={{}} id="title-page-supervisor-ext-supervisor">Supervisor Leibniz Center for Law:</div>
                    <div style={{}} className="name">Radboud Winkels</div>
                </div>
                <div style={{}} id="title-page-time-and-date">
                    <span style={{}} id="title-page-time">
                        Utrecht
                    </span>, <span style={{}} id="title-page-date">2016</span>
                </div>
            </section>

            <section className="chapter">
                <h2>Abstract</h2>
                <AbstractContent/>

                <hr style={{marginTop: '60em'}}/>
                <p>
                    This is a print version
                    of a web site.
                    Visit <a
                    href="https://digitalheir.github.io/java-rechtspraak-library/"
                >https://digitalheir.github.io/java-rechtspraak-library/</a> for
                    for a version of this thesis with hyperlinks and some interactive elements.
                </p>

            </section>

            <section id="#toc" className="chapter">
                <h2>Table of Contents</h2>
                <ToC singlePage={true} showHome={false} {...this.props}/>
            </section>

            {
                chapters.inOrder.map(chapter => {
                    const ChapterContent = getHandler(chapter.route);
                    return <ChapterContent key={chapter.route} id={chapter.id} inline={true} {...this.props}/>;
                })
            }

        </div>
    }
}
