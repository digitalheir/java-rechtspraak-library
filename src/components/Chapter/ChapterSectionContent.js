import React from 'react';
import getHandler from '../getSectionComponent';
import chapters from '../../../chapters';
import ToC from '../ToC/ToC'
/**
 * A component for a chapter page on our website.
 */
export default class Chapter extends React.Component {
    render() {
        // var subsections = this.props.sections.map(section => {
        //         var SectionContent = getHandler(section.id);
        //         return <section key={section.id}
        //                         id={section.id}>
        //             <h4 className="title">
        //                 <a hrefLang="en" className="link-up" href="#"/>
        //                 {section.title}
        //             </h4>
        //             <SectionContent {...this.props}/>
        //         </section>;
        //     }
        // );

        return (
            <div>
                {this.props.children}
                {/*subsections*/}
            </div>
        );
    }
}
Chapter.propTypes = {
    title: React.PropTypes.string.isRequired,
    sections: React.PropTypes.arrayOf(React.PropTypes.shape({
        component: React.PropTypes.func.isRequired
    })).isRequired
};

module.exports = Chapter;
