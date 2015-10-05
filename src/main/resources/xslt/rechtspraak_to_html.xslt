<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:rs="http://www.rechtspraak.nl/schema/rechtspraak-1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" encoding="UTF-8" indent="yes"/>
  <xsl:template match="open-rechtspraak">
    <xsl:apply-templates select="node()"/>
  </xsl:template>

  <!-- If we have no match, make a div and pass through. For instance, for:
uitspraak.info
conclusie.info
parablock
paragroup
group
alt
-->
  <xsl:template match="*">
    <xsl:call-template name="makeDivSimple"/>
  </xsl:template>

  <!--Metadata is handled elsewhere-->
  <xsl:template match="rdf:RDF"/>
  <xsl:template match="rdf:Description"/>

  <xsl:template match="rs:superscript">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">sup</xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  <xsl:template match="rs:subscript">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">sub</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="rs:uitspraak|rs:conclusie">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">article</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="rs:para">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">p</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="rs:bridgehead">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">p</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="rs:emphasis">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">em</xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  <xsl:template match="rs:section|rs:inhoudsindicatie">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">section</xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  <!-- Title given to, for example, tables and sections -->
  <xsl:template match="rs:title">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">header</xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  <xsl:template match="rs:nr|rs:imageobject|rs:videoobject">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">span</xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  <xsl:template match="rs:linebreak">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">br</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="rs:itemizedList|rs:itemizedlist">
    <xsl:if test="@mark">
      <style>
        <xsl:text>#</xsl:text>
        <xsl:value-of select="@id"/>
        <xsl:text>{</xsl:text>
        <xsl:text>list-style: none;</xsl:text>
        <xsl:text>}</xsl:text>
        <xsl:text>#</xsl:text>
        <xsl:value-of select="@id"/>
        <xsl:text>li:before{</xsl:text>
        <xsl:text>content: "</xsl:text>
        <xsl:value-of select="@mark"/>
        <xsl:text>";</xsl:text>
        <xsl:text>}</xsl:text>
      </style>
    </xsl:if>
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">ul</xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  <xsl:template match="rs:orderedlist">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">ol</xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  <!-- List items -->
  <xsl:template match="rs:listitem">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">li</xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  <!-- Videos -->
  <xsl:template match="rs:videodata">
    <xsl:element name="video">
      <xsl:attribute name="controls">controls</xsl:attribute>
      <xsl:if test="@width">
        <xsl:attribute name="width">
          <xsl:value-of select="@width"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="@height">
        <xsl:attribute name="height">
          <xsl:value-of select="@height"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:attribute name="class">
        <xsl:value-of select="@name"/>
        <xsl:if test="@align">
          <xsl:text>align-</xsl:text>
          <xsl:value-of select="@align"/>
        </xsl:if>
      </xsl:attribute>
      <xsl:if test="@scale">
        <xsl:attribute name="data-scale">
          <xsl:value-of select="@scale"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="@depth">
        <xsl:attribute name="data-depth">
          <xsl:value-of select="@depth"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="@fileref">
        <xsl:attribute name="src">
          <xsl:value-of select="concat('http://uitspraken.rechtspraak.nl/video/?id=',@fileref)"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="@format">
        <xsl:attribute name="type">
          <xsl:value-of select="@format"/>
        </xsl:attribute>
      </xsl:if>
    </xsl:element>
    <xsl:apply-templates select="node()"/>
  </xsl:template>

  <!-- Mediaobject (image or video. Although we only have 1 example of video which does not work) -->
  <xsl:template match="rs:mediaobject">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">figure</xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  <!--
<imagedata
  align="center"
  scale="100"
  fileref="51d61cad-953a-45b5-9cf9-19742dedef67"
  depth="166"
  width="250"
  height="850"
  format="image/png"/>
