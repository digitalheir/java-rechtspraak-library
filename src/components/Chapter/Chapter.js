import React from 'react';
import {getSubSections} from '../getSectionComponent';
import chapters from '../../../chapters';
import ChapterToc from '../ChapterToc'
import abbrs  from '../abbreviations'
/**
 * A component for a chapter page on our website.
 */
export default class Chapter extends React.Component {
    render() {
        var subsections = this.props.sections.map(getSubSections(this.props, 3));

        const standaloneChapter = !this.props.inline;
        const main = <section id={this.props.id?this.props.id:this.props.chapterObject.id}
                              itemProp="hasPart"
                              itemScope={true}
                              itemType="https://schema.org/Chapter"
                              className="chapter numbered-section reset-counter">
            <h2 itemProp="name" className="title"><a className="link-up" href="#"/>{this.props.title}</h2>
            <div>
                {this.props.children}
                {subsections}
            </div>
        </section>;
        return (
            <div>
                {standaloneChapter ? <div id="mount-point-chapter-toc"><ChapterToc {...this.props}/></div> : ''}
                {standaloneChapter ? <main>{main}</main> : main}
                {standaloneChapter ? this.linkToNextChapter(this.props.title) : ''}
            </div>
        );
    }

    linkToNextChapter(title) {
        let found = false;
        for (const chapter of chapters.inOrder) {
            if (found) {
                return <p className="link-to- next-chapter">
                    Next chapter: <a hrefLang="en" href={chapters.pathTo(this.props.path,chapter)}>{chapter.title}</a>
                </p>
            }
            if (chapter.title == title) {
                found = true;
            }
        }

        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <p className="link-to- home">
            <a hrefLang="en" href={relativeToRoot}>Back to home</a>
        </p>;
    }
}
Chapter.propTypes = {
    title: React.PropTypes.string.isRequired,
    sections: React.PropTypes.array.isRequired
};

module.exports = Chapter;
