require 'nokogiri'
XSLT_ONLY_ADVOCAAT = Nokogiri::XSLT(File.read('only_advocaat.xslt'))

require_relative 'mallet_helper'
include MalletHelper


rows=[]
Dir.glob('xml_annotated_attorney/*.xml').each do |path|
  xml = Nokogiri::XML File.open(path)
  xml = XSLT_ONLY_ADVOCAAT.transform(xml)

  xml.root.children.each do |child|
    rows += encode_to_mallet_train_inst(child)
  end

  # # Write rows to file
  # File.open(path.sub('xml_annotated_attorney/', 'mallet/').sub('.xml', '.txt'), 'w+') do |f|
  #   rows.each do |row|
  #     f.puts(row.join(' '))
  #   end
  # end

end
# Write rows to file
File.open('mallet/train.txt', 'w+') do |f|
  rows.each do |row|
    f.puts(row.join(' '))
  end
end
