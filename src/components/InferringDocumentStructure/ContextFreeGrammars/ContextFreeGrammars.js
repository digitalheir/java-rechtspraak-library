//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import F from '../../Math/Math';
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';
import FigRef from './../../Figures/FigRef'
import figs from './../../Figures/figs'
import listings from  '../../Figures/listings';
import ListingRef from  '../../Figures/ListingRef';
import abbrs from  '../../abbreviations';

export default class ContextFreeGrammars extends Component {
    render() {
        return <div>
            <p>Context-Free Grammars ({abbrs.cfgs}) are grammars where each rule is of the form</p>
            <F {...this.props} l="A \rightarrow \alpha" display="true"/>
            <p>
                where <F {...this.props} l="A"/> is a single non-terminal symbol and <F {...this.props} l="\alpha"/> is
                any string
                of terminals and non-terminals, including the empty string <F {...this.props} l="\epsilon"/>.
            </p>
            <p>
                A Probabilistic Context-Free Grammar ({abbrs.pcfg}) is then a Context-Free Grammar in which each rule
                has a probability assigned to it.
                A derivation
                of a sequence with a {abbrs.pcfg} has a probability score attached to it, which is the product of
                the probabilities of all of the applied rules.
            </p>
            <p>
                In our discussions, we assume probability scores to be
                real numbers between <F {...this.props} l="0"/> and <F {...this.props} l="1"/>, with the common
                operations
                of multiplication and addition, but in implementation we use
                the <a itemScope={true}
                       itemType="https://schema.org/TechArticle"
                       hrefLang="en"
                       href="http://www.johndcook.com/blog/2014/02/26/log-semiring/">
                <span itemScope={true}
                      itemType="https://schema.org/Intangible"
                      itemProp="about">
                    <span itemProp="name">Log semiring</span>
                </span>
            </a> to avoid <a hrefLang="en" href="https://en.wikipedia.org/wiki/Arithmetic_underflow">
                  <span itemScope={true}
                        itemType="https://schema.org/Intangible">
                      <span itemProp="name">
                      arithmetic underflow
                  </span></span>
            </a>.
            </p>
            <p>
                {abbrs.cfgs} are said to be in Chomsky Normal Form ({abbrs.cnf}) if all rules are of the following form:
            </p>

            <F {...this.props} l="A\rightarrow B C" display="true"/>
            <F {...this.props} l="A\rightarrow t" display="true"/>

            <p>Where <F {...this.props} l="A"/>, <F {...this.props} l="B"/> and <F {...this.props} l="C"/> are
                non-terminal types, and <F {...this.props} l="t"/> is a
                terminal type.
                In the following, we use an extension of CNF with unary rules. In this extension, <F {...this.props}
                    l="t"/> is either
                a terminal or non-terminal type.
            </p>

            <p>
                A lot of work has been done in parsing (P){abbrs.cfgs} in applications
                of natural language processing and parsing programming languages. More recently, {abbrs.pcfgs} have
                been used for other applications such as
                modeling RNA structures, as in {ref.cite(bib.sakakibara1994stochastic)}.
            </p>

            <p>
                <ListingRef listing={listings.figGrammar}/> shows a simplified version of the grammar
                that we use to create the section hierarchy.
            </p>

            <figure id={listings.figGrammar.id}>
                <table className="grammar">
                    <tbody>
                    <tr>
                        <th colSpan="2">Terminal rules</th>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{Text} \rightarrow \text{text}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{Text} \rightarrow \text{newline}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{Numbering} \rightarrow \text{numbering}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{TitleText} \rightarrow \text{section-title}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    <tr>
                        <td colSpan="2"/>
                    </tr>
                    <tr>
                        <th colSpan="2">Non-terminal rules</th>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{Document} \rightarrow \text{Header DocumentContent}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{Document} \rightarrow \text{DocumentContent}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    <tr>
                        <td colSpan="2"/>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{DocumentContent} \rightarrow \text{Sections}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{DocumentContent} \rightarrow \text{Text Sections}"/></td>
                        <td><F {...this.props} l="0.8"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{DocumentContent} \rightarrow \text{Sections Text}"/></td>
                        <td><F {...this.props} l="0.8"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{DocumentContent} \rightarrow \text{Text Sections Text}"/></td>
                        <td><F {...this.props} l="0.8"/></td>
                    </tr>
                    <tr>
                        <td colSpan="2"/>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{Text} \rightarrow \text{Text Text}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    <tr>
                        <td colSpan="2"/>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{Sections} \rightarrow \text{Sections Sections}"/></td>
                        <td><F
                            l="0.4 + \begin{cases}0.6&\text{if numberings in sequence}\\0&\text{otherwise}\end{cases}"/>
                        </td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{Sections} \rightarrow \text{Section}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{Sections} \rightarrow \text{Section Text}"/></td>
                        <td><F {...this.props} l="0.9"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{Sections} \rightarrow \text{Text Section}"/></td>
                        <td><F {...this.props} l="0.8"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{Section} \rightarrow \text{SectionTitle SectionContent}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    <tr>
                        <td colSpan="2"/>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{SectionTitle} \rightarrow \text{Numbering}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{SectionTitle} \rightarrow \text{TitleText}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{SectionTitle} \rightarrow \text{Numbering TitleText}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{SectionContent} \rightarrow \text{Text}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props} l="\text{SectionContent} \rightarrow \text{Sections}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    <tr>
                        <td><F {...this.props}
                            l="\text{SectionContent} \rightarrow \text{SectionContent SectionContent}"/></td>
                        <td><F {...this.props} l="1.0"/></td>
                    </tr>
                    </tbody>
                </table>
                <figcaption>
                    <span className={listings.figGrammar.id}>Listing {listings.figGrammar.num}.</span> Simplified
                    grammar for creating section hierarchy in {abbrs.cnf} with unary rules.
                </figcaption>
            </figure>
        </div>;
    }
}
