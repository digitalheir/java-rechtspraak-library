<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
  <xsl:output
          method="html" encoding="UTF-8" indent="no"/>
  <xsl:template match="/error">
  </xsl:template>
  <xsl:template match="/*[*//*[@name='title']]">
    <!-- Only proceed if there actually is a 'kop' below -->
    <ol id="navigation-root" data-element="{local-name()}">
      <xsl:apply-templates mode="entered" select="*"/>
    </ol>
  </xsl:template>

  <!-- match kop -->
  <xsl:template mode="entered" match="*">
    <xsl:choose>
      <xsl:when test="./*[@name='title']">
        <!-- There's a kop: make a 'li' element -->
        <xsl:variable name="id">
          <xsl:value-of select="*[@name='title']/@id"/>
        </xsl:variable>
        <!-- Determine class names; classnames are different if this node has children -->
        <xsl:variable name="is-sublist">
          <xsl:choose>
            <xsl:when test=".//*/*[@name='title']">
              <xsl:value-of select="1>0"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="0>1"/>
            </xsl:otherwise>
          </xsl:choose>

        </xsl:variable>
        <xsl:variable name="li-class-name">
          <xsl:choose>
            <xsl:when test="$is-sublist='true'">
              <xsl:text>sublist</xsl:text>
            </xsl:when>
            <xsl:otherwise>
              <xsl:text>item</xsl:text>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="labelling-class-names">
          <xsl:choose>
            <xsl:when test="$is-sublist='true'">
              <xsl:text>title-container labelling sublist-labelling</xsl:text>
            </xsl:when>
            <xsl:otherwise>
              <xsl:text>title-container labelling</xsl:text>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>

        <li data-element="{local-name()}" class="{$li-class-name}">
          <div class="{$labelling-class-names}">
            <xsl:choose>
              <xsl:when test="$is-sublist='true'">
                <!-- Sublist -->
                <a href="{concat('#', $id)}">
                  <xsl:call-template name="addTitleContainer">
                    <xsl:with-param name="title" select="*[@name='title']"/>
                  </xsl:call-template>
                </a>
              </xsl:when>
              <xsl:otherwise>
                <!-- Item -->
                <a href="{concat('#', $id)}">
                  <xsl:call-template name="addTitleContainer">
                    <xsl:with-param name="title" select="*[@name='title']"/>
                  </xsl:call-template>
                </a>
              </xsl:otherwise>
            </xsl:choose>
            <div class="label-link-container">
              <div class="label-link-cell">
                <a class="label-link" href="{concat('#', $id)}"/>
              </div>
            </div>
          </div>

          <!-- Proceed with children if there is another kop nested somewhere -->
          <xsl:if test="$is-sublist='true'">
            <ol>
              <xsl:apply-templates mode="entered" select="*"/>
            </ol>
          </xsl:if>
        </li>
      </xsl:when>
      <xsl:otherwise>
        <!-- Proceed to drill deeper-->
        <xsl:apply-templates mode="entered" select="*"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- Remove text -->
  <xsl:template match="text()"/>
  <xsl:template mode="entered" match="text()"/>

  <!-- Template to add title container -->
  <xsl:template name="addTitleContainer">
    <xsl:param name="title"/>

    <div class="title-container">
      <div class="label">
        <xsl:value-of select="normalize-space($title)"/>
      </div>

      <!--<xsl:if test="string-length($titel) &gt; 0">-->
        <!--<div class="title">-->
          <!--<xsl:value-of select="normalize-space($titel)"/>-->
        <!--</div>-->
      <!--</xsl:if>-->

      <!--<xsl:if test="string-length($subtitel) &gt; 0">-->
        <!--<div class="subtitle">-->
          <!--<xsl:value-of select="normalize-space($subtitel)"/>-->
        <!--</div>-->
      <!--</xsl:if>-->
    </div>
  </xsl:template>
</xsl:stylesheet>
