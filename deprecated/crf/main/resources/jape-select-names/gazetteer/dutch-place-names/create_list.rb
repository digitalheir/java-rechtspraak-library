require 'open-uri'
require 'json'
require 'set'

# URLs to JSON files containing Dutch place names for a number of consecutive years
URLS = [
    'http://opendata.cbs.nl/ODataApi/OData/PLAATS96/Plaatsnamen', #1996
    'http://opendata.cbs.nl/ODataApi/OData/PLAATS97/Plaatsnamen', #1997
    'http://opendata.cbs.nl/ODataApi/OData/PLAATS98/Plaatsnamen', #1998
    'http://opendata.cbs.nl/ODataApi/OData/PLAATS99/Plaatsnamen', #1999
    'http://opendata.cbs.nl/ODataApi/OData/37316/Plaatsnamen', #2000
    'http://opendata.cbs.nl/ODataApi/OData/37434PIN/Plaatsnamen', #2001
    'http://opendata.cbs.nl/ODataApi/OData/37205PIN/Plaatsnamen', #2002
    'http://opendata.cbs.nl/ODataApi/OData/70652NED/Plaatsnamen', #2003
    'http://opendata.cbs.nl/ODataApi/OData/70805NED/Plaatsnamen', #2004
    'http://opendata.cbs.nl/ODataApi/OData/70939ned/Plaatsnamen', #2005
    'http://opendata.cbs.nl/ODataApi/OData/71171NED/Plaatsnamen', #2006
    'http://opendata.cbs.nl/ODataApi/OData/71441NED/Plaatsnamen', #2007
    'http://opendata.cbs.nl/ODataApi/OData/71716NED/Plaatsnamen', #2008
    'http://opendata.cbs.nl/ODataApi/OData/80023ned/PlaatsEnGemeentenamen', #2009
    'http://opendata.cbs.nl/ODataApi/OData/80477ned/PlaatsEnGemeentenamen', #2010
]

# Collect all names. Note that we use a set to avoid duplicates
all_names = Set.new
URLS.each do |url|
  place_names = JSON.parse(open(url).read)['value'].map do |place|
    title = place['Title'].gsub(/c\.a\.$/, '').gsub(/\(.*\)?\s*$/, '').strip # Remove qualifiers like "(ged.)" or "(=Tuinhoek)"

    case (title)
      when /^(\p{Lu}\p{L})-,/ # Oost-, West- en Midden
        return title
      when /,(.*)/ # Match prefixes like "'s-" or "Den"
        prefix = title.match(/,(.*)/)[1].strip
        unless prefix[-1] == '-'
          prefix = "#{prefix} " # Add space for anything except '['s-]Gravenhage'
        end
        title = "#{prefix}#{title}"
      when /s-\s*$/ #There is at least one case where the ", 's-" postfix misses the comma 
        title = "'s-#{title}"
      else
    end
    title.gsub(/,.*|'s-\s*$/, '').strip
  end
  place_names=Set.new(place_names)
  puts "Found #{(place_names-all_names).size} additional names"
  all_names=all_names.merge place_names
end

puts '--------------------------'
puts "Found #{all_names.size} names in total"

# Write all names
all_names=all_names.to_a.sort
File.open('dutch_place_names.lst', 'w+') do |f|
  all_names.each do |name|
    f.puts name
  end
end
