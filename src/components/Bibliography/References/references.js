import Inl from './ParentheticalReference/ParentheticalReference';
import references from '../bib';
//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

export default {
    ref: references,
    cite: function (refId, page) {
        if (!refId) throw new Error("Reference not defined");
        return <Inl
            refId={refId}
            page={page}
        />
    }
};
