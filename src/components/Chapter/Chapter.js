import React from 'react';
import getHandler from '../getSectionComponent';
import chapters from '../../../chapters';
import ToC from '../ToC/ToC'
/**
 * A component for a chapter page on our website.
 */
export default class Chapter extends React.Component {
    render() {
        var subsections = this.props.sections.map(section => {
                var SectionContent = getHandler(section.id);
                return <section key={section.id}
                                id={section.id}>
                    <h3 className="title">
                        <a className="link-up" href="#"/>
                        {section.title}
                    </h3>
                    <SectionContent {...this.props}/>
                </section>;
            }
        );

        return (
            <div>
                <h2>Table of Contents</h2>
                <ToC showHome={true} {...this.props} />

                <section>
                    <h2 className="title"><a className="link-up" href="#"/>{this.props.title}</h2>
                    {this.props.children}
                    {subsections}
                </section>

                {this.linkToNextChapter(this.props.title)}
            </div>
        );
    }

    linkToNextChapter(title) {
        let found = false;
        for (const chapter of chapters.inOrder) {
            if (found) {
                return <p class="link-to-next-chapter">
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
