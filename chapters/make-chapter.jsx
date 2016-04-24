const React = require('react');
const _ = require('underscore');

const Chapter = require('./chapter-component')

function getComponents(chapter){
    return _.map(chapter.sections.inOrder, function(section,i){
       const Section = require("./"+chapter.id+"/sections/"+section.id+"/"+section.id+".jsx");
       return <Section id={section.id} title={section.title} key={i} />;
    });
}

module.exports = function(data){
    return <Chapter
    title={data.title}
    description={data.description}
    >
    {getComponents(data)}
    </Chapter>;
}