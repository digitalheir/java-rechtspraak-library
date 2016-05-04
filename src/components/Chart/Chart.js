//noinspection JSUnresolvedVariable
import React, {Component} from 'react';



import './Chart.scss'

/**
 * Chart component to render a container svg element with a given size.
 */
export default class extends Component {
    render() {
        var el = ReactFauxDOM.createElement('svg');
        var margin = this.props.margin;
        var size = this.props.size;

        var svg = d3.select(el)
            .attr("width", size.width + margin.left + margin.right)
            .attr("height", size.height + margin.top + margin.bottom)
            .append("g");

        var xAxis = d3.svg.axis()
            .scale(this.props.xScale)
            .orient("bottom");
        //.tickFormat("%Y")
        //.ticks(d3.time.year, 10);

        var yAxis = d3.svg.axis()
            .scale(this.props.yScale)
            .orient("left")
            .ticks(5);

        //SVGReact.props.children = this.props.children;
        return (
            <svg
                width={size.width}
                height={size.height}>
                {this.props.children}
            </svg>
        );
    }
}
