const sections = {
    text_patterns: {
        id: 'feature-selection',
        title: "Feature selection"
    },
    introduction: {
        id: 'introduction',
        title: "Introduction"
    },
    results: {
        id: 'results',
        title: "Results"
    }
};

sections.inOrder = [
    sections.introduction,
    sections.text_patterns,
    sections.results
];

module.exports = sections;