-->
  <xsl:template match="rs:imagedata">
    <xsl:element name="img">
      <!--<xsl:attribute name="style"><xsl:if test="@width"><xsl:value-of select="concat('width: ', @width, 'px;')"/></xsl:if><xsl:if test="@height"><xsl:value-of select="concat('height: ', @height, 'px;')"/></xsl:if><xsl:if test="@scale"><xsl:value-of select="concat('scale: ', number(@scale div 100), ';')"/></xsl:if></xsl:attribute>-->
      <xsl:attribute name="src">
        <xsl:value-of select="concat('http://uitspraken.rechtspraak.nl/image/?id=',@fileref)"/>
      </xsl:attribute>
      <xsl:if test="../alt">
        <xsl:attribute name="alt">
          <xsl:value-of select="../alt"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="../../alt">
        <xsl:attribute name="alt">
          <xsl:value-of select="../../alt"/>
        </xsl:attribute>
      </xsl:if>
      <!-- Copy attributes to data-* -->
      <xsl:for-each select="@*">
        <xsl:if test="not(starts-with(name(), 'xmlns:'))">
          <xsl:attribute name="data-{local-name()}">
            <xsl:value-of select="."/>
          </xsl:attribute>
        </xsl:if>
      </xsl:for-each>
    </xsl:element>
    <xsl:apply-templates select="node()"/>
  </xsl:template>
  <!-- Footnote reference: make an anchor whose inner HTML is the label of the actual footnote. If that label is empty, use the text 'voetnoot' -->
  <xsl:template match="rs:footnote-ref">
    <xsl:variable name="ref">
      <xsl:value-of select="@linkend"/>
    </xsl:variable>
    <xsl:variable name="label">
      <xsl:value-of select="/.//*[@id=$ref]/@label"/>
    </xsl:variable>
    <xsl:element name="a">
      <xsl:attribute name="href">
        <xsl:value-of select="@linkend"/>
      </xsl:attribute>
      <xsl:attribute name="data-tag">
        <xsl:value-of select="local-name()"/>
      </xsl:attribute>
      <xsl:choose>
        <xsl:when test="string-length(normalize-space($label))=0">
          <xsl:text>voetnoot</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$label"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>

  <!-- Footnote: start off with label -->
  <xsl:template match="rs:footnote">
    <xsl:element name="div">
      <xsl:attribute name="data-tag">
        <xsl:value-of select="local-name()"/>
      </xsl:attribute>

      <xsl:element name="a">
        <xsl:attribute name="class">footnote-label</xsl:attribute>
        <xsl:variable name="id">
          <xsl:value-of select="@id"/>
        </xsl:variable>
        <xsl:attribute name="href">
          <xsl:text>#</xsl:text>
          <xsl:value-of select="/.//*[@linkend=$id]/@id"/>
        </xsl:attribute>
        <xsl:value-of select="@label"/>
      </xsl:element>
      <xsl:apply-templates select="node()"/>
    </xsl:element>
  </xsl:template>

  <!-- Don't do anything. We handle alt in inlinemediaobject -->
  <xsl:template match="rs:alt"/>

  <xsl:template match="rs:inlinemediaobject">
    <xsl:element
            name="span"
            namespace="">
      <xsl:attribute name="data-tag">
        <xsl:value-of select="local-name()"/>
      </xsl:attribute>

      <xsl:for-each select="@*">
        <xsl:if test="not(starts-with(name(), 'xmlns:'))">
          <xsl:attribute name="data-{local-name()}">
            <xsl:value-of select="."/>
          </xsl:attribute>
        </xsl:if>
      </xsl:for-each>
      <xsl:copy-of select="@xml:space|@lang"/>

      <xsl:if test="alt">
        <!-- Alt attribute is used in imagedata -->
        <xsl:attribute name="data-alt">
          <xsl:value-of select="alt"/>
        </xsl:attribute>
      </xsl:if>

      <xsl:apply-templates select="node()"/>
    </xsl:element>
  </xsl:template>


  <!-- (informal) table -->
  <!-- May be a tgroup and a title -->
  <xsl:template match="rs:table|rs:informaltable">
    <xsl:call-template name="makeDivSimple"/>
  </xsl:template>

  <!-- tgroup: table starts here -->
  <xsl:template match="rs:tgroup">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">table</xsl:with-param>
    </xsl:call-template>
    <!-- TODO handle colspec? -->
    <!--<xsl:if test="count(colspec)>0">-->
    <!--<colgroup>-->
    <!--<xsl:for-each select="*">-->
    <!--<col>-->
    <!--</col>-->
    <!--</xsl:for-each>-->
    <!--</colgroup>-->
    <!--</xsl:if>-->
  </xsl:template>
  <!-- colspec -->
  <!--<xsl:template match="rs:colspec">-->
  <!-- NOTE: We ignore colspec here, because it's already handled in tgroup -->
  <!--</xsl:template>-->

  <!-- tbody -->
  <xsl:template match="rs:tbody">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">tbody</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <!-- thead-->
  <xsl:template match="rs:thead">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">thead</xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  <!-- tfoot -->
  <xsl:template match="rs:tfoot">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">tfoot</xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  <!-- table row -->
  <xsl:template match="rs:row">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">tr</xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  <!-- table cell -->

  <xsl:template match="rs:entry">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">td</xsl:with-param>
    </xsl:call-template>
    <!-- TODO stylesheet?-->
    <!--<xsl:if test="@morerows">-->
    <!--<xsl:attribute name="rowspan">-->
    <!--<xsl:value-of select="number(@morerows)+1"/>-->
    <!--</xsl:attribute>-->
    <!--</xsl:if>-->
    <!--<xsl:if test="@valign">-->
    <!--<xsl:attribute name="data-valign">-->
    <!--<xsl:value-of select="@valign"/>-->
    <!--</xsl:attribute>-->
    <!--</xsl:if>-->
  </xsl:template>

  <!-- Quote -->
  <xsl:template match="rs:blockquote">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">blockquote</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <!-- Foreign phrase -->
  <xsl:template match="rs:foreignphrase">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">foreignphrase</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <!-- Helper templates -->
  <xsl:template name="makeDivSimple">
    <xsl:call-template name="makeElement">
      <xsl:with-param name="element">div</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="makeElement">
    <xsl:param name="element"/>
    <xsl:element
            name="{$element}"
            namespace="">
      <xsl:attribute name="data-tag">
        <xsl:value-of select="local-name()"/>
      </xsl:attribute>

      <!--<xsl:attribute name="class">-->
      <!--<xsl:choose>-->
      <!--<xsl:when test="@role">-->
      <!--<xsl:value-of select="concat(@name, ' ', @role)"/>-->
      <!--</xsl:when>-->
      <!--<xsl:otherwise>-->
      <!--<xsl:value-of select="local-name()"/>-->
      <!--</xsl:otherwise>-->
      <!--</xsl:choose>-->
      <!--</xsl:attribute>-->

      <xsl:for-each select="@*">
        <xsl:if test="not(starts-with(name(), 'xmlns:'))">
          <xsl:attribute name="data-{local-name()}">
            <xsl:value-of select="."/>
          </xsl:attribute>
        </xsl:if>
      </xsl:for-each>
      <xsl:copy-of select="@xml:space|@lang"/>
      <xsl:apply-templates select="node()"/>
    </xsl:element>
  </xsl:template>
</xsl:stylesheet>
