import React  from 'react'

export default class PercentageBar extends React.Component {
    render() {
        var txt = this.props.text ? this.props.text : this.props.percentage;
        return (
            <svg width="80" height="10">
                <rect className="perc-bar bg" width="100%" height="100%">
                    <title>{txt}</title>
                </rect>
                <rect className="perc-bar" width={this.props.percentage} height="100%">
                    <title>{txt}</title>
                </rect>
            </svg>
        );
    }
}   
PercentageBar.propTypes = {
    percentage: React.PropTypes.string.isRequired,
    text: React.PropTypes.string.isRequired
};