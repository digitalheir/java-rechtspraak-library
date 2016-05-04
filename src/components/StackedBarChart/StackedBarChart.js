//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import _ from 'underscore'
import d3 from 'd3'
import Bar from '../Chart/Bar'
import ReactFauxDOM from 'react-faux-dom'

class StackedBarChart extends Component {
  getHorizontalGridlines(yScale, margin, innerWidth) {
    var g = ReactFauxDOM.createElement('g');
    d3.select(g).selectAll("line.horizontalGrid")
      .data(yScale.ticks(5))
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


  getSeriesFunction(color, xScale, yScale, innerHeight) {
    return function (series, i) {
      // console.log(color);
      // console.log(series);
      var groupColor = color(i);
      // console.log(groupColor);
      //console.log("series " + i + ", bars: " + series.length + ", colour: " + groupColor);
      var bars = _.map(series, function (d, barIndex) {
        return <Bar
          key={barIndex+"-"+d.x+"-"+d.y}
          xScale={xScale}
          yScale={yScale}
          fill={groupColor}
          height={innerHeight - yScale(d.y)}
          data={d}
          i={barIndex}
        />
      });

      return <g key={i} className="bar-group">
        {bars}
      </g>;
    };
  }


  createYAxis(yScale) {
    var yAxis = d3.svg.axis()
      .scale(yScale)
      .orient("left")
      .ticks(5);

    var yAxisEl = ReactFauxDOM.createElement("g");
    d3.select(yAxisEl)
      .attr("class", "y axis")
      .call(yAxis)
      .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text("Document count");
    return React.createElement('g', {className: "axis"}, yAxisEl.toReact());
  }

  createLegend(dataStack, outerMargin, stackLabels, textSize, color) {
    var legendHeight = 15;
    var legendWidth = 25;
    var legendSpacing = 4;
    var labelMargin = {
      top: 5,
      right: 5,
      bottom: 5,
      left: 5
    };
    var labelOffsetLeft = outerMargin.right + labelMargin.left;
    var labelOffsetTop = labelMargin.top;

    var legendItems = _.map(dataStack,
      function (series, i) {
        var yPos = (i * (legendHeight + legendSpacing)) + (labelOffsetTop);

        return <g
          key={i}
          className={"label series-"+(i+1)}>
          <rect
            x={labelOffsetLeft}
            y={yPos}
            fill={color(i)}
            width={legendWidth}
            height={legendHeight}
            className={"series-"+(i+1)}/>
          <text fill="#000"
                x={labelOffsetLeft+legendWidth+5}
                y={yPos+textSize}
          >{stackLabels[i]}</text>
        </g>;
      }
    );

    return <g className="legend">
      <rect
        width={115}
        x={outerMargin.right}
        height={((legendSpacing+legendHeight)*legendItems.length)-legendSpacing+labelMargin.top+labelMargin.bottom}
        fill="#FFFFFF"
      />
      {legendItems}
    </g>
  }


  createXAxis(xScale, innerHeight) {
    var xAxis = d3.svg.axis()
      .scale(xScale)
      .orient("bottom")
      .tickFormat(function (d, i) {
        if (i % 3 == 1) return d;
        else return null;
      });
    //.ticks(d3.time.year, 10)
    //.tickFormat("%Y")


    var xAxisEl = ReactFauxDOM.createElement("g");

    d3.select(xAxisEl)
      .attr("class", "x axis")
      .attr("transform", "translate(0," + innerHeight + ")")
      .call(xAxis);
    return React.createElement('g', {className: "axis"}, xAxisEl.toReact());
  }

  render() {
    var stackLabels = [];
    var preStack = [];

    //console.log(this.props.data)
    // Split in two variables for use in d3
    _.each(this.props.data, (bucket)=> {
      //console.log(bucket.data);
      preStack.push(bucket.data);
      stackLabels.push(bucket.title);
    });

    // Stacklabels like
    // [<label>]

    //console.log(stackLabels);
    //console.log(this.props.data);
    // console.log(preStack);

    var dataStack = d3.layout.stack()(preStack);
    //console.log(dataStack);
    var size = {width: this.props.width, height: this.props.height};
    var margin = {
      top: 35, right: 20, bottom: 30, left: 50
    };
    var textSize = 10;
    var innerWidth = this.props.width - margin.left - margin.right;
    var innerHeight = this.props.height - margin.top - margin.bottom;

    var xValues = _.sortBy(_.uniq(
      _.map(_.flatten(dataStack), function (point) {
        return point.x;
      })
    ), function (p) {
      return p.x;
    });


    // console.log(this.props.colorRange);
    // console.log(d3.keys(dataStack));
    var color = d3.scale.ordinal()
      .range(this.props.colorRange)
      .domain(d3.keys(dataStack));

    var xScale = d3.scale.ordinal()
      .domain(xValues)
      .rangeRoundBands([0, innerWidth], .1);

    var yVals = [];
    var yMax = 0;
    _.forEach(_.flatten(dataStack), function (e) {
      if (typeof e.y != 'number') throw new Error("Expected numbers, but got " + e.y);
      yVals[e.x] = (yVals[e.x] || 0) + e.y;
      if (yVals[e.x] > yMax)yMax = yVals[e.x];
    });
    var yScale = d3.scale.linear()
      .domain([yMax, 0])
      .range([0, innerHeight]);


    // Add series
    var getSeriesAsReactElement = this.getSeriesFunction(color, xScale, yScale, innerHeight);

    var barGroups = _.map(dataStack, getSeriesAsReactElement);

    // Add horizontal grid lines
    var gridLines = this.getHorizontalGridlines(yScale, margin, innerWidth);


    var ax1 = this.createXAxis(xScale, innerHeight);
    var ax2 = this.createYAxis(yScale);
    var legend = stackLabels ?
      this.createLegend(dataStack, margin, stackLabels, textSize, color)
      : '';

    //console.log(this.props.sourceHref);
    var sourceAnchor = <a xlinkHref={this.props.sourceHref}
    >
      <text
        style={{textDecoration: "underline", fill: "#0000EE"}}
        textAnchor="start" x={12} y={20}>Data source
      </text>
    </a>;

    //console.log("Rendered "+this.props.sourceHref);
    return <svg
      className="chart"
      width={size.width}
      height={size.height}
    >
      <g transform={"translate(" + margin.left + "," + margin.top + ")"}>
        {ax1}
        {ax2}
        {gridLines}
        {legend}
        <g>{barGroups}</g>
      </g>
      {sourceAnchor}
    </svg>;
  }
}

StackedBarChart.defaultProps = {
  alignLegend: 'left',
  sourceHref: undefined,
  width: 600,
  height: 300,
  stackLabels: undefined,
  colorRange: ["#1E405C", "#2C70A9", "#56a0dd", "#9DCBF1", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"]
};
StackedBarChart.propTypes = {
  // Looks like
  // [{
  //   title: string,
  //   data{
  //       x: any,
  //       y: any,
  //   },*
  // },*]
  data: React.PropTypes.array.isRequired
};
export default StackedBarChart;
