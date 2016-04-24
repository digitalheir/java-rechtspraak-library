"use strict";

const React = require('react');
const Header = require('../header');
const Footer = require('../footer.jsx');
const AuthorData = require('../author-data.jsx');
const BrowserCheck = require('../browser-check.jsx');
const Bibliography = require('../citation/bibliography-section.jsx');
const urls = require('../urls');

/**
 * A component for a chapter page on our website.
 */
const ChapterComponent = React.createClass({
    getDefaultProps: function () {
        return {
            title: null,
            description: null,
            relativePath: urls.relativePath
        }
    },

    //
//"Description of the Rechtspraak.nl open data set and how we use it."

    render: function () {
        return (
            <html lang="en" className="no-js">

            <Header
                relativePath={this.props.relativePath}
                title={this.props.title}
                description={this.props.description}
            />

            <body>

            <div className="wrapper">
                <article>
                    <header style={{marginBottom: "20px"}}>
                        <BrowserCheck/>
                        <nav className="top-nav"
                        ><a href={this.props.relativePath}>Home</a></nav>

                        <h1>{this.props.title}</h1>

                        <AuthorData />
                    </header>
                    {this.props.children}

                    <Bibliography/>
                </article>

                <Footer/>
            </div>
            <script src="https://cdn.rawgit.com/google/code-prettify/master/loader/run_prettify.js"
                    async={true}></script>
            </body>
            </html>
        );
    }
});

module.exports = ChapterComponent;