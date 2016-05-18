import React  from 'react'
// import Router from 'react-router'
import chapters from '../../../chapters'
// let Link = Router.Link;
import {getHandler} from '../../Routes.jsx'
import getSectionComponent from '../getSectionComponent'

class ToC extends React.Component {
    static getSubSections(chapter, urlSection, depth) {
        if (chapter.getSections && chapter.getSections()) {
            // console.log(chapter.getSections());
            return <ol>
                {chapter.getSections().inOrder.map(section => {
                    if (!!section) return <li>
                        <a href={urlSection+"#"+section.id}>{section.title}</a>
                        {ToC.getSubSections(getSectionComponent(section.id), urlSection, depth + 1)}
                    </li>;
                    else throw Error("Null section found in " + JSON.stringify(chapter.getSections()
                        ))
                })}
            </ol>
        } else {
            return "";
        }
    }

    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");
        const path = this.props.path;


        return <nav className='toc'>
            <ol>
                {this.props.showHome ? <a href={relativeToRoot}>Home</a> : ""}
                {
                    chapters.inOrder.map((chapter) => {
                            const urlSection = relativeToRoot + chapter.route.replace('/', '');

                            return <li key={chapter.route}>
                                {path == chapter.route
                                    ? <strong>{chapter.title}</strong>
                                    : <a href={urlSection}
                                         className='nav-link'>{chapter.title}</a>}
                                {ToC.getSubSections(getHandler(chapter.route), urlSection, 1)}
                            </li>
                        }
                    )
                }
            </ol>
        </nav>;
    }

}
ToC.propTypes = {
    path: React.PropTypes.string.isRequired
};
export default ToC;