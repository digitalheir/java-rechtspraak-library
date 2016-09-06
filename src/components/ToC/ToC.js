import React  from 'react'
// import Router from 'react-router'
import chapters from '../../../chapters'
// let Link = Router.Link;
import {getHandler} from '../../Routes.jsx'
import getSectionComponent from '../getSectionComponent'

class ToC extends React.Component {
    static getSubSections(chapter, urlSection, depth, singlePage) {
        if (chapter.getSections && chapter.getSections()) {
            // console.log(chapter.getSections());
            // itemProp={"pageStart":''}
            return <ol key="subsections">
                {chapter.getSections().inOrder.map(section => {
                    //console.log(JSON.stringify(section));
                    if (!!section) return <li
                        key={section.id}
                        itemScope={true}
                        itemProp="hasPart"
                        itemType="https://schema.org/CreativeWork">
                                <span key="row" className="row">
                                    <a key="title" hrefLang="en" itemProp="url" href={urlSection+"#"+section.id}>
                                        <span key="title" itemProp="name">{section.title}</span>
                                    </a>
                                    {singlePage ? <span key="nr" className="nr">{section.page}</span> : ''}
                                    </span>
                        {ToC.getSubSections(getSectionComponent(section.id), urlSection, depth + 1, singlePage)}
                    </li>;
                    else throw Error("Null section found in " + JSON.stringify(chapter.getSections()));
                })}
            </ol>
        } else {
            return "";
        }
    }

    render() {
        let relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        const path = this.props.path;


        var props = this.props;
        return <div className={'chapter toc'}>
            {this.props.showHome ? <li key="home"><a hrefLang="en" href={relativeToRoot}>Home</a></li> : ""}
            <ol key="list" className={this.props.singlePage?"leaders":""}>
                
                {
                    chapters.inOrder.map((chapter) => {
                            let urlSection;

                            if (!this.props.singlePage)  urlSection = relativeToRoot + chapter.route.replace('/', '');
                            else  urlSection = "#" + chapter.id;

                            return <li itemProp="hasPart"
                                       itemScope={true}
                                       itemType="https://schema.org/Chapter"
                                       key={chapter.route}>
                                <span key="row" className="row">
                                <span key="span">
                                {path == chapter.route
                                    ? <strong key="title" itemProp="name">{chapter.title}</strong>
                                    : <a key="title" hrefLang="en" itemProp="mainEntityOfPage url" href={urlSection}
                                         className='nav-link'><span key='title'
                                                                    itemProp="name">{chapter.title}</span></a>}
                                    </span>
                                    {this.props.singlePage ?
                                        <span key='pagenr' itemProp="pageStart"
                                              className="nr">{chapter.page}</span> : ''}
                                    </span>
                                {ToC.getSubSections(getHandler(chapter.route), props.singlePage ? '' : urlSection, 1, this.props.singlePage)}
                            </li>
                        }
                    )
                }
            </ol>
        </div>;
    }

}
ToC.propTypes = {
    path: React.PropTypes.string.isRequired
};
export default ToC;