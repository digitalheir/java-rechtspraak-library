<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:rs="http://www.rechtspraak.nl/schema/rechtspraak-1.0"
        >
  <xsl:output method="text" encoding="UTF-8" omit-xml-declaration="yes" indent="no"/>

  <xsl:template match="/open-rechtspraak/rs:uitspraak//text()">
    <xsl:value-of select="."/>
  </xsl:template>

  <xsl:template match="/open-rechtspraak/rs:conclusie//text()">
    <xsl:value-of select="."/>
  </xsl:template>
  <xsl:template match="text()"/>
</xsl:stylesheet>