const data = require('./get-word-count-data');
data.data.splice(15);
const React = require('react');
const _ = require('underscore');
const PureRenderMixin = require('react-addons-pure-render-mixin');
const figs = require('../../../../../figures/figs.jsx');
const PercentageBar = require('../figure-relative-title-count-for-terms/PercentageBar.jsx');
//const LineChart = require('../../../../../charts/bar-chart/BarChart.jsx');

var WordCountFigure = React.createClass({
    mixins: [PureRenderMixin],

    getDefaultProps: function () {
        return {}
    },

    render: function () {
        //<LineChart labelY='²log frequency' sourceHref={data.url} data={data.data}/>
        return <figure className="chart"  id={figs.tfidf.id}>
            <table className=" table">
                <thead>
                <tr>
                    <th>Word #</th>
                    <th>Relative frequency</th>
                    <th>Absolute frequency</th>
                </tr>
                </thead>
                <tbody>
                {_.map(data.data, function (obj) {
                    var percentageTxt = (100*obj.titleCount/data.totalCount).toFixed(2)+"%";
                    return <tr key={obj.wordCount}>
                        <th className="nr">{obj.wordCount}</th>
                        <td>
                            <PercentageBar percentage={percentageTxt} text={percentageTxt}/>
                            <span className="perc-text">{percentageTxt}</span>
                        </td>
                        <td><span className='perc-text'>{obj.titleCount}</span></td>
                    </tr>;
                    })
                    }
                </tbody>
            </table>
            <figcaption>
                <span className="figure-number">Fig {figs.tfidf.num}.</span> Word count in
                titles.
            </figcaption>
        </figure>;
    }
});

module.exports = WordCountFigure;