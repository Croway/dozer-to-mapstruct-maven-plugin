<xsl:stylesheet version="2.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" omit-xml-declaration="yes" />
	<xsl:template match="mappings">
package mapstruct;
		<xsl:for-each select="mapping">
			<xsl:variable name="name">
				<xsl:call-template name="concat-qualified-name">
					<xsl:with-param name="a" select="class-a"/>
					<xsl:with-param name="mid" select="''"/>
					<xsl:with-param name="b" select="class-b"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:variable name="methodName">
				<xsl:call-template name="concat-qualified-name">
					<xsl:with-param name="a" select="class-a"/>
					<xsl:with-param name="mid" select="'To'"/>
					<xsl:with-param name="b" select="class-b"/>
				</xsl:call-template>
			</xsl:variable>

@org.mapstruct.Mapper
interface <xsl:value-of select="$name"/>Mapper {
	<xsl:value-of select="$name"/>Mapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(<xsl:value-of select="$name"/>Mapper.class);

	<xsl:for-each select="field">@org.mapstruct.Mapping(source = "<xsl:value-of select="a"/>"
		, target = "<xsl:value-of select="b"/>"
		<xsl:if test="@map-id">, qualifiedByName = "<xsl:value-of select="@map-id"/>"</xsl:if>
		<xsl:if test="@date-format">, dateFormat = "<xsl:value-of select="@date-format"/>"</xsl:if>)
	</xsl:for-each>
	<xsl:for-each select="field-exclude">@org.mapstruct.Mapping(source = "<xsl:value-of select="a"/>", target = "<xsl:value-of select="b"/>", ignore = true)
	</xsl:for-each>

	<xsl:value-of select="class-b"/><xsl:value-of select="' '"/><xsl:value-of select="$methodName"/>(<xsl:value-of select="class-a"/> from);
}
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="concat-qualified-name">
		<xsl:param name="a" />
		<xsl:param name="mid" required="no"/>
		<xsl:param name="b" />

		<xsl:call-template name="substring-after-last">
			<xsl:with-param name="string" select="$a"/>
			<xsl:with-param name="delimiter" select="'.'"/>
		</xsl:call-template>
		<xsl:value-of select="$mid"/>
		<xsl:call-template name="substring-after-last">
			<xsl:with-param name="string" select="$b"/>
			<xsl:with-param name="delimiter" select="'.'"/>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="substring-after-last">
		<xsl:param name="string" />
		<xsl:param name="delimiter" />
		<xsl:choose>
			<xsl:when test="contains($string, $delimiter)">
				<xsl:call-template name="substring-after-last">
					<xsl:with-param name="string"
							select="substring-after($string, $delimiter)" />
					<xsl:with-param name="delimiter" select="$delimiter" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise><xsl:value-of select="$string" /></xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>