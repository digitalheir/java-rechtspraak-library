import React  from 'react'

export default class Header extends React.Component {

    constructor(props) {
        super(props);
        //title: props.string
    }

    render() {
        var relativeToRoot = this.props.path.match(/\//g).slice(1).map(a=>"../").join("");

        return (
            <header className='py2'>
                <h1 className='mt0'>{this.props.title}</h1>
            </header>
        )
    }
}
