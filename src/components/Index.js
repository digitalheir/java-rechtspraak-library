//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import BrowserCheck from './BrowserCheck/BrowserCheck'
import AuthorData from './Author/Author'
import AbstractContent from './Abstract/AbstractContent'
import ToC from './ToC/ToC'

export default class Index extends Component {
    render() {
        // <h1 itemProp="name">{this.props.title}</h1>
        return (
            <div>
                <BrowserCheck/>
                <header style={{marginBottom: "20px"}}>
                    <AuthorData />
                </header>
                <main>
                    <section >
                        <h2 >Abstract</h2>
                        <AbstractContent/>
                    </section>
                    <section>
                        <h2>Table of Contents</h2>
                        <ToC {...this.props}/>
                    </section>
                </main>
            </div>
        );
    }
}