require 'open-uri'
require 'json'

count = 1000

rows = JSON.parse(open("http://rechtspraak.cloudant.com/ecli/_design/query_dev/_view/hoge_raad_by_date?stale=ok&limit=#{count}&reduce=false&include_docs=false").read)['rows']

rows.shuffle[0..20].each do |row|
  ecli = row['id']
  
  File.write("docs/#{ecli.gsub(':','.')}.xml", open("https://rechtspraak.cloudant.com/ecli/#{ecli}/data.xml").read)
  
end