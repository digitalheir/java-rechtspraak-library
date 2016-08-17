const importingSections = {
    rechtspraakNl: {
        page: '6',
        id: 'rechtspraak-nl',
        title: "Rechtspraak.nl"
    },
    rechtspraakNlMarkup: {
        page: '6',
        id: 'rechtspraak-nl-data-set',
        title: "Rechtspraak.nl Data Set"
    },
    importing: {
        page: '11',
        id: 'importing',
        title: 'Importing'
    },
    tokenizing: {
        page: '11',
        id: 'tokenizing',
        title: 'Tokenizing'
    }
};

importingSections.inOrder = [
    // importingSections.rechtspraakNl,
    importingSections.rechtspraakNlMarkup,
    importingSections.importing,
    importingSections.tokenizing,
];

export default importingSections;