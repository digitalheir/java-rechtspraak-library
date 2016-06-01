//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import chapters from '../../../chapters'
import {getHandler} from '../../Routes.jsx'
import Introduction from '../Introduction/Introduction';
import Tagging from '../Tagging/Tagging';
import InferringDocumentStructure from '../InferringDocumentStructure/InferringDocumentStructure';
import ToC from '../ToC/ToC'

const title = require('../../../title');

export default class FullThesis extends Component {
    render() {
        return <div>
            <section style={{}} id="title-page">
                <div style={{}} id="title-page-university-name">Utrecht University</div>
                <div style={{}} id="title-page-faculty-name">Faculty of Humanities</div>
                <div style={{}} id="title-page-msc-thesis">Master of Science Thesis</div>
                <h1 style={{}}>{title}</h1>
                <div style={{}} id="title-page-author">
                    <div style={{}} id="title-page-author-author">by</div>
                    <div style={{}} id="title-page-author-name">Maarten Trompper</div>
                </div>
                <div style={{}} id="title-page-supervisor">
                    <div style={{}} id="title-page-supervisor-supervisor">Supervisor:</div>
                    <div style={{}} id="title-page-supervisor-name">Ad Feelders</div>
                </div>
                <div style={{}} id="title-page-time-and-date">
                    <span style={{}} id="title-page-time">
                        Utrecht
                    </span>, <span style={{}} id="title-page-date">2016</span>
                </div>
            </section>
            <section id="#toc">
                <h2>Table of Contents</h2>
                <ToC singlePage={true} showHome={false} {...this.props}/>
            </section>
            {chapters.inOrder.map(chapter => {
                const ChapterContent = getHandler(chapter.route);
                return <section id={chapter.id}><ChapterContent inline={true} {...this.props}/></section>;
            })}

        </div>
    }
}
