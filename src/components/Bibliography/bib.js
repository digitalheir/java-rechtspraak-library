const bib = {
    bradley2010learning: {
        id: 'bradley2010learning', type: 'inproceedings',
        title: "Learning tree conditional random fields",
        author: {
            abbr: "Bradley & Guestrin",
            full: "Bradley, Joseph K and Guestrin, Carlos"
        },
        booktitle: "Proceedings of the 27th International Conference on Machine Learning (ICML-10)",
        pages: "127—134",
        year: 2010
    },
    jordan2002discriminative: {
        id: 'jordan2002discriminative',
        title: "On discriminative vs. generative classifiers: A comparison of logistic regression and naive bayes",
        author: {
            abbr: "Ng & Jordan",
            full: "Ng, Andrew and Jordan, Michael"
        },
        journal: "Advances in neural information processing systems",
        volume: "14",
        pages: "841",
        year: 2002
    },
    younger1967recognition: {
        id: 'younger1967recognition',
        title: 'Recognition and parsing of context-free languages in time n³',
        author: {
            firstName: "Daniel",
            lastName: "Younger"
        },
        journal: 'Information and control',
        volume: '10',
        number: '2',
        pages: '189—208',
        year: 1967,
        publisher: 'Elsevier',
        type: 'article'
    },

    kasami1965efficient: {
        type: 'techreport', id: 'kasami1965efficient',
        title: 'AN EFFICIENT RECOGNITION AND SYNTAX ANALYSIS ALGORITHM FOR CONTEXT-FREE LANGUAGES',
        author: {
            firstName: "Tadao",
            lastName: "Kasami"
        },
        year: '1965',
        institution: 'DTIC Document'
    },

    sipser2006introduction: {
        id: 'sipser2006introduction',
        type: 'book',
        title: 'Introduction to the Theory of Computation',
        author: {lastName: 'Sipser', firstName: 'Michael'},
        volume: '2',
        year: '2006',
        publisher: 'Thomson Course Technology Boston'
    },

    huang1971stochastic: {
        id: 'huang1971stochastic',
        type: 'article',
        title: 'On stochastic context-free languages',
        author: 'Huang, Teddy and Fu, King Sun',
        journal: 'Information Sciences',
        volume: '3',
        number: '3',
        pages: '201—224',
        year: '1971',
        publisher: 'Elsevier'
    },
    lange2009cnf: {
        id: 'lange2009cnf',
        type: 'article',
        title: 'To CNF or not to CNF? An efficient yet presentable version of the CYK algorithm',
        author: {
            abbr: "Lange and Leiß",
            full: "Lange, Martin and Leiß, Hans"
        },
        journal: 'Informatica Didactica',
        volume: '8',
        pages: '2008—2010',
        year: '2009'
    },
    cocke1969programming: {
        id: 'cocke1969programming',
        type: 'article',
        title: 'Programming languages and their compilers: Preliminary notes',
        author: {lastName: 'Cocke', firstName: 'John'},
        year: '1969',
        publisher: 'Courant Institute of Mathematical Sciences, New York University'
    },

    vanopijnen2014: {
        id: "vanopijnen2014",
        type: "article",
        title: "Op en in het web: Hoe de toegankelijkheid van rechterlijke uitspraken kan worden verbeterd",
        href: "http://dare.uva.nl/record/1/408882",
        author: {
            firstName: "Marc",
            lastName: "van Opijnen"
        },
        year: 2014
    },
    trompper2014: {
        id: "trompper2014",
        type: "web",
        title: "NL open legal data survey: Rechtspraak.nl",
        href: "http://leibniz-internship-report.herokuapp.com/eu-legal-data-survey/nl#rechtspraak.nl",
        author: {
            lastName: "Trompper"
        },
        year: 2014
    },
    mccallum2000maximum: {
        id: "mccallum2000maximum",
        title: "Maximum Entropy Markov Models for Information Extraction and Segmentation",
        author: {
            abbr: "Freitag et al.",
            full: "McCallum, Andrew and Freitag, Dayne and Pereira, Fernando"
        },
        booktitle: "ICML",
        volume: "17",
        pages: "591—598",
        year: 2000,
        href: "http://cseweb.ucsd.edu/~elkan/254spring02/gidofalvi.pdf"
    },
    mallet: {
        id: 'mallet',
        author: {
            full: "McCallum, Andrew Kachites",
            abbr: "McCallum"
        },
        title: "MALLET: A Machine Learning for Language Toolkit",
        href: "http://mallet.cs.umass.edu",
        year: 2002
    },
    sutton2006introduction: {
        id: 'sutton2006introduction',
        type: "article",
        title: "An introduction to conditional random fields for relational learning",
        author: {
            abbr: "McCallum & Sutton",
            full: "Sutton, Charles and McCallum, Andrew"
        },
        journal: "Introduction to statistical relational learning",
        pages: "93—128",
        year: 2006,
        publisher: "MIT press"
    },
    finkel2004exploiting: {
        id: 'finkel2004exploiting',
        type: "inproceedings",
        title: "Exploiting context for biomedical entity recognition: from syntax to the web",
        author: {
            full: "Finkel, Jenny and Dingare, Shipra and Nguyen, Huy and Nissim, Malvina and Manning, Christopher and Sinclair, Gail",
            abbr: "Finkel et al."
        },
        journal: "Proceedings of the International Joint Workshop on Natural Language Processing in Biomedicine and its Applications",
        pages: "88—91",
        year: 2004,
        publisher: "Association for Computational Linguistics"
    },
    klinger2009feature: {
        id: 'klinger2009feature',
        type: "inproceedings",
        title: "Feature Subset Selection in Conditional Random Fields for Named Entity Recognition",
        author: {
            full: "Klinger, Roman and Friedrich, Christoph",
            abbr: "Klinger et al."
        },
        journal: "RANLP",
        pages: "185—191",
        year: 2009
    },
    krishnan2006effective: {
        id: 'krishnan2006effective',
        type: "inproceedings",
        title: "An effective two-stage model for exploiting non-local dependencies in named entity recognition",
        author: {
            full: "Krishnan, Vijay and Manning, Christopher D",
            abbr: "Krishnan et al."
        },
        journal: "Proceedings of the 21st International Conference on Computational Linguistics and the 44th annual meeting of the Association for Computational Linguistics",
        pages: "1121-1128",
        year: 2006,
        publisher: "Association for Computational Linguistics"
    }

};
export default bib;

//html4: {
//    id: 'html4',
//    type: "web",
//    href: 'https://www.w3.org/TR/html4/',
//    title: "HTML 4.01 Specification",
//    author: {
//        abbr: "McCallum & Sutton",
//        full: "Sutton, Charles and McCallum, Andrew"
//    },
//    journal: "Introduction to statistical relational learning",
//    pages: "93—128",
//    year: 2006,
//    publisher: "MIT press"
//}