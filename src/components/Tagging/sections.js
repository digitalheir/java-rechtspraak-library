const taggingSections = {
    taggingIntroduction: {
        id: 'tagging-introduction',
        title: 'Introduction',
    }, 
    taggingMethods: {
        id: 'tagging-methods',
        title: 'Methods',
    }, 
    taggingResults: {
        id: 'tagging-evaluation',
        title: 'Evaluation',
    }
};

taggingSections.inOrder = [
    taggingSections.taggingIntroduction,
    taggingSections.taggingMethods,
    taggingSections.taggingResults,
];
export default taggingSections;
