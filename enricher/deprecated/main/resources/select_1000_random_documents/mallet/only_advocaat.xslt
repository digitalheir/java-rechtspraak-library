<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rs="http://www.rechtspraak.nl/schema/rechtspraak-1.0"
                version="1.0"
        >
  <xsl:output method="xml" encoding="UTF-8" omit-xml-declaration="yes" indent="no"/>

  <xsl:template match="/">
    <xml>
      <xsl:for-each select="/open-rechtspraak/rs:uitspraak|/open-rechtspraak/rs:conclusie">
        <xsl:apply-templates/>
      </xsl:for-each>
    </xml>
  </xsl:template>

  <!--<xsl:template match="text()"/>-->

  <xsl:template match="rs:advocaat">
    <advocaat>
      <xsl:apply-templates/>
    </advocaat>
  </xsl:template>

</xsl:stylesheet>