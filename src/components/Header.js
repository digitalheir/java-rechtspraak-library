import React  from 'react'
import abbrs  from './abbreviations'

export default class Header extends React.Component {

    constructor(props) {
        super(props);
        //title: props.string
    }

    render() {
        var relativeToRoot = this.props.path.match(/\//g).slice(1).map(a=>"../").join("");

        return (
            <header
                className='py2'>
                <h1 itemProp="name" className='mt0'>{this.props.title}</h1>
            </header>
        )
    }
}
