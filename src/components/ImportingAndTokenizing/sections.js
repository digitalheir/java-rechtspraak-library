const importingSections = {
    rechtspraakNl: {
        id: 'rechtspraak-nl',
        title: "Rechtspraak.nl"
    },
    rechtspraakNlMarkup: {
        id: 'rechtspraak-nl-data-set',
        title: "Rechtspraak.nl Data Set"
    },
    importing: {
        id: 'importing',
        title: 'Importing'
    },
    tokenizing: {
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