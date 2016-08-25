const taggingSections = {
    // intro: {
    //     id: 'introduction',
    //     title: "Introduction",
    //     component: Introduction
    // },
    // methods: {
    //     id: 'methods',
    //     title: "Methods",
    //     component: Methods
    // },
    crf: {
        page: '13',
        id: 'crf',
        title: "Conditional Random Fields",
    },
    featureSelection: {
        page: '12',
        id: 'features',
        title: 'Features',
    },
    deterministic: {
        page: 'x',
        id: 'deterministic-tagger',
        title: 'Deterministic Tagger',
    },
    manual: {
        page: 'x',
        id: 'manual-tagger',
        title: 'Manual Tagger',
    }
};

taggingSections.inOrder = [
    taggingSections.featureSelection,
    taggingSections.crf,
    //taggingSections.deterministic,
    //taggingSections.manual
];
export default taggingSections;
