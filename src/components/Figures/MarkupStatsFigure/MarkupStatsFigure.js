//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import figs from '../figs';
import StackedBarChart from '../../StackedBarChart/StackedBarChart';
import data from './getData';

export default class extends Component {
    render() {
        // console.log(data)
        //   console.log(figs.markupStats);
        return <figure itemProp="hasPart" itemScope={true} itemType="https://schema.org/Dataset"
                       id={figs.markupStats.id}>
            <div itemProp="distribution" itemScope={true} itemType="https://schema.org/DataDownload">
                <meta itemProp="contentUrl" content={figs.markupStats.url}/>
            </div>
            <div className="figure-container">
                <StackedBarChart
                    data={data}
                    sourceHref={figs.markupStats.url}
                />
            </div>
            <figcaption>
                <span itemProp="alternateName" className="figure-number">Fig {figs.markupStats.num}</span>. <span
                itemProp="description">Chart
                showing the number of documents with different kinds of markup. In particular,
                we are interested in the number of <code>*.info</code> tags, which are
                headers that contain metadata about the case,
                and <code>section</code> tags, denoting sections.</span>
            </figcaption>
        </figure>;
    }
}
