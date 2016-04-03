<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
  <xsl:output omit-xml-declaration="yes" indent="yes"/>
  <xsl:strip-space elements="*"/>

  <xsl:template match="/open-rechtspraak/uitspraak//text()|/open-rechtspraak/conclusie//text()"><xsl:value-of select="."/></xsl:template>
</xsl:stylesheet>