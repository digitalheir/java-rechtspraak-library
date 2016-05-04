//noinspection JSUnresolvedVariable
import React, {Component} from 'react';


/**
 * A component for each individual Bar to render an SVG rect element.
 */
export default class extends Component {
  static propTypes() {
    return {
      xScale: React.PropTypes.func.isRequired,
      yScale: React.PropTypes.func.isRequired,
      height: React.PropTypes.number.isRequired,
      i: React.PropTypes.number.isRequired,
      data: React.PropTypes.shape({
        x: React.PropTypes.isRequired,
        y: React.PropTypes.isRequired
      }).isRequired
    };
  }

  render() {
    var xScale = this.props.xScale;
    var yScale = this.props.yScale;
    var height = this.props.height;
    var i = this.props.i;
    var d = this.props.data;

    //var tip = d3.tip()
    //    .attr('class', 'd3-tip')
    //    .offset([-10, 0])
    //    .html(function (d) {
    //        return "";
    //    });

    return (
      <rect
        className={"bar series-" + (i + 1)}
        x={xScale(d.x)}
        y={yScale(d.y + d.y0)}
        fill={this.props.fill}
        width={xScale.rangeBand()}
        height={height}
        data-x={"" + d.x.toString()}
        data-y={"" + d.y.toString()}
      ><title>{d.title + ", " + d.x.toString() + " (" + d.y.toString() + ")"}</title></rect>
    );
  }
}
