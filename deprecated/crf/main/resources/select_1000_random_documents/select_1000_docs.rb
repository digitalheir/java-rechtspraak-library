# puts File.absolute_path("~/ecli-dump/")

require 'json'
require 'fileutils'
require 'open-uri'
sample_num=100
times='10'
rows = JSON.parse(open('http://rechtspraak.cloudant.com/ecli/_all_docs?stale=ok').read)['rows']
           .map {|el| el['id']}.shuffle

('01'..times).each do |i|
  puts i
  sample = rows.take(sample_num)
  rows = rows.drop(sample_num)
  sample.each do |ecli|
    folder_path="../../ecli-dump/train-corpus-#{sample_num}-#{i}/"
    if File.exist? "#{folder_path}/#{ecli.gsub(/:/, '.')}.xml"
      puts "skipping #{ecli}"
    else
      FileUtils.makedirs(folder_path)
      File.open("#{folder_path}/#{ecli.gsub(/:/, '.')}.xml", 'w+') do |f|
        f.puts open("http://rechtspraak.cloudant.com/ecli/#{ecli}/data.xml").read
      end
    end
  end
end
