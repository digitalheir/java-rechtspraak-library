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

export default class Introduction extends Component {
    render() {
        const nonValidatingXml = "https://validator.w3.org/nu/?useragent=Validator.nu%2FLV+http%3A%2F%2Fvalidator.w3.org%2Fservices&amp;doc=http%3A%2F%2Fuitspraken.rechtspraak.nl%2Finziendocument%3Fid%3DECLI%3ANL%3ACBB%3A2010%3ABN1294";

        const htmlManifestation = "http://uitspraken.rechtspraak.nl/inziendocument?id=ECLI:NL:CBB:2010:BN1294";
        const xmlManifestation = "http://data.rechtspraak.nl/uitspraken/content?id=ECLI:NL:CBB:2010:BN1294";

        const urlSchemeText = "http://data.rechtspraak.nl/uitspraken/content?id={ecli}";
        const urlSchemeUrl = "http://deeplink.rechtspraak.nl/uitspraak?id=ECLI:NL:CBB:2010:BN1294";

        var relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div>
            <p>
                We want to automatically annotate documents in the corpus with some semantic mark
                up, so it is helpful to see what is already done in this regard
                by <a href="http://www.rechtspraak.nl/">Rechtspraak.nl</a>.
                As we have seen, in recent years documents are more richly marked up than older
                documents. Indeed: most older documents consist exclusively
                of <code>para</code> and <code>paragroup</code> tags, denoting paragraphs
                and groups of paragraphs respectively.
            </p>
            <p>
                We observe that a richly marked up case law document
                typically consists of the following parts:
            </p>
            <section id="info">
                <h3><code>*.info</code></h3>
                <p>
                    The first element in a document is typically a unique header element with
                    a tagname of either <code>uitspraak.info</code> or <code>conclusie.info</code> for two types
                    of case law (judgments and conclusions, respectively). We refer to
                    either of these as <code>*.info</code>. <code>*.info</code> elements contain
                    interesting metadata such as names an court location. The information is generally
                    not semantically marked up, but is reasonably easy to parse thanks
                    to style consistencies in authors (e.g., most units of metadata are on a separate line).
                </p>

                <strike>
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
                        a header section. [TODO figref] suggests that analyzing tf-idf in <code>*.info</code> elements
                        does not seem to be a particularly useful method of generating features
                        that select for these metadata items. But it
                        is easy for the human eye to recognize some recurring patterns.
                    </p>
                    <FigureInfoText/>
                    <p>See, e.g., <cite><a
                        href="https://rechtspraak.lawreader.nl/ecli/ECLI:NL:GHARL:2014:9139">ECLI:NL:GHARL:2014:9139</a>
                    </cite> for an example.</p>
                </strike>

                <p>
                    Automatically marking up text portions with <code>*.info</code> tags is outside of the scope of
                    this thesis, although it can be achieved by simply extending the label set
                    to include a <code>*.info</code> tag.
                </p>
                <p>
                    A <code>*.info</code> element is followed by any number of sections.
                </p>
            </section>
            <section>
                <h3><code>section</code></h3>
                <p>
                    <code>section</code> tags generally
                    contain a title element, and optionally contain an attribute which denotes the section role.
                    A <code>section</code> is a generic grouping of running text, and can be nested
                    to create a section hierarchy.
                </p>
                <p>
                    In practice<Source
                    href="https://rechtspraak.cloudant.com/docs/_design/stats/_view/section_roles?group_level=1"/> we
                    see three values for the <code>role</code> attribute, of either
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
                    Many sections have no role, although one may imagine other roles than the above, such
                    as <code>feiten</code> (facts). Assigning roles to sections
                    is an interesting avenue of research, but we do not explore this in this thesis. Instead,
                    we limit ourselves to demarcating sections and assigning some hierarchical section structure.
                </p>
            </section>
            <section>
                <h3><code>title</code></h3>
                <p>
                    <code>title</code> elements typically occur as the
                    first descendant of
                    a <code>section</code> element, and contain either a
                    numbering in a <code>nr</code> node, or some text, or both.
                </p>
                <p>
                    We assume that <code>title</code> elements consist of an optional
                    numbering, followed by a
                    handful of words (see <FigRef fig={figs.figTitleWordCount}/>).
                </p>
                <WordCountFig/>

                <p>Titles have a number of patterns that often recur. See <FigRef
                    fig={figs.figTitleTreemap}/> for a tree map for the occurrence
                    title texts, and <FigRef fig={figs.tfidf}/> for a chart of the terms with the
                    highest tf-idf scores.  
                </p>

                <FigTitlePattern/>
                <TitleTfIdfFigure/>
                <TitleTfIdfFigurePerSection/>
            </section>

            <section id="xml-schema">
                <h4>XML Schema</h4>
                <p>
                    Sadly, Rechtspraak.nl does not offer an XML schema. This makes it a little more difficult to
                    create
                    programs that work with the XML data, such as a converter to
                    HTML. This is because we don't know exactly which elements we can expect in the
                    XML documents.

                    In the absence of an official schema,
                    we have created a makeshift XML schema,
                    that was automatically generated from a random sample of
                    500 documents, and then manually corrected.
                </p>
                <p>
                    Using this schema,
                    we can utilize a technology
                    called <a href="https://en.wikipedia.org/wiki/Java_Architecture_for_XML_Binding">
                    JAXB</a> to automatically marshall
                    and demarshall Rechtspraak.nl XML documents to and from Java objects.
                    (Source code and schema
                    available <a href="https://github.com/digitalheir/java-rechtspraak-library">on Github.</a>)
                </p>
            </section>
        </div>;
    }
}
