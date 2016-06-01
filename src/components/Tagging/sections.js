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
    },
    taggingDiscussion: {
        id: 'tagging-discussion',
        title: 'Discussion',
    }
};

taggingSections.inOrder = [
    taggingSections.taggingIntroduction,
    taggingSections.taggingMethods,
    taggingSections.taggingResults,
    taggingSections.taggingDiscussion,
];
export default taggingSections;
