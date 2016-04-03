require_relative 'mallet_helper'
include MalletHelper

Dir.glob('xml/*.xml').sample(10).each do |path|
  rows = []
  ecli = path.match(/^xml(.*)\.xml$/)[1]
  xml = Nokogiri::XML File.open(path)
  xml = XSLT_ONLY_ADVOCAAT.transform(xml)

  xml.root.children.each do |child|
    rows += encode_node_to_mallet_target(child)
  end

  File.open("mallet/test/#{ecli}.txt", 'w+') do |f|
    rows.each do |row|
      f.puts row.join(' ')
    end
  end
end