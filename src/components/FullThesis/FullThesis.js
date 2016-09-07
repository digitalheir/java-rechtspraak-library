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
    //    <li><a hrefLang="en" href="https://github.com/digitalheir/java-rechtspraak-library"
    //    >https://github.com/digitalheir/java-rechtspraak-library</a>
    //    </li>
    //    <li><a hrefLang="en" href="https://github.com/digitalheir/dutch-case-law-to-couchdb"
    //    >https://github.com/digitalheir/dutch-case-law-to-couchdb</a>
    //    </li>
    //</ul>
    render() {
        // itemProp="alumniOf"
        return <div className="full-thesis reset-counter numbered-section">
            <section id="title-page">
                <div itemScope={true}
                     itemRef="uu"
                     itemType="https://schema.org/CollegeOrUniversity">
                    <div itemProp="name"
                         id="title-page-university-name">
                        <span itemProp="name">Utrecht University</span>
                        <div itemProp="address" itemScope={true} itemType="https://schema.org/PostalAddress">
                            <meta itemProp="addressCountry" content="the Netherlands"/>
                            <meta itemProp="addressLocality" content="Utrecht"/>
                            <meta itemProp="postalCode" content="3584 CS"/>
                            <meta itemProp="addressLocality" content="De Uithof"/>
                            <meta itemProp="streetAddress" content="Heidelberglaan 8"/>
                        </div>
                    </div>
                    <div itemScope={true}
                         itemProp="department"
                         itemType="https://schema.org/EducationalOrganization"
                         id="title-page-faculty-name">
                        <span itemProp="name">Faculty of Humanities</span>
                        <link itemProp="alumni" rel="#title-page-author"/>
                    </div>
                </div>
                <div id="title-page-msc-thesis"><span itemProp="inSupportOf">Master of Science</span> Thesis</div>
                <h1 >{title}</h1>
                <div itemScope={true}
                     itemProp="author"
                     itemType="https://schema.org/Person"
                     id="title-page-author">
                    <div id="title-page-author-author">by</div>
                    <div

                        id="title-page-author-name">
                        <span itemProp="name">
                            <span itemProp="givenName">Maarten</span> <span itemProp="familyName">Trompper</span>
                        </span>
                    </div>
                </div>
                <div itemScope={true}
                     itemProp="contributor"
                     itemType="https://schema.org/Person"
                     className="title-page-supervisor">
                    <div id="title-page-supervisor-supervisor">Supervisor <span itemScope={true}
                                                                                itemType="https://schema.org/EducationalOrganization"
                                                                                itemRef="uu"
                                                                                itemProp="worksFor affiliation">
                            <span itemProp="name">Utrecht University</span>
                        </span>
                        :
                    </div>
                    <div itemProp="name" className="name">
                        <span itemProp="givenName">Ad</span> <span itemProp="familyName">Feelders</span>
                    </div>
                </div>
                <div itemScope={true}
                     itemProp="contributor"
                     itemType="https://schema.org/Person"
                     className="title-page-supervisor external">
                    <div id="title-page-supervisor-ext-supervisor">Supervisor <span itemScope={true}
                                                                                    itemType="https://schema.org/EducationalOrganization"
                                                                                    itemProp="affiliation">
                        <span itemProp="name">Leibniz Center for Law</span>
                    </span>:
                    </div>
                    <div itemProp="name" className="name">
                        <span itemProp="givenName">Radboud</span> <span itemProp="familyName">Winkels</span>
                    </div>
                </div>
                <div id="title-page-time-and-date">
                    <span id="title-page-time">
                        <span itemScope={true} itemProp="">
                        Utrecht
                            </span>
                    </span>, <span itemProp="dateCreated datePublished" id="title-page-date">2016</span>
                </div>
            </section>

            <section className="chapter">
                <h2>Abstract</h2>
                <AbstractContent/>

                <hr style={{marginTop: '59em'}}/>
                <p style={{marginBottom: 0}}>
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
