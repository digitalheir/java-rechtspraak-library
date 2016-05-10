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
        id: 'tagging-results',
        title: 'Results',
    },
    taggingDiscussion: {
        id: 'tagging-discussion',
        title: 'Evaluation',
    }
};

taggingSections.inOrder = [
    taggingSections.taggingIntroduction,
    taggingSections.taggingMethods,
    taggingSections.taggingResults,
    taggingSections.taggingDiscussion
];
export default taggingSections;
