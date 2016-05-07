import React  from 'react'
// import Router from 'react-router'
import chapters from '../../../chapters'
// let Link = Router.Link;
import {getHandler} from '../../Routes.jsx'

class ToC extends React.Component {
    static getSubSections(chapter) {
        if (chapter.getSections && chapter.getSections()) {
            // console.log(chapter.getSections());
            return <ol>
                {chapter.getSections().inOrder.map(section => {
                    if (!!section) return <li>{section.title}</li>;
                    else throw Error("Null section found in " + JSON.stringify(chapter.getSections()
                        ))
                })}
            </ol>
        } else {
            return "";
        }
    }

    render() {
        var relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <ol className='mxn2'>
            {
                chapters.inOrder.map(chapter =>
                    <li key={chapter.route}>
                        {this.props.path == chapter.route
                            ? <span>{chapter.title}</span>
                            : <a href={relativeToRoot+chapter.route.replace('/','')}
                                 className='nav-link'>{chapter.title}</a>}
                        {ToC.getSubSections(getHandler(chapter.route))}
                    </li>
                )
            }
        </ol>;
    }

}
ToC.propTypes = {
    path: React.PropTypes.string.isRequired
};
export default ToC;