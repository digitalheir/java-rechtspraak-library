import React  from 'react'
import abbrs  from './abbreviations'

export default class Header extends React.Component {

    constructor(props) {
        super(props);
        //title: props.string
    }

    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(a=>"../").join("");
        const attrs = {
            id: 'thesis',
            itemScope: true,
            itemType: "https://schema.org/Thesis"
        };
        if (this.props.path.match(/\//)) attrs.itemProp = "mainEntity";
        
        const title = relativeToRoot.length>0?<a hrefLang="en" href={relativeToRoot}>{this.props.title}</a>:this.props.title;
        return React.createElement(
            "header",
            attrs,
            <h1 key="title" itemProp="name" className='mt0'>
                {title}
            </h1>,
            <div key="audience" itemProp="audience" itemScope={true} itemType="http://schema.org/EducationalAudience">
                <meta content="student" itemProp="audienceType"/>
                <meta content="researcher" itemProp="audienceType"/>
                <meta content="computer scientist" itemProp="audienceType"/>
                <meta content="legal researcher" itemProp="audienceType"/>
            </div>
        );
    }
}
