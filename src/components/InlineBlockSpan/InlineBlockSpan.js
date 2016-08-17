//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

class InlineBlockSpan extends Component {

    render() {
        return (
            <span style={
            {
            display:'inline-block'
            }
            }>{this.props.children}</span>
        );
    }
}

export default InlineBlockSpan;