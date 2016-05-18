export default  {
    "Deterministic tagger (baseline)": {
        "confusionMatrix": {
            "NEWLINE": {"m": {"NEWLINE": 3557}},
            "NR": {"m": {"NR": 1593, "TEXT_BLOCK": 10}},
            "SECTION_TITLE": {"m": {"SECTION_TITLE": 381, "TEXT_BLOCK": 132}},
            "TEXT_BLOCK": {"m": {"SECTION_TITLE": 18, "TEXT_BLOCK": 5417}}
        },
        "scores": {
            "NEWLINE": {
                "truePositive": 3557,
                "trueNegative": 7551,
                "falseNegative": 0,
                "falsePositive": 0,
                "precision": 1.0,
                "recall": 1.0,
                "f1": 1.0,
                "f0_5": 1.0
            },
            "NR": {
                "truePositive": 1593,
                "trueNegative": 9505,
                "falseNegative": 10,
                "falsePositive": 0,
                "precision": 1.0,
                "recall": 0.9937616968184654,
                "f1": 0.9968710888610764,
                "f0_5": 0.9987460815047023
            },
            "SECTION_TITLE": {
                "truePositive": 381,
                "trueNegative": 10577,
                "falseNegative": 132,
                "falsePositive": 18,
                "precision": 0.9548872180451128,
                "recall": 0.7426900584795322,
                "f1": 0.8355263157894737,
                "f0_5": 0.9032716927453769
            },
            "TEXT_BLOCK": {
                "truePositive": 5417,
                "trueNegative": 5531,
                "falseNegative": 18,
                "falsePositive": 142,
                "precision": 0.9744558373808239,
                "recall": 0.996688132474701,
                "f1": 0.985446607240313,
                "f0_5": 0.978822594051534
            }
        },
        "wrongs": [{
            "text": "o",
            "actual": "NR",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "maximale afmeting gehard glas",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "standpunt van de verdachte  de verdediging",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBGEL:2014:6020",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "beschikking bezwaar en geding voor het hof",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:HR:2002:AD8769",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "behoefte van de vrouw",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "productie 5bi",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "gronden van het beroep in cassatie",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHAMS:2002:AE3880",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "formele voorvragen",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBZLY:2008:BD9013",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "productie 5a i",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "proceskosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:HR:2009:BJ2011",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "de ingangsdatum",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "poging tot doodslag",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2012:BY4227",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "proceskosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:HR:2002:AD8769",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "productie 5biii",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "behoefte van de vrouw aan een bijdrage van de zijde van de man",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "in reconventie",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBDHA:2015:3551",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "kosten van de inventaris",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "beslissing de hoge raad verwerpt het beroep",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:HR:1996:AA1944",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "herstelmogelijkheden",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "geen erkenning",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vrijspraak ten aanzien van het onder 1 en 2 tenlastegelegde",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBGEL:2014:6020",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "beslag",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2012:BY4227",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "het beslag",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2007:BB2701",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "de duur van de alimentatieverplichting",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "belanghebbende",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "aanleiding van het onderzoek",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBGEL:2014:6020",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vrijspraak van het meer of anders ten laste gelegde",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBGEL:2014:4337",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "begunstiging",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2007:BB0311",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "echtscheiding of scheiding van tafel en bed",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "proceskosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:HR:2015:1200",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vonnis",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBAMS:2010:BN0717",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "proceskosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "productie 22",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "geen uitsluiting van aansprakelijkheid",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "lotsverbondenheid",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "onderzoek van de zaak",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHAMS:2014:2752",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "wijze van vervanging",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "feit 2",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBGEL:2014:4337",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "gronden van het beroep in cassatie",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHSGR:2003:AN9319",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "inkomen",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "x",
            "actual": "NR",
            "ecli": "ECLI:NL:RBAMS:2010:BN0717",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "verzoek grondslag en verweer",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2009:BK1763",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "de behoefte van de vrouw",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "ten aanzien van feit 2",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBGEL:2014:4337",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "141",
            "actual": "NR",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "de jusvergelijking",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vordering van de advocaatgeneraal",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSGR:2012:BV8404",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "primair",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBLEE:2008:BE9193",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "standpunt van de verdachte  de verdediging",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBGEL:2014:4337",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vonnis",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBGEL:2014:6020",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "benadeelde partijen",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBLEE:2008:BE9193",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "gevorderde kosten van juridisch advies",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBDHA:2015:3551",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "benadeelde partij",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBZLY:2008:BD9013",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "beschikking bezwaar en geding voor het hof",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:HR:2007:AZ7460",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "ziektekosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "ten aanzien van feit 2",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2007:BB2701",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "woonlasten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "proceskosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vordering officier van justitie",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBLEE:2008:BE9193",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vorderingen na voorwaardelijke veroordeling",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBLEE:2008:BE9193",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "producties 5aii 20 en 21",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "de kostenveroordeling",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "rechten verzekeringnemer",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2007:BB0311",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "productie 5aiii",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "nihilbeding",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "productie 20",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "artikel 21",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARN:2012:BW6258",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "aanslag bezwaar en geding voor het hof",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARN:1998:AA1313",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "onderzoeksvragen srk",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vonnis",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "slotsom met betrekking tot het testament van 4 april 2007",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBDHA:2015:3551",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "uitspraak",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBSGR:2000:AA6276",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "draagkracht",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "de procedures in beide zaken",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2011:BQ0812",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "het standpunt van de officier van justitie",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBAMS:2010:BN0717",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "in conventie",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBDHA:2015:3551",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vonnis",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBSHE:2012:BV3010",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "ten aanzien van feit 3",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2007:BB2701",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "de gemiddelde engelse consument",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "producties 5aiii 20 21 en 22",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "ten aanzien van feit 1 meer subsidiair",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBGEL:2014:4337",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "artikel 72r",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:CBB:2008:BD2958",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "144",
            "actual": "NR",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "productie 5aiv",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "conclusies van partijen",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSGR:2003:AN9319",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "artikel 13",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARN:2012:BW6258",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "1",
            "actual": "NR",
            "ecli": "ECLI:NL:GHAMS:2014:2752",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "ten aanzien van de tenlastelegging",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBZLY:2008:BD9013",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "verzoeken in het kader van artikel 1253a bw",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBSGR:2012:BY8779",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "proceskosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2007:BB0311",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "ondeugdelijk werk",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "wijziging voorlopige zorgregeling",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBSGR:2012:BY8779",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "in conventie en in reconventie",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBDHA:2015:3551",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "proceskosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHAMS:2002:AE3880",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "3 het geschil de standpunten en conclusies van partijen",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARN:1998:AA1313",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "buitengerechtelijke incassokosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBDHA:2015:3551",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "proceskosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARN:1998:AA1313",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "productie 5ai",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "feit 1 meer subsidiair",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBGEL:2014:4337",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "in de zaak 09283",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2011:BQ0812",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "glasschade",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "primair",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2012:BY4227",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "proceskosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBLEE:2010:BZ8239",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "artikel 72m",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:CBB:2008:BD2958",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "griffierecht",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "arrest",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:HR:2011:BT6827",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "oorzaak thermische breuk",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "proceskosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBDHA:2013:8514",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "2",
            "actual": "NR",
            "ecli": "ECLI:NL:GHAMS:2014:2752",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "productie 5aii en 21",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "beslissing op de vordering na voorwaardelijke veroordeling onder parketnummer",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBLEE:2008:BE9193",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "producties 20 en 22",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "ten aanzien van feit 1",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2007:BB2701",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "de inspecteur",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "proceskosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBHAA:2011:BW5844",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "productie 5bii",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "de beslissing is in het openbaar uitgesproken op",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBLIM:2015:3625",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "artikel 15",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:CBB:2008:BD2958",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "bewijsoverweging",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBLEE:2008:BE9193",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "1410",
            "actual": "NR",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vaststaande feiten  aanleiding van het onderzoek",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBGEL:2014:4337",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "proceskosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:HR:2011:BT6827",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vonnis",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "proceskosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBDHA:2015:3551",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "bewijslast",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBDHA:2015:3551",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "1",
            "actual": "NR",
            "ecli": "ECLI:NL:GHSHE:2012:BY4227",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "omschrijving geschil en standpunten van partijen",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSGR:2003:AN9319",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "geschil alsmede standpunten en conclusies van partijen",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "proceskosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSGR:2003:AN9319",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "artikel 14  garantie",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "proceskosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBSGR:2012:BY8779",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vonnis",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBMAA:2011:BQ0812",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "behoeftigheid",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vonnis",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBGEL:2014:4337",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "de redengeving van de op te leggen straffen",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2007:BB2701",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "kosten",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARN:2012:BW6258",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "arrest",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:HR:2003:AF3100",
            "predicted": "SECTION_TITLE"
        }, {"text": "vonnis", "actual": "TEXT_BLOCK", "ecli": "ECLI:NL:RBDHA:2015:3551", "predicted": "SECTION_TITLE"}]
    },
    "CRF trained on manually annotated (with newlines)": {
        "confusionMatrix": {
            "NEWLINE": {"m": {"NEWLINE": 3557}},
            "NR": {"m": {"NR": 1603}},
            "SECTION_TITLE": {"m": {"SECTION_TITLE": 468, "TEXT_BLOCK": 45}},
            "TEXT_BLOCK": {"m": {"SECTION_TITLE": 45, "TEXT_BLOCK": 5390}}
        },
        "scores": {
            "NEWLINE": {
                "truePositive": 3557,
                "trueNegative": 7551,
                "falseNegative": 0,
                "falsePositive": 0,
                "precision": 1.0,
                "recall": 1.0,
                "f1": 1.0,
                "f0_5": 1.0
            },
            "NR": {
                "truePositive": 1603,
                "trueNegative": 9505,
                "falseNegative": 0,
                "falsePositive": 0,
                "precision": 1.0,
                "recall": 1.0,
                "f1": 1.0,
                "f0_5": 1.0
            },
            "SECTION_TITLE": {
                "truePositive": 468,
                "trueNegative": 10550,
                "falseNegative": 45,
                "falsePositive": 45,
                "precision": 0.9122807017543859,
                "recall": 0.9122807017543859,
                "f1": 0.9122807017543859,
                "f0_5": 0.9122807017543858
            },
            "TEXT_BLOCK": {
                "truePositive": 5390,
                "trueNegative": 5628,
                "falseNegative": 45,
                "falsePositive": 45,
                "precision": 0.9917203311867525,
                "recall": 0.9917203311867525,
                "f1": 0.9917203311867525,
                "f0_5": 0.9917203311867525
            }
        },
        "wrongs": [{
            "text": "uitspraak",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBSGR:2000:AA6276",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "totaal 415222",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "vernietigt de bestreden uitspraak",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:HR:2007:AZ1706",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "productie 5bi",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vonnis",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBSHE:2012:BV3010",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "behoefte van de vrouw",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "zaaknummer 770098 blad 6",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBSHE:2012:BV3010",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "ten aanzien van feit 3",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2007:BB2701",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "antwoord",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBNNE:2013:BZ3452",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "gronden van het beroep in cassatie",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHAMS:2002:AE3880",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "productie 5a i",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "in conventie",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBDHA:2015:3551",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "premiebetaling",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBBRE:2007:BB0311",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "de gemiddelde engelse consument",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "weging stellingen a",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBDHA:2015:3551",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "vernietigt de aangevallen uitspraak",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:CRVB:2002:BJ3072",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "poging tot doodslag",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2012:BY4227",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "gemengd kapitaal",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBBRE:2007:BB0311",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "productie 5biii",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "behoefte van de vrouw aan een bijdrage van de zijde van de man",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "kosten van de inventaris",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "producties 5aiii 20 21 en 22",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "beslissing de hoge raad verwerpt het beroep",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:HR:1996:AA1944",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "belanghebbende",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBBRE:2007:BB0311",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "standpunt van de officier van justitie",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBAMS:2010:BN0717",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "productie 5aiv",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "artikel 13",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARN:2012:BW6258",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "de draagkracht van de man",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "mrs mclachlan",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "stokke nederland bv",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:HR:2015:1200",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "in conventie en in reconventie",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBDHA:2015:3551",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "3 het geschil de standpunten en conclusies van partijen",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARN:1998:AA1313",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "begunstiging",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2007:BB0311",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vonnis",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBAMS:2010:BN0717",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "productie 5ai",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "feit 1 meer subsidiair",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBGEL:2014:4337",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "productie 22",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "primair",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2012:BY4227",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "onderzoek van de zaak",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHAMS:2014:2752",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "arrest",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:HR:2011:BT6827",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "productie 5aii en 21",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "beslissing op de vordering na voorwaardelijke veroordeling onder parketnummer",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBLEE:2008:BE9193",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "producties 20 en 22",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "ten aanzien van feit 1",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2007:BB2701",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "parketnummer 1353301209",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBAMS:2010:BN0717",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "artikel 10d wet vpb 0",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHARN:2012:BW6258",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "in de zaak 09172",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBMAA:2011:BQ0812",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "aanslag beschikking en bezwaar",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHSGR:2003:AN9319",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "productie 5bii",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vordering van de benadeelde partij benadeelde 1",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHSHE:2012:BY4227",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "de partiële vrijspraak",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBMAA:2007:BB2701",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "de beslissing is in het openbaar uitgesproken op",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBLIM:2015:3625",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "uitspraak",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHAMS:2002:AE3880",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vonnis",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBGEL:2014:6020",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "beschikking bezwaar en geding voor het hof",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:HR:2007:AZ7460",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "als gesteld en onvoldoende weersproken staat vast",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBAMS:2009:BK6193",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "ten aanzien van feit 2",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2007:BB2701",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "stellingen a",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBDHA:2015:3551",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "geschil alsmede standpunten en conclusies van partijen",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "proceskosten",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:HR:2007:AZ7460",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "producties 5aii 20 en 21",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "ten aanzien van het onder 3 ten laste gelegde",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBAMS:2010:BN0717",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "rechten verzekeringnemer",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2007:BB0311",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "productie 5aiii",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vordering van de benadeelde partij benadeelde 2",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHSHE:2012:BY4227",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "vordering van de benadeelde partij verbalisant 1 politie",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHSHE:2012:BY4227",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "nader vast te stellen belastbaar inkomen f 123698",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "productie 20",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "bedreiging met enig misdrijf tegen het leven gericht",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBGEL:2014:4337",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "vonnis",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBGEL:2014:4337",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "de redengeving van de op te leggen straffen",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2007:BB2701",
            "predicted": "TEXT_BLOCK"
        }, {"text": "arrest", "actual": "TEXT_BLOCK", "ecli": "ECLI:NL:HR:2003:AF3100", "predicted": "SECTION_TITLE"}]
    },
    "CRF trained on manually annotated (no newlines)": {
        "confusionMatrix": {
            "NR": {"m": {"NR": 1603}},
            "SECTION_TITLE": {"m": {"SECTION_TITLE": 469, "TEXT_BLOCK": 44}},
            "TEXT_BLOCK": {"m": {"SECTION_TITLE": 47, "TEXT_BLOCK": 5388}}
        },
        "scores": {
            "NR": {
                "truePositive": 1603,
                "trueNegative": 5948,
                "falseNegative": 0,
                "falsePositive": 0,
                "precision": 1.0,
                "recall": 1.0,
                "f1": 1.0,
                "f0_5": 1.0
            },
            "SECTION_TITLE": {
                "truePositive": 469,
                "trueNegative": 6991,
                "falseNegative": 44,
                "falsePositive": 47,
                "precision": 0.9089147286821705,
                "recall": 0.9142300194931774,
                "f1": 0.91156462585034,
                "f0_5": 0.9099728366317423
            },
            "TEXT_BLOCK": {
                "truePositive": 5388,
                "trueNegative": 2072,
                "falseNegative": 47,
                "falsePositive": 44,
                "precision": 0.991899852724595,
                "recall": 0.9913523459061637,
                "f1": 0.991626023741603,
                "f0_5": 0.991790302985679
            }
        },
        "wrongs": [{
            "text": "totaal 415222",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "uitspraak",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBSGR:2000:AA6276",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "behoefte van de vrouw",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "productie 5bi",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vonnis",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBSHE:2012:BV3010",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "zaaknummer 770098 blad 6",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBSHE:2012:BV3010",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "ten aanzien van feit 3",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2007:BB2701",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "gronden van het beroep in cassatie",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHAMS:2002:AE3880",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "productie 5a i",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "in conventie",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBDHA:2015:3551",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "ten aanzien van de tenlastegelegde feiten",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBZLY:2008:BD9013",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "is in 2003 geliquideerd",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:HR:2011:BT6827",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "poging tot doodslag",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2012:BY4227",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "weging stellingen a",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBDHA:2015:3551",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "productie 5biii",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "behoefte van de vrouw aan een bijdrage van de zijde van de man",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "kosten van de inventaris",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "beslissing de hoge raad verwerpt het beroep",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:HR:1996:AA1944",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "producties 5aiii 20 21 en 22",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "standpunt van de officier van justitie",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBAMS:2010:BN0717",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "ondergetekenden",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBBRE:2007:BB0311",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "u",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBDOR:2010:BM8492",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "de draagkracht van de man",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHARL:2013:7996",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "productie 5aiv",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "vrijspraak ten aanzien van het onder 1 en 2 tenlastegelegde",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBGEL:2014:6020",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "dat verdachte het",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBGEL:2014:6020",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "belanghebbende",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "mrs mclachlan",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "ontbrekende bescheiden  70",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "c",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:HR:2011:BT6827",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "official declaration of win",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "begunstiging",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2007:BB0311",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "onnodig grievend",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHAMS:2014:2752",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "productie 5ai",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "feit 1 meer subsidiair",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBGEL:2014:4337",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "productie 22",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "primair",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSHE:2012:BY4227",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "onderzoek van de zaak",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHAMS:2014:2752",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "feit 2",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBGEL:2014:4337",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "gronden van het beroep in cassatie",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHSGR:2003:AN9319",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "arrest",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:HR:2011:BT6827",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "productie 5aii en 21",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "beslissing op de vordering na voorwaardelijke veroordeling onder parketnummer",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBLEE:2008:BE9193",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "producties 20 en 22",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "onder 1 en 2 tenlastegelegde",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBGEL:2014:6020",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "ten aanzien van feit 1",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2007:BB2701",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "artikel 10d wet vpb 0",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHARN:2012:BW6258",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "niet bewezen",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBGEL:2014:6020",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "in de zaak 09172",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBMAA:2011:BQ0812",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "aanslag beschikking en bezwaar",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHSGR:2003:AN9319",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "totaal fl 53228",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "productie 5bii",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "bewijsverweren",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHAMS:2014:2752",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "de partiële vrijspraak",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBMAA:2007:BB2701",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "de beslissing is in het openbaar uitgesproken op",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBLIM:2015:3625",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "uitspraak",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHAMS:2002:AE3880",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "beledigend karakter uitlatingen",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHAMS:2014:2752",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "direct aansluitend",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBZWB:2013:5906",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "arrest",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:HR:2005:AU3949",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "arrest",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:HR:2007:AZ1706",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "omschrijving geschil en standpunten van partijen",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:GHSGR:2003:AN9319",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "winners certificate",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "ten aanzien van het onder 1 en 2 ten laste gelegde",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBAMS:2010:BN0717",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "ten aanzien van feit 2",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2007:BB2701",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "stellingen a",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBDHA:2015:3551",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "vordering officier van justitie",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBLEE:2008:BE9193",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "proceskosten",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:HR:2007:AZ7460",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "ten aanzien van het onder 3 ten laste gelegde",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:RBAMS:2010:BN0717",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "producties 5aii 20 en 21",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "rechten verzekeringnemer",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2007:BB0311",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "productie 5aiii",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "overige kosten 214944",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHSHE:2006:BA2526",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "vordering van de benadeelde partij verbalisant 1 politie",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHSHE:2012:BY4227",
            "predicted": "SECTION_TITLE"
        }, {
            "text": "productie 20",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBBRE:2008:BD6815",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "de redengeving van de op te leggen straffen",
            "actual": "SECTION_TITLE",
            "ecli": "ECLI:NL:RBMAA:2007:BB2701",
            "predicted": "TEXT_BLOCK"
        }, {
            "text": "6 vervanging directie",
            "actual": "TEXT_BLOCK",
            "ecli": "ECLI:NL:GHARN:2012:BW6258",
            "predicted": "SECTION_TITLE"
        }, {"text": "vonnis", "actual": "TEXT_BLOCK", "ecli": "ECLI:NL:RBDHA:2015:3551", "predicted": "SECTION_TITLE"}]
    }
}