var chapters = {
    introduction: {
        title: "Introduction",
        route: '/introduction/'
    },
    rechtspraakNl: {
        title: "Rechtspraak.nl Data Set",
        route: '/rechtspraak-nl/'
    },
    tagging: {
        title: "Tagging Elements",
        route: '/tagging/'
    },
    documentStructure: {
        title: "Inferring a Document Structure",
        route: '/document-structure/'
    }//,
    // presentation: {
    //     title: "Dissemination",
    //     route: '/dissemination/'
    // }
};

chapters.inOrder = [
    chapters.introduction,
    chapters.rechtspraakNl,
    chapters.tagging,
    chapters.documentStructure
];

module.exports = chapters;