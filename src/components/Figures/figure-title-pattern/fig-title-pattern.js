import data from './raw-data';
import React from 'react';
import figs from '../figs';
import TreeMap from '../../Chart/TreeMap/TreeMap';

export default class FigureTitlePattern extends React.Component {
    render() {
        return <figure id={figs.figTitleTreemap.id}>
            <TreeMap data={data.data} sourceHref={data.href}/>
            <figcaption>
                <span className="figure-number">Fig {figs.figTitleTreemap.num}.</span> Absolute frequency of title
                patterns
                that occur more than 5 times in the corpus, for all types of
                section that Rechtspraak.nl divides documents in.
            </figcaption>
        </figure>;
    }
}