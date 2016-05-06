import d3 from 'd3'
import _ from 'underscore'
import ReactFauxDOM from 'react-faux-dom'
import React from 'react';

var OTHER = '_OTHER';

var createLegend = function (labels, textSize, color) {
    var legendHeight = 15;
    var legendWidth = 25;
    var legendSpacing = 4;
    var labelMargin = {
        top: 5,
        right: 5,
        bottom: 5,
        left: 5
    };
    var labelOffsetLeft = labelMargin.left;
    var labelOffsetTop = labelMargin.top;

    var legendItems = _.map(labels,
        function (label, i) {
            var labelEl = '';

            var labelText = label;
            if (label == 'null') {
                labelText = "No role";
            }

            if (labelText.match(/beslissing|overwegingen|procesverloop/)) {
                labelEl = <span style={{
                    display:'table-cell',
                    fontSize:'x-small'
                    }}
                                className={"cell"}
                >{labelText}</span>;
            } else {
                labelEl = <span style={{
                    display:'table-cell',
                    fontSize:'x-small'
                    }}
                                className={"cell"}
                >{labelText}</span>;
            }
            var yPos = (i * (legendHeight + legendSpacing)) + (labelOffsetTop);
            //console.log(label, i);
            return <div
                key={i}
                style={{display:"table-row"}}
                className={"label series-"+(labelText)}>
                <div style={{
                    display:'table-cell'
                }}
                     className={"cell series-"+(i+1)}
                >
                    <div style={{
                    width: "26px",
                    height: "14px",
                    display:'block',
                    backgroundColor:color(label),
                    margin: "1px 5px"
                }}></div>
                </div>
                {labelEl}
            </div>;
        }
    );

    var style = {
        //width:"115px",
        float: "right",
        marginBottom: "6px",
        display: "table",
        height: ((legendSpacing + legendHeight) * legendItems.length) - legendSpacing + labelMargin.top + labelMargin.bottom + "px"
        //position:  "absolute",
        //top:0,
        //right:0
    };
    return <div
        style={style} className="legend">
        {legendItems}
    </div>;
};

export default class TreeMap extends React.Component {

    render() {
        var root = this.props.data;

        var outerWidth = this.props.width ? this.props.width : 600;
        var outerHeight = this.props.height ? this.props.height : 400;

        var color = d3.scale.ordinal().range(this.props.colorRange);
        //var color = d3.scale.category20c();

        var el = ReactFauxDOM.createElement('div');

        var treemap = d3.layout.treemap()
            .size([outerWidth, outerHeight])
            .sticky(true)
            .value(function (d) {
                return d.size;
            });

        var div = d3.select(el);


        function position() {
            this.style("left", function (d) {
                    return d.x + "px";
                })
                .style("top", function (d) {
                    return d.y + "px";
                })
                .style("width", function (d) {
                    return Math.max(0, d.dx - 1) + "px";
                })
                .style("min-width", function (d) {
                    return Math.max(0, d.dx - 1) + "px";
                })
                .style("height", function (d) {
                    return Math.max(0, d.dy - 1) + "px";
                })
                .style("min-height", function (d) {
                    return Math.max(0, d.dy - 1) + "px";
                });
        }

        var node = div.datum(root).selectAll(".node")
            .data(treemap.nodes)
            .enter().append("div")
            .attr("class", "node")
            .call(position)
            .style("background", function (d) {
                return d.children ? color(d.name) : null;
            });
        node.append("span")
            .attr("class", "label-pattern")
            .text(function (d) {
                return d.children ? null : d.name;
            });
        node.append("span")
            .attr("class", "label-pattern-count")
            .text(function (d) {
                return d.children ? null : d.size;
            });

        var labels = _.map(root.children, function (el) {
            return el.name;
        });
        var legend = createLegend(labels, 15, color);

        div
            .style("position", "relative")
            .style("display", "inline-block")
            .style("width", outerWidth)
            .style("height", outerHeight);
        //SVGReact.props.children = this.props.children;
        return (<div
                className="chart tree-map">
                {legend}
                <a style={{    display: "block",fontSize: 'x-small'}} href={this.props.sourceHref}>Data
                    source</a>
                {el.toReact()}
            </div>
        );
    }
}
TreeMap.defaultProps = {
    colorRange: [
        "#1E405C",
        "#FFB4B4",
        "#2C70A9",
        "#56a0dd",
        "#9DCBF1",
        "#a05d56",
        "#d0743c",
        "#ff8c00"]
};
TreeMap.propTypes = {
    margin: React.PropTypes.shape({
        top: React.PropTypes.number,
        bottom: React.PropTypes.number,
        left: React.PropTypes.number,
        right: React.PropTypes.number
    }),
    data: React.PropTypes.shape({
        name: React.PropTypes.string,
        children: React.PropTypes.arrayOf(React.PropTypes.object)
    }).isRequired,
    width: React.PropTypes.number,
    height: React.PropTypes.number,
    sourceHref: React.PropTypes.string.isRequired
};