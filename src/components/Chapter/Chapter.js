import React from 'react';
import ToC from '../ToC/ToC'
/**
 * A component for a chapter page on our website.
 */
export default class Chapter extends React.Component {
    render() {
        var subsections = this.props.sections.map(section => {
                var SectionContent = section.component;
                return <section key={section.id}
                    id={section.id}><h3>{section.title}</h3>
                    <SectionContent {...this.props}/>
                </section>;
            }
        );

        return (
            <section>
                <ToC {...this.props} />
                <h2>{this.props.title}</h2>
                {this.props.children}
                {subsections}
            </section>
        );
    }
}
Chapter.propTypes = {
    title: React.PropTypes.string.isRequired,
    sections: React.PropTypes.arrayOf(React.PropTypes.shape({
        component: React.PropTypes.object.isRequired
    })).isRequired
};

module.exports = Chapter;
