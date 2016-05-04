/**
 * React Static Boilerplate
 * https://github.com/koistya/react-static-boilerplate
 * Copyright (c) Konstantin Tarkus (@koistya) | MIT license
 */

import React, { Component } from 'react';
import BrowserCheck from '../components/BrowserCheck/BrowserCheck'
import AuthorData from '../components/Author/Author'
import Bibliography from '../components/Bibliography/Bibliography'

export default class extends Component {

  render() {
    return (
      <article
        itemProp="mainEntity"
        itemScope={true}
        itemType="http://schema.org/Thesis">
        <header style={{marginBottom: "20px"}}>
          <BrowserCheck/>
          <nav className="top-nav"
          ><a href={this.props.relativePath}>Home</a></nav>

          <h1 itemProp="name">{this.props.title}</h1>

          <AuthorData />
        </header>
        <Bibliography/>
      </article>
    );
  }
        // {this.props.children}

}
