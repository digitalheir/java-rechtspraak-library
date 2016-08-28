//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../Figures/FigRef'
import FigImg from './../../Figures/Image/Image'
import figs from './../../Figures/figs'
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';
import F from  '../../Math/Math';

import FigureInfoText from '../../Figures/figure-info-tf-idf/figure-info-tf-idf';
import FigTitlePattern from '../../Figures/figure-title-pattern/fig-title-pattern';
import FigureRelativeTitleCountForTerms from '../../Figures/figure-relative-title-count-for-terms/figure-relative-title-count-for-terms';
import TitleTfIdfFigure from '../../Figures/figure-title-tf-idf/figure-title-tf-idf';
import TitleTfIdfFigurePerSection from '../../Figures/figure-section-title-tf-idf/figure-section-tf-idf';
import WordCountFig from '../../Figures/figure-word-count-title/figure-title-word-count';
import Source from './../../Source/Source';
import sections from './sections';

import abbrs  from '../../abbreviations'
export default class Introduction extends Component {
    //noinspection JSUnusedGlobalSymbols
    static getSections() {
        return sections;
    }

    render() {
        const nonValidatingXml = "https://validator.w3.org/nu/?useragent=Validator.nu%2FLV+http%3A%2F%2Fvalidator.w3.org%2Fservices&amp;doc=http%3A%2F%2Fuitspraken.rechtspraak.nl%2Finziendocument%3Fid%3DECLI%3ANL%3ACBB%3A2010%3ABN1294";

        const htmlManifestation = "http://uitspraken.rechtspraak.nl/inziendocument?id=ECLI:NL:CBB:2010:BN1294";
        const xmlManifestation = "http://data.rechtspraak.nl/uitspraken/content?id=ECLI:NL:CBB:2010:BN1294";

        const urlSchemeText = "http://data.rechtspraak.nl/uitspraken/content?id={ecli}";
        const urlSchemeUrl = "http://deeplink.rechtspraak.nl/uitspraak?id=ECLI:NL:CBB:2010:BN1294";

        var relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div className="section-content">
            <p><a hrefLang="nl" href="http://www.rechtspraak.nl/">Rechtspraak.nl</a> is the official website of
                the <span itemScope={true}
                          itemType="https://schema.org/GovernmentOrganization">Dutch
                judiciary</span>. The website hosts an open data portal for Dutch case law, containing metadata for
                about 2 million court judgments<Source hrefLang="nl"
                                                       href="http://data.rechtspraak.nl/uitspraken/zoeken?"/> and
                judgment texts for about 350.000 judgments in {abbrs.xml}<Source
                    hrefLang="nl" href="http://data.rechtspraak.nl/uitspraken/zoeken?return=doc"/>.
                In this thesis, we only consider those documents that
                contain text.
                The full data set
                of <a hrefLang="nl" href="http://www.rechtspraak.nl/">Rechtspraak.nl</a> court
                judgments contains
                only a fraction of all court judgments that exist
                in the Netherlands, but the collection is curated so that it is representative of
                all case law in the Netherlands.
            </p>


            <p>
                For a comprehensive study on the legal and technical background
                of the digital publication of Dutch case law, see {ref.cite(bib.vanopijnen2014)}.
                For a general overview of Rechtspraak.nl's web service, see {ref.cite(bib.trompper2014)}.
            </p>

            <p>
                We wish to automatically annotate documents in the corpus with some semantic markup,
                so it is helpful to see what is already done in this regard
                by <a href="http://www.rechtspraak.nl/">Rechtspraak.nl</a>.
                As noted earlier, recent documents tend to be more richly marked up than older
                documents. Indeed, most older documents consist exclusively
                of <code>para</code> and <code>paragroup</code> elements, denoting paragraphs
                and groups of paragraphs respectively.
            </p>

            <p>
                We observe that a richly marked up case law document
                typically consists of the following parts:
            </p>
            <ol className="nomargin nopadding">
                <li className="desc info" id="info">
                    <p>
                        The first element in a document is typically a unique header element with
                        a tag name of either <code>uitspraak.info</code> or <code>conclusie.info</code> for two types
                        of case law (judgments and conclusions, respectively). We refer to
                        either of these as <code>*.info</code> elements. <code>*.info</code> elements contain
                        interesting metadata such as names and court location. The information is generally
                        not semantically marked up, but is reasonably easy to parse thanks
                        to style consistencies (e.g., most units of metadata are on a separate line).
                    </p>

                    <div style={{display: 'none'}}>
                        <p>The <code>*.info</code> element typically
                            contains metadata about the legal case, such as:
                        </p>
                        <ul>
                            <li>A case law identifier following some identification
                                scheme
                            </li>
                            <li>Heading on the type of judgment (e.g. "U I T S P R A A K")</li>
                            <li>Date of judgment</li>
                            <li>Branch of the judiciary (i.e. type of court)</li>
                            <li>Location of the judgment (i.e. court, jurisdiction)</li>
                            <li>A description of the parties involved, possibly detailing
                                their names, roles, locations, representatives
                            </li>
                            <li>A reference to preceding cases, for example in the case of an
                                appeal to a previous judgment
                            </li>
                        </ul>
                        <p>
                            The order and formatting of this information appears in a multitude of order and
                            formatting, making it difficult to write a deterministic grammar for recognizing
                            a header section. [TODO figref] suggests that analyzing tf–idf in <code>*.info</code>
                            elements
                            does not seem to be a particularly useful method of generating features
                            that select for these metadata items. But it
                            is easy for the human eye to recognize some recurring patterns.
                        </p>
                        <p>See, e.g., <cite><a
                            href="https://rechtspraak.lawreader.nl/ecli/ECLI:NL:GHARL:2014:9139">ECLI:NL:GHARL:2014:9139</a>
                        </cite> for an example.</p>
                    </div>

                    <p>
                        Automatically marking up text portions within <code>*.info</code> tags is outside of the scope of
                        this thesis. For our further purposes, we do not distinguish header elements from
                        generic sections,
                        although this can easily be achieved by extending the label set
                        to include a <code>*.info</code> label.
                    </p>
                    <p>
                        A <code>*.info</code> element is generally followed by any number of <code>section</code> tags.
                    </p>
                </li>
                <li className="desc section">
                    <p>
                        <code>section</code> tags generally
                        contain a title element, and optionally contain an attribute which denotes the section role.
                        A <code>section</code> is a generic grouping of running text, and can be nested
                        to create a section hierarchy.
                    </p>
                    <p>
                        In practice, we
                        see three values for the <code>role</code> attribute.<Source
                        href=
                            "https://rechtspraak.cloudant.com/docs/_design/stats/_view/section_roles?group_level=1"
                    /> These
                        values are either
                    </p>
                    <ul>
                        <li>
                            <code>beslissing</code> (judgment)
                        </li>
                        <li>
                            <code>overwegingen</code> (considerations)
                        </li>
                        <li>
                            <code>procesverloop</code> (proceedings)
                        </li>
                    </ul>
                    <p>
                        Many <code>section</code> elements have no role, although one may imagine other roles
                        than the above, such
                        as <code>feiten</code> (facts). Assigning roles to sections
                        is an interesting avenue of research, but we do not explore this in this thesis. Instead,
                        we limit ourselves to demarcating sections and assigning some hierarchical section structure.
                    </p>
                </li>
                <li className="desc title avoid-page-break">
                    <p>
                        <code>title</code> elements typically occur as the
                        first descendant of
                        a <code>section</code> element, and contain either a
                        numbering or some text, or both. <code>title</code> elements
                        may occur elsewhere, for example
                        as labels for figures, but we only
                        consider section titles in this thesis.
                    </p>

                    <p>
                        Titles are the most difficult elements
                        to label, so we make
                        a special effort to describe common title patterns.
                        In <FigRef fig={figs.figTitleWordCount}/> we see that if
                        a <code>title</code> element contains text, it usually contains
                        only a handful of words. Close to 99% of section titles
                        contain 10 words or less.
                    </p>
                </li>

                <WordCountFig/>

                <p>
                    Title texts have a number of patterns that often recur. See <FigRef
                    fig={figs.figTitleTreemap}/> for a treemap of the distribution of
                    normalized title texts.
                </p>

                <FigTitlePattern/>

                <p>
                    From this figure, we learn that most of the section roles in use have a number of title strings that
                    are used very often, but there is also a greatly diverse set of variations that occur
                    less often. This figure encourages our use of CRFs: there are clearly identifiable patterns,
                    but too many variations to list exhaustively.
                </p>

                <p>
                    <FigRef fig={figs.tfidf}/> and <FigRef fig={figs.sectionsTfidf}/> list terms within
                    section title elements by tf–idf score,
                    which is a number that reflects how important a given term is
                    in an element with respect to all other elements in the corpus.
                </p>
                <p>
                    <strike>
                        tf–idf is short for 'term frequency–inverse document frequency'.
                        It represents the importance of a given word by taking the number of times
                        that word occurs in the document, and offsetting it against the number of
                        times that word occurs elsewhere in the corpus.
                    </strike>
                </p>
                <p>tf–idf is defined as follows: </p>

                <F {...this.props} display={true} l="\text{tfidf}(t, d, D) = \text{tf}(t, d)\cdot \text{idf}(t, D)"/>
                <p>where</p>
                <ul>
                    <li><F {...this.props} l="\text{tf}(t,d)"/> is some measure of the importance of a
                        term <F {...this.props} l="t"/> in a
                        given document <F {...this.props} l="d"/>. Let the raw frequency <F {...this.props}
                            l="f_{t,d}"/> be
                        the plain number of times the term <F {...this.props} l="t"/> in occurs in a
                        given document <F {...this.props} l="d"/>. We use for <F {...this.props} l="\text{tf}(t,d)"/>
                        the logarithmically
                        scaled term
                        count: <F {...this.props} l="\text{tf}(t,d) = 1 + \log{f_{t,d}}"/>, or <F {...this.props}
                            l="0"/> if <F {...this.props} l="f_{t,d} = 0"/>.
                    </li>
                    <li><F {...this.props} l="\text{idf}(t, D)"/> is some measure of how rare it is to find a
                        term <F {...this.props} l="t"/> in a
                        given document corpus <F {...this.props} l="D"/>. We obtain this measure
                        by calculating the logarithmically scaled inverse
                        fraction of documents in <F {...this.props} l="D"/> that contain the term <F {...this.props}
                            l="t"/>.
                        Let <F {...this.props} l="D"/> be the collection of documents, we then define
                        the standard idf measure as:
                        <F {...this.props} display="true"
                                           l="\text{idf}(t, D) = \log{\frac{|D|}{|\{d \in D:t \in d\}|}}"/>
                    </li>
                </ul>
                <p>
                    Because we
                    want to infer the most important words within
                    title elements specifically, we take as <F {...this.props} l="D"/> the collection
                    of all element types (paragraphs, titles, &#8230;),
                    and compute the tf-idf score
                    for each term with the <code>title</code> type. Some classes of words, such as articles
                    and numbers, are treated as a single term.
                </p>
                <TitleTfIdfFigure/>
                <TitleTfIdfFigurePerSection/>
                <p>
                    We observe, not very surprisingly,
                    that numbers and articles are top terms
                    for section titles.
                    Furthermore, we notice that most terms in a section
                    title are semantically related to the section role.
                </p>
            </ol>
        </div>;
    }
}
