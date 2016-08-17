const inferringSections = {
    intro: {
        page: 22,
        id: 'parse-tree-introduction',
        title: "Introduction",
    },
    methods: {
        page: 22,
        id: 'parse-tree-methods',
        title: "Methods",
    },
    evaluation: {
        page: 25,
        id: 'parse-tree-evaluation',
        title: 'Results',
    },
    discussion: {
        page: 26,
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
