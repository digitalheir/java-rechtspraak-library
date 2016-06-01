const inferringSections = {
    intro: {
        id: 'parse-tree-introduction',
        title: "Introduction",
    },
    methods: {
        id: 'parse-tree-methods',
        title: "Methods",
    },
    evaluation: {
        id: 'parse-tree-evaluation',
        title: 'Evaluation',
    }, 
    discussion: {
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
