var rd3 = require('react-d3');
var StackedBarChart = require('./../charts/StackedBarChart');
var Source = require('./../source');
var React = require('react');
var d3 = require('d3');
var _ = require('underscore');
var http = require('http');

var ReactFauxDOM = require('react-faux-dom');

var ref = require('../citation/references.jsx');
var refs = ref.ref;

var o = {
    getMarkupChart: function (cb) {
        this.getMarkupStats(
            function (obj, err) {
                if (err) {
                    cb(null, err);
                } else {
                    var margin = {
                            top: 20, right: 20, bottom: 30, left: 40
                        },
                        width = 600 - +margin.left - +margin.right,
                        height = 500 - margin.top - margin.bottom;

                    var x = d3.time.scale()
                        .range([0, width], .1);

                    var y = d3.scale.linear()
                        .range([height, 0]);

                    //downloadJson(markupStatsUrl, cb);
                    var xAxis = d3.svg.axis()
                        .scale(x)
                        .orient("bottom");
                    //.tickFormat("%Y")
                    //.ticks(d3.time.year, 10);

                    var yAxis = d3.svg.axis()
                        .scale(y)
                        .orient("left")
                        .ticks(5);

                    // Set SVG box
                    var el = ReactFauxDOM.createElement('svg');
                    // Change stuff using actual DOM functions.
                    // Even perform CSS selections.
                    el.style.setProperty('border', 'solid black 1px');
                    el.setAttribute('class', 'box');

                    var svg = d3.select(el)
                        .attr("width", width + margin.left + margin.right)
                        .attr("height", height + margin.top + margin.bottom)
                        .append("g")
                        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");


                    var data = getMarkupStatsBarData(o.rows)[1];

                    x.domain(d3.extent(data, function (d) {
                        return (d.x);
                    }));
                    y.domain([0, d3.max(data, function (d) {
                        return d.y;
                    })]);

                    svg.append("g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0," + height + ")")
                        .call(xAxis);

                    svg.append("g")
                        .attr("class", "y axis")
                        .call(yAxis)
                        .append("text")
                        .attr("transform", "rotate(-90)")
                        .attr("y", 6)
                        .attr("dy", ".71em")
                        .style("text-anchor", "end")
                        .text("Count");

                    svg.selectAll(".bar")
                        .data(data)
                        .enter().append("rect")
                        .attr("class", "bar")
                        .attr("data-year", function (d) {
                            return d.x + "";
                        })
                        .attr("x", function (d) {
                            return x((d.x));
                        })
                        .attr("data-count", function (d) {
                            console.log(d.y);
                            return d.y + "";
                        })
                        .attr("width", x.rangeBand())
                        .attr("y", function (d) {
                            return y(d.y);
                        })
                        .attr("height", function (d) {
                            return height - y(d.y);
                        });

                    // Render it to React elements.


                    cb(chart, null);
                }
            }
        );
    }
};

var PureRenderMixin = require('react-addons-pure-render-mixin');

var Chapter = require('../chapter');
var SectionMarkup = require('./sections/markup/section-markup.jsx');
var SectionMetadata = require('./sections/metadata/section-metadata.jsx');
var SectionCouch = require('./section-couch.jsx');

function getYearFromRow(row) {
    return row.key[1];
}
function getValueFromRow(row) {
    return row.value;
}




var RechtspraakComponent = React.createClass({
    mixins: [PureRenderMixin],

    getDefaultProps: function () {
        return {}
    },
    render: function () {




        return (
            <Chapter
                title={this.props.title}
                description="Description of Rechtspraak.nl data set describing Dutch case law">

                

                <SectionMarkup/>
                <SectionMetadata/>
                <SectionCouch/>

            </Chapter>
        );
    }
});

module.exports = {
    title: title,
    content: <RechtspraakComponent/>
};