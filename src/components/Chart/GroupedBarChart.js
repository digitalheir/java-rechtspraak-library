//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import _ from 'underscore'
import d3 from 'd3'
import Bar from './Bar'
import ReactFauxDOM from 'react-faux-dom'

class GroupedBarChart extends Component {
    static getHorizontalGridlines(yScale, margin, innerWidth) {
        var g = ReactFauxDOM.createElement('g');
        d3.select(g)
            .selectAll("line.horizontalGrid")
            .data(yScale.ticks(10))
            .enter()
            .append("line")
            .attr(
                {
                    "class": "horizontalGrid",
                    "x1": margin.right,
                    "x2": innerWidth,
                    "y1"(d) {
                        return yScale(d);
                    },
                    "y2"(d) {
                        return yScale(d);
                    },
                    "fill": "none",
                    "shape-rendering": "crispEdges",
                    "stroke": "lightgray",
                    "stroke-width": "1px"
                });
        return React.createElement('g', {}, g.toReact());
    }


    static createYAxis(yScale, text) {
        var yAxis = d3.svg.axis()
            .scale(yScale)
            .orient("left")
            .tickSize(0.1);

        var yAxisEl = ReactFauxDOM.createElement("g");
        d3.select(yAxisEl)
            .attr("class", "y axis")
            .call(yAxis)
            .append("text")
            .attr("transform", "rotate(-90)")
            .attr("y", 6)
            .attr("dy", ".71em")
            .style("text-anchor", "end")
            .text(text);
        return React.createElement('g', {className: "axis"}, yAxisEl.toReact());
    }

    static createLegend(outerMargin, label, textSize, color, innerWidth) {
        var legendHeight = 12;
        var legendWidth = 20;
        var legendSpacing = 4;
        var labelMargin = {
            top: 4,
            right: 5,
            bottom: 4,
            left: 5
        };
        var labelOffsetLeft = outerMargin.right + labelMargin.left;
        var labelOffsetTop = labelMargin.top;

        var legendItems = _.map(label, (title, i) => {
                var yPos = (i * (legendHeight + legendSpacing)) + (labelOffsetTop);

                return <g
                    key={i}
                    className={"label series-"+(i+1)}>
                    <rect
                        x={innerWidth+outerMargin.left-10}
                        y={yPos}
                        fill={color(title)}
                        width={legendWidth}
                        height={legendHeight}
                        className={"series-"+(i+1)}/>
                    <text fill="#000"
                          textAnchor="end"
                          x={innerWidth+outerMargin.left-15}
                          y={yPos+textSize}
                    >{title}</text>
                </g>;
            }
        );

        // legend.append("rect")
        //     .attr("x", width - 18)
        //     .attr("width", 18)
        //     .attr("height", 18)
        //     .style("fill", color);
        //
        // legend.append("text")
        //     .attr("x", width - 24)
        //     .attr("y", 9)
        //     .attr("dy", ".35em")
        //     .text(function(d) { return d; });

        // <rect
        //
        //     x={outerMargin.right}
        //     height={((legendSpacing+legendHeight)*legendItems.length)-legendSpacing+labelMargin.top+labelMargin.bottom}
        //     fill="#FFFFFF"
        // />
        return <g
            key="legend"
            width={innerWidth-18}
            className="legend">
            {legendItems}
        </g>;
    }

    static createXAxis(xScale, innerHeight) {
        var xAxis = d3.svg.axis()
            .scale(xScale)
            .orient("bottom");

        var xAxisEl = ReactFauxDOM.createElement("g");

        d3.select(xAxisEl)
            .attr("class", "x axis")
            .attr("transform", "translate(0," + innerHeight + ")")
            .call(xAxis);
        return React.createElement('g', {className: "axis"}, xAxisEl.toReact());
    }

    render() {
        //noinspection JSUnresolvedVariable
        var size = {
            width: this.props.width,
            height: this.props.height
        };
        var margin = {
            top: 72, right: 20, bottom: 30, left: 50
        };
        var innerWidth = this.props.width - margin.left - margin.right;
        var innerHeight = this.props.height - margin.top - margin.bottom;

        var data = this.props.data;

        var titles = new Set();
        //console.log(JSON.stringify(data));
        data.forEach(d => d.data.forEach(data => titles.add(data.x)));

        var color = d3.scale.ordinal()
            .range(this.props.colorRange)
            .domain([...titles]);

        var xScale = d3.scale.ordinal()
            .rangeRoundBands([0, innerWidth], .2)
            .domain(data.map(d => d.title));

        var yScale = d3.scale.linear()
            .range([innerHeight, 0])
            .domain([0, this.props.maxY]);

        var bars = _.map(data, (d) => {
            var x1 = d3.scale.ordinal();
            x1.domain(d.data.map(da=>da.x)).rangeRoundBands(
                [0, xScale.rangeBand()]
            );
            return <g
                key={d.title}
                className="f-score-group"
                transform={ "translate(" + xScale(d.title) + ",0)"}>
                {
                    d.data.map(data => {
                        //console.log(data);
                        data.title = d.x;
                        return <Bar xScale={x1}
                                    key={'x-'+data.x+'-y'+data.y}
                                    yScale={yScale}
                                    fill={color(data.x)}
                                    height={innerHeight - yScale(data.y)}
                                    data={data}
                                    i={0}/>;
                    })
                }
            </g>
        });

        // Add horizontal grid lines
        var gridLines = GroupedBarChart.getHorizontalGridlines(yScale, margin, innerWidth);

        var ax1 = GroupedBarChart.createXAxis(xScale, innerHeight);
        var ax2 = GroupedBarChart.createYAxis(yScale, this.props.textY);

        return <svg
            className="chart"
            width={size.width}
            height={size.height}
        >
            <g transform={"translate(" + margin.left + "," + margin.top + ")"}>
                {ax1}
                {ax2}
                {gridLines}
                <g>{bars}</g>
            </g>
            {GroupedBarChart.createLegend(margin, [...titles], 10, color, innerWidth)}
            <a xlinkHref={this.props.sourceHref}>
                <text style={{textDecoration: "underline", fill: "#0000EE"}} textAnchor="start" x={12} y={20}>Data
                    source
                </text>
            </a>
        </svg>;
    }
}

GroupedBarChart.defaultProps = {
    alignLegend: 'left',
    sourceHref: undefined,
    textY: undefined,
    maxY: 1,
    width: 400,
    height: 300,
    stackLabels: undefined,
    colorRange: ["#AA0A0A","#1E405C", "#2C70A9", /*"#56a0dd", "#9DCBF1", "#6b486b", "#a05d56",*/ "#d0743c", "#ff8c00"]
};
GroupedBarChart.propTypes = {
    // Looks like
    // [
    //    {
    //      title: string,
    //      data: [ {x: any,
    //        y: any}, *]
    //    }
    //   ,*
    // ]
    data: React.PropTypes.array.isRequired
};
export default GroupedBarChart;
