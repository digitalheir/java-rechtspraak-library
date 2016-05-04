import React  from 'react'
// import Router from 'react-router'
import chapters from '../../../chapters'
// let Link = Router.Link;

class ToC extends React.Component {
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