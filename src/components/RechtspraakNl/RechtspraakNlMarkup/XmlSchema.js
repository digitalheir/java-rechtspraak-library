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

import abbrs  from '../../abbreviations'
export default class XmlSchema extends Component {
    render() {
        return <div>
            <p>
                Regrettably, Rechtspraak.nl does not offer an {abbrs.xml} schema. This makes it more difficult to
                create programs that work with the {abbrs.xml} data,
                because we don't know exactly which elements we can expect in the {abbrs.xml} documents.

                In the absence of an official schema,
                we have created a makeshift {abbrs.xml} schema
                that was automatically generated from a random sample of
                500 documents. The resulting schema was afterwards manually
                corrected.
            </p>
            <p>
                Using this schema,
                we can utilize a technology
                called <a href="https://en.wikipedia.org/wiki/Java_Architecture_for_XML_Binding">
                JAXB</a> to automatically marshall
                and demarshall Rechtspraak.nl {abbrs.xml} documents to and from Java objects.
                Source code and schema
                are available <a className='print-url' href="https://github.com/digitalheir/java-rechtspraak-library">on
                Github.</a>
            </p></div>;
    }
}