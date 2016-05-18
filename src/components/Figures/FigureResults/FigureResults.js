import data from './data';
import React from 'react';
import _ from 'underscore';
import figs from '../figs';
import BarChart from '../../Chart/GroupedBarChart';

export default class FigureResults extends React.Component {
    render() {
        // [{
        //   title: string,
        //   data{
        //       x: any,
        //       y: any,
        //   },*
        // },*]
        var formattedData = [
            {
                title: 'F1 scores',
                data: _.map(data,(val, key) => {return {x: key, y: val.scores.SECTION_TITLE.f1}})
            },
            {
                title: 'F0.5 scores',
                data: _.map(data,(val, key) => {return {x: key, y: val.scores.SECTION_TITLE.f0_5}})
            }
                //
                // {
                //     y: data["tagger-trained-on-auto-annotated.crf"],
                //     x: 'Trained on automatically tagged corpus, without newlines'
                // },
                // {
                //     y: data["tagger-trained-on-auto-annotated.crf"].scores.SECTION_TITLE.f2,
                //     x: 'Trained on manually tagged corpus'
                // },
                // {
                //     y: data["tagger-trained-on-auto-annotated.crf"].scores.SECTION_TITLE.f2,
                //     x: 'Trained on manually tagged corpus, without newlines'
                // }
            ];
        return <figure className="chart" id={figs.taggingResults.id}>
            <h4 style={{textAlign: 'center', width: '402px', margin: 0}}>F-scores for tagging section titles</h4>
            <BarChart sourceHref={this.props.url} data={formattedData}/>
            <figcaption>
                <span className="figure-number">Fig {figs.taggingResults.num}.</span> F<sub>1</sub> scores
                and F<sub>0.5</sub> scores for different training conditions of Conditional Random Fields.
            </figcaption>
        </figure>;
    }
}