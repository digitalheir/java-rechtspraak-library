//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import chapter from '../../../../chapters';
import abbrs from '../../abbreviations';

export default class TaggingIntroduction extends Component {


    render() {
        var relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");
        const urlToIntrochapter = relativeToRoot + chapter.introduction.route.replace('/', '');
        return <div>
            <p>
                In the <a hrefLang="en" href={urlToIntrochapter}>previous chapter</a>, we developed
                a way to import
                Rechtspraak.nl {abbrs.xml} documents and distill them into
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
                Even as a human reader, it can be hard to distinguish what
                should properly be called a section, and so what is a section heading.
                This means that there is some subjectivity involved in tagging.
                Consider, for example,
                a numbered enumeration of facts which might either be
                considered a list or a section sequence.

                For our purposes, we call a 'section' any semantic grouping of text that is headed by a title or
                a number, inspired by the {abbrs.html}5 definition of <code>section</code>:
            </p>

            <blockquote cite="https://www.w3.org/TR/html5/sections.html#the-section-element">
                A section is a thematic grouping of content.
                The theme of each section should be identified, typically by including
                a heading (h1-h6 element) as a child of the section element.
            </blockquote>

            <p>
                Labeling a string of tokens is a task that has been widely covered in literature,
                mostly in the application of part-of-speech tagging in natural language.
                Popular methods include graphical models, which model the probability distributions
                of labels and observations occurring together. These include Hidden
                Markov Models ({abbrs.hmms}) and the closely related Conditional Random Fields ({abbrs.crfs}).
            </p>
            <p>
                In this chapter, we experiment
                with {abbrs.crfs} for
                labeling the tokens, and we compare the results
                to a hand-written deterministic tagger that utilizes features 
                that are largely the same as those used by the {abbrs.crf} models.
                It turns out that both approaches score around 1.0 on all labels
                except section titles.

                For section titles, {abbrs.crfs} significantly
                outperform the hand-written tagger
                in terms of recall, while trading in some precision. For section titles,
                the hand-written tagger
                has a precision of 0.96 and recall of 0.74; the trained {abbrs.crfs} of 
                0.91 and 0.91, respectively.
            </p>
        </div>;
    }
}
