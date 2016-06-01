//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Source from '../../Source/Source'
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';

export default class AdditionalEnrichment extends Component {
    render() {
        return <div>
            <p>
                We have developed a pipeline
                that transforms a Dutch case law document from
                a typically sparsely marked up documents to one
                with a reasonably precise section hierarchy.
            </p>
            <p>
                Neither the process of tagging the document tokens,
                nor that of creating a section hierarchy
                has been extensively optimized, so we can likely
                still improve on the F<sub>1</sub> scores we
                report: 0.92 for tagging and 0.91 for parsing.
            </p>


            - publish enriched dataset


            - publish source code

        </div>;
    }
}
