import Inl from './ParentheticalReference/ParentheticalReference';
import references from '../bib';
//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

export default {
  ref: references,
  cite: function (refId, page) {
    return <Inl
      refId={refId}
      page={page}
    />
  }
};
