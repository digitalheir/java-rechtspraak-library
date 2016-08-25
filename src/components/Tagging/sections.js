const taggingSections = {
    taggingIntroduction: {
        page: 12,
        id: 'tagging-introduction',
        title: 'Introduction'
    },
    taggingMethods: {
        page: 12,
        id: 'tagging-methods',
        title: 'Methods'
    },
    taggingResults: {
        page: 19,
        id: 'tagging-evaluation',
        title: 'Results'
    },
    taggingDiscussion: {
        page: 22,
        id: 'tagging-discussion',
        title: 'Discussion'
    }
};

taggingSections.inOrder = [
    taggingSections.taggingIntroduction,
    taggingSections.taggingMethods,
    taggingSections.taggingResults,
    taggingSections.taggingDiscussion,
];
export default taggingSections;
