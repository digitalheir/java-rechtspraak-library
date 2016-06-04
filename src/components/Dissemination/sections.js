const disseminationSections = {
    futureWork: {
        id: 'future-work',
        title: 'Future Work'
    },dissemination: {
        id: 'dissemination',
        title: 'Dissemination'
    },relatedWork: {
        id: 'relatedWork',
        title: 'Related Work'
    }
};

disseminationSections.inOrder = [
    disseminationSections.dissemination,
    disseminationSections.relatedWork,
    // disseminationSections.futureWork,
];
export default disseminationSections;