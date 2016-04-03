require 'open3'
require 'nokogiri'


module MalletHelper
  XSLT_ONLY_ADVOCAAT = Nokogiri::XSLT(File.read('only_advocaat.xslt'))
  def tokenize str
    lines = []
    o, e, s = Open3::capture3('$ALPINO_HOME/Tokenization/paragraph_per_line | $ALPINO_HOME/Tokenization/tokenize.sh', :stdin_data => str)

    if e and e.length > 0
      raise "Could not tokenize: #{puts s}\n #{e}"
    end
    o.each_line do |line|
      lines << line.split(' ')
    end
    lines
  end

  def get_capitalization_type(word)
    if word.match /^[A-Z]{2,}$/
      :allcaps
    elsif word.match /^([A-Z]\.)+$/
      :acronym
    elsif word.match /^[A-Z]+$/
      :capitalized
    elsif word.match /^[a-z]+$/
      :lowercase
    else
      :other
    end
  end

  def get_word_type(word)
    if word == ','
      :comma
    elsif word == '.'
      :period
    elsif word == ':'
      :colon
    elsif word ==';'
      :semicolon
    elsif word.match /^[^a-z]*(advocaat|gemachtigde|raads(man|vrouw)|procureur)[^a-z]*$/i
      :word_advocaat
    elsif word.match /^[^a-z]*(namens)[^a-z]*$/i
      :word_namens
    elsif word.match /^[^a-z]*(meester|mr|drs)[^a-z]*$/i
      :word_meester
    elsif word.match /^[A-Za-z-]+$/i
      :word
    elsif word.match /^[0-9]+([0-9\/a-z\.-]*[0-9a-z]+)?$/i
      :number
    else
      :other
    end
  end


  def encode_node_to_mallet_target(child)
    rows = []

    if child.text?
      text = child.to_s
    elsif child.element? and child.name == 'advocaat'
      text = child.text
    else
      raise "Could ot handle node: #{child}"
    end

    tokenize(text).each do |paragraph|
      paragraph.each_with_index do |word, i|
        if word.match /\[/
          # puts word
        end

        if word.match(/\s/)
          puts "WARNING: #{word} had whitespace"
        end
        capitalized = get_capitalization_type(word)
        word_type = get_word_type(word)

        rows << [capitalized, word_type, word]
      end
    end

    rows
  end

  def encode_to_mallet_train_inst(child)
    rows = []

    if child.text?
      text = child.to_s
    elsif child.element? and child.name == 'advocaat'
      text = child.text
    else
      raise "Could ot handle node: #{child}"
    end

    tokenize(text).each do |paragraph|
      label = :o

      paragraph.each_with_index do |word, i|
        if word.match /\[/
          # puts word
        end
        if child.element? and child.name == 'advocaat'
          if i == 0
            label = :b
          else
            label = :i
          end
        end
        if word.match(/\s/)
          puts "WARNING: #{word} had whitespace"
        end
        capitalized = get_capitalization_type(word)
        word_type = get_word_type(word)

        rows << [word, capitalized, word_type, label]
      end
    end

    rows
  end

end