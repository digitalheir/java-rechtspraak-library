words = [
    {
        singular: 'advocaat-generaal',
        multiple: 'advocaten-generaal'
    },
    {
        singular: 'advocaat',
        multiple: 'advocaten'
    },
    {
        singular: 'adv.',
        multiple: 'advctn.'
    },
    {
        singular: 'ambtenaar van Staat',
        multiple: 'ambtenaren van Staat'
    },
    {
        singular: 'ambtenaar van de provincie',
        multiple: 'ambtenaren van de provincie'
    },
    {
        singular: 'ambtenaar van de gemeente',
        multiple: 'ambtenaren van de gemeente'
    },
    {
        singular: 'ambtenaar',
        multiple: 'ambtenaren'
    },
    {
        singular: 'raadsman',
        multiple: 'raadsmannen'
    },
    {
        singular: 'raadman',
        multiple: 'raadmannen'
    },
    {
        singular: 'raadsheer',
        multiple: 'raadsheren'
    },
    {
        singular: 'raad',
        multiple: 'raden'
    },
    {
        singular: 'raadsvrouw',
        multiple: 'raadsvrouwen'
    },
    {
        singular: 'raadvrouw',
        multiple: 'raadvrouwen'
    },
    {
        singular: 'griffier',
        multiple: 'griffiers'
    },
    {
        singular: 'procureur',
        multiple: 'procureurs'
    },
    {
        singular: 'voorzitter',
        multiple: 'voorzitters'
    },
    {
        singular: 'lid van de enkelvoudige kamer',
        multiple: 'leden van de enkelvoudige kamer'
    },
    {
        singular: 'lid van de meervoudige kamer',
        multiple: 'leden van de meervoudige kamer'
    },
    {
        singular: 'lid',
        multiple: 'leden'
    },
    {
        singular: 'officier van justitie',
        multiple: 'officieren van justitie'
    },
    {
        singular: 'rechter',
        multiple: 'rechters'
    },
    {
        singular: 'rechter-commissaris',
        multiple: 'rechter-commissarissen'
    },
    {
        singular: 'gemachtigde',
        multiple: 'gemachtigden'
    },
]

File.write('roles_single.lst', words.map { |word| word[:singular] }.sort.join("\n"))
File.write('roles_multiple.lst', words.map { |word| word[:multiple] }.sort.join("\n"))
