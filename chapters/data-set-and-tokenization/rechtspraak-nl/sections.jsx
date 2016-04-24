const sections = {
    intro: {
        id: 'introduction',
        title: "Introduction"
    },
    //metadata: {
    //    id: 'metadata',
    //    title: "Metadata"
    //},
    markup: {
        id: 'markup',
        title: "Markup"
    }
};

sections.inOrder = [
    sections.intro,
    //sections.metadata,
    sections.markup
];

module.exports = sections;