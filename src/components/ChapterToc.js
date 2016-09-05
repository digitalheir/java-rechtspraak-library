import ToC from './ToC/ToC'
import React, {Component} from 'react'
import PureRenderMixin from 'react-addons-pure-render-mixin';

export default React.createClass({
    mixins: [PureRenderMixin],

    componentDidMount: function () {
    },

    getInitialState: function () {
        return {closed: true};
    },

    toggleState: function () {
        this.setState({closed: !this.state.closed});
    },

    render: function () {
        const style = {
            
        };
        const closed = (this.props.clientRendered && this.state && this.state.closed);
        if (this.props.clientRendered)
            style.cursor = 'pointer';
        return <div className={("chapter-toc")+(closed?' closed':'')}>
            <h2 key="h2" style={style}
                onClick={this.toggleState} className="toc-title">Table of Contents</h2>
            <ToC key="toc" showHome={true} {...this.props} />
        </div>;
    }
});
