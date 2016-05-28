import React from 'react';
import {getSubSections} from '../getSectionComponent';
import chapters from '../../../chapters';
import ToC from '../ToC/ToC'
/**
 * A component for a chapter page on our website.
 */
export default class Chapter extends React.Component {
    render() {
        var subsections = this.props.sections.map(getSubSections(this.props, 3));

        const standaloneChapter = !this.props.inline;
        return (
            <div>
                {standaloneChapter ? <h2>Table of Contents</h2> : ''}
                {standaloneChapter ? <ToC showHome={true} {...this.props} /> : ''}

                <section className="chapter">
                    <h2 className="title"><a className="link-up" href="#"/>{this.props.title}</h2>
                    {this.props.children}
                    {subsections}
                </section>

                {standaloneChapter ? this.linkToNextChapter(this.props.title) : ''}
            </div>
        );
    }

    linkToNextChapter(title) {
        let found = false;
        for (const chapter of chapters.inOrder) {
            if (found) {
                return <p className="link-to-next-chapter">
                    Next chapter: <a href={chapters.pathTo(this.props.path,chapter)}>{chapter.title}</a>
                </p>
            }
            if (chapter.title == title) {
                found = true;
            }
        }
        return "";
    }
}
Chapter.propTypes = {
    title: React.PropTypes.string.isRequired,
    sections: React.PropTypes.arrayOf(React.PropTypes.shape({
        component: React.PropTypes.func.isRequired
    })).isRequired
};

module.exports = Chapter;
