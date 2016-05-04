//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import katex from 'katex';

class Math extends Component {

    render() {
        //console.log(katex.renderToString(this.props.latex,
        //    {displayMode: this.props.displayMode}));
        var latex = this.props.latex || this.props.l;
        var displayMode = this.props.displayMode || this.props.display;

        return (
            <span dangerouslySetInnerHTML={
            {
            __html:katex.renderToString(latex,
            {displayMode:displayMode})
            }
            }/>
        );
    }
}

Math.defaultProps = {
        latex: undefined,
        l: undefined,
        displayMode: false
};

export default Math;