const inferringSections = {
    intro: {
        page: 23,
        id: 'parse-tree-introduction',
        title: "Introduction",
    },
    methods: {
        page: 23,
        id: 'parse-tree-methods',
        title: "Methods",
    },
    evaluation: {
        page: 26,
        id: 'parse-tree-evaluation',
        title: 'Results',
    },
    discussion: {
        page: 27,
        id: 'parse-discussion',
        title: 'Discussion',
    }
};

inferringSections.inOrder = [
    inferringSections.intro,
    inferringSections.methods,
    inferringSections.evaluation,
    inferringSections.discussion,
];
export default inferringSections;
