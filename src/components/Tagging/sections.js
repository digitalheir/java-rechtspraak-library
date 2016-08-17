const taggingSections = {
    taggingIntroduction: {
        page: 13,
        id: 'tagging-introduction',
        title: 'Introduction',
    },
    taggingMethods: {
        page: 13,
        id: 'tagging-methods',
        title: 'Methods',
    },
    taggingResults: {
        page: 20,
        id: 'tagging-evaluation',
        title: 'Results',
    },
    taggingDiscussion: {
        page: 21,
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
