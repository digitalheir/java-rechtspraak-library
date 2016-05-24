//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Chapter from '../Chapter/Chapter'
import Introduction from '../Introduction/Introduction';

export default class FullThesis extends Component {
    render() {
        const intro = <Introduction {...props}/>;
        return <div>
            {intro.props.children}
        </div>
    }
}
