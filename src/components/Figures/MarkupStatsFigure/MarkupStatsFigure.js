//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import figs from '../figs';
import StackedBarChart from '../../StackedBarChart/StackedBarChart';
import data from './getData';

export default class extends Component {
    render() {
        // console.log(data)
        //   console.log(figs.markupStats);
        return <figure id={figs.markupStats.id}>
            <div className="figure-container">
                <StackedBarChart
                    data={data}
                    sourceHref={figs.markupStats.url}
                />
            </div>
            <figcaption>
                <span className="figure-number">Fig {figs.markupStats.num}.</span> Chart
                showing the number documents with different kinds of markup. In particular,
                we are interested in the number of <code>*.info</code> tags, which are
                headers that contain metadata about the case,
                and <code>section</code> tags, denoting sections.
            </figcaption>
        </figure>;
    }
}
