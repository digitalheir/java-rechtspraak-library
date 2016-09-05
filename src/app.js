import React from 'react'
import ReactDOM from 'react-dom'
import ChapterToc from './components/ChapterToc'

const intialPropsElement = document.getElementById('initial-props');
if (intialPropsElement) {
    console.log(intialPropsElement.textContent);
    const intialProps = JSON.parse(intialPropsElement.textContent);
    const tocMount = document.getElementById('mount-point-chapter-toc');
    if (tocMount)  ReactDOM.render(
        <ChapterToc
            {...intialProps}
            clientRendered={true}
            singlePage={false}
            showHome={true}/>, tocMount);
}