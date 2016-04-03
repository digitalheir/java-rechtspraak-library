require 'fileutils'
require 'nokogiri'

i=0
sample_num=900
roots = []

File.open("D:/ecli-dump/train_corpus_#{sample_num}.xml", 'w+') do |f|
  f.puts '<xml>'
  Dir.glob('D:/ecli-dump/train-corpus/*.xml').sample(sample_num).each do |path|
    doc = Nokogiri::XML File.open(path, 'r')
    root = doc.at_xpath '/open-rechtspraak/rs:conclusie|/open-rechtspraak/rs:uitspraak', {:rs => 'http://www.rechtspraak.nl/schema/rechtspraak-1.0'}
    root.name = 'DOC'
    f.puts root.to_s

    i+=1
    if i%100==0
      puts i
    end
  end
  f.puts '</xml>'
end
