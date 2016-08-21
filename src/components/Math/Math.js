//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import katex from 'katex';

class Math extends Component {

    render() {
        //console.log(katex.renderToString(this.props.latex,
        //    {displayMode: this.props.displayMode}));
        var latex = this.props.latex || this.props.l;
        var displayMode = this.props.displayMode || this.props.display;
        var clss = "math " + (displayMode ? "block" : "");
        var chapterNumDot = this.props.chapterObject ?
            ('<span class="math-numbering-chapter">' + this.props.chapterObject.number + '</span>' + '.')
            : '';
        var html = katex.renderToString(latex,
                {displayMode: displayMode}) + (displayMode ? '<span class="math-numbering"><span class="math-numbering-content">Eq.&nbsp;' + chapterNumDot + '</span></span>' : '');
        return (
            <span className={clss} dangerouslySetInnerHTML={
            {__html: html}
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