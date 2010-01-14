<?xml version='1.0' encoding='utf-8' ?>
<!--

    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.

-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:param name="baseActionURL">render.userLayoutRootNode.uP</xsl:param>
  <xsl:param name="locale">sv_SE</xsl:param>
  <xsl:variable name="mediaPath">media/org/jasig/portal/channels/CUserPreferences</xsl:variable>

  <xsl:template match="profile">
        <form name="form1" method="post" action="{$baseActionURL}">
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td colspan="4" class="uportal-channel-table-header" valign="top">Ändra profil</td>
            </tr>

            <tr>
              <td colspan="4" class="uportal-background-dark" valign="top">
                <img alt="" src="{$mediaPath}/transparent.gif" width="1" height="1" />
              </td>
            </tr>

            <tr>
              <td colspan="4" class="uportal-channel-subtitle" valign="top">
                <img alt="" src="{$mediaPath}/transparent.gif" width="10" height="10" />
              </td>
            </tr>

            <tr>
              <td colspan="4" class="uportal-label" valign="top">Profilens namn:</td>
            </tr>

            <tr>
              <td colspan="4" class="uportal-channel-subtitle" valign="top">
                <input type="text" name="profileName" class="uportal-input-text" size="20" value="{name}" />
              </td>
            </tr>

            <tr>
              <td colspan="4" class="uportal-channel-subtitle" valign="top">
                <img alt="" src="{$mediaPath}/transparent.gif" width="10" height="10" />
              </td>
            </tr>

            <tr>
              <td colspan="4" class="uportal-label" valign="top">Profilbeskrivning:</td>
            </tr>

            <tr>
              <td colspan="4" class="uportal-channel-subtitle" valign="top">
                <textarea name="profileDescription" class="uportal-input-text" cols="50" rows="2">
                  <xsl:value-of select="description" />
                </textarea>
              </td>
            </tr>

            <tr>
              <td colspan="4" class="uportal-channel-subtitle" valign="top">
                <img alt="" src="{$mediaPath}/transparent.gif" width="10" height="10" />
              </td>
            </tr>

            <xsl:apply-templates select="themestylesheets" />

            <tr>
              <td class="uportal-text-small" colspan="4">
                <img alt="" src="{$mediaPath}/transparent.gif" width="10" height="10" />
              </td>
            </tr>

            <tr>
              <td class="uportal-text-small" colspan="4">
		<input type="hidden" name="action" value="completeEdit"/>
                <input type="submit" name="submitSave" value="Save Changes" class="uportal-button" />

                <img alt="" src="{$mediaPath}/transparent.gif" width="10" height="10" />

                <input type="submit" name="submitCancel" value="Avbryt" class="uportal-button" />
              </td>
            </tr>
          </table>
        </form>
  </xsl:template>

  <xsl:template match="themestylesheets">
    <tr>
      <td class="uportal-label" colspan="4" valign="top">Tema:</td>
    </tr>

    <tr>
      <td class="uportal-background-dark" colspan="4" valign="top">
        <img alt="" src="{$mediaPath}/transparent.gif" width="1" height="1" />
      </td>
    </tr>

    <tr>
      <td colspan="4" valign="top">
        <img alt="" src="{$mediaPath}/transparent.gif" width="10" height="10" />
      </td>
    </tr>

    <tr>
      <td colspan="4" valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="5">
          <tr valign="top">
            <td align="center" class="uportal-channel-subtitle">Välj</td>

            <td align="center" class="uportal-channel-subtitle">Typ av webbläsare</td>

            <td width="100%" class="uportal-channel-subtitle">
              <div align="center">Namn/Beskrivning</div>
            </td>

            <td class="uportal-channel-subtitle" align="center">
              <div align="center">Exempel</div>
            </td>
          </tr>

          <xsl:apply-templates select="current" />

          <xsl:apply-templates select="alternate" />
        </table>
      </td>
    </tr>

    <tr>
      <td class="uportal-background-med" colspan="4">
        <img alt="" src="{$mediaPath}/transparent.gif" width="1" height="1" />
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="current">
    <tr valign="top">
      <td align="center" class="uportal-text-small" valign="middle">
        <input type="radio" name="stylesheetID" value="{id}" checked="checked" />
      </td>

      <td align="center" class="uportal-text-small">
        <img alt="device icon" src="{deviceiconuri}" width="120" height="90" />

        <br />

        <xsl:value-of select="mimetype" />
      </td>

      <td width="100%" class="uportal-channel-text" valign="top">
        <p>
          <strong>
            <xsl:value-of select="name" />
          </strong>

          <br />

          <xsl:value-of select="description" />
        </p>
      </td>

      <xsl:choose>
        <xsl:when test="sampleiconuri =''">
          <td align="center" valign="middle" class="uportal-text-small">Inget exempel
          <br />

          Tillgängligt</td>
        </xsl:when>

        <xsl:when test="sampleuri = ''">
          <td align="center" class="uportal-text-small">
            <img alt="sample icon" src="{sampleiconuri}" width="120" height="90" border="0" />
          </td>
        </xsl:when>

        <xsl:otherwise>
          <td align="center" class="uportal-text-small">
          <a href="{sampleuri}" target="_blank">
            <img alt="sample icon" src="{sampleiconuri}" width="120" height="90" border="0" />
          </a>

          <br />

          Klicka för att förstora</td>
        </xsl:otherwise>
      </xsl:choose>
    </tr>
  </xsl:template>

  <xsl:template match="alternate">
    <tr valign="top">
      <td align="center" class="uportal-text-small" valign="middle">
        <input type="radio" name="stylesheetID" value="{id}" />
      </td>

      <td align="center" class="uportal-text-small">
        <img alt="device icon" src="{deviceiconuri}" width="120" height="90" />

        <br />

        <xsl:value-of select="mimetype" />
      </td>

      <td width="100%" class="uportal-channel-text" valign="top">
        <p>
          <strong>
            <xsl:value-of select="name" />
          </strong>

          <br />

          <xsl:value-of select="description" />
        </p>
      </td>

      <xsl:choose>
        <xsl:when test="sampleiconuri =''">
          <td align="center" valign="middle" class="uportal-text-small">Inget exempel
          <br />

          Tillgängligt</td>
        </xsl:when>

        <xsl:when test="sampleuri = ''">
          <td align="center" class="uportal-text-small">
            <img alt="sample icon" src="{sampleiconuri}" width="120" height="90" border="0" />
          </td>
        </xsl:when>

        <xsl:otherwise>
          <td align="center" class="uportal-text-small">
          <a href="{sampleuri}" target="_blank">
            <img alt="sample icon" src="{sampleiconuri}" width="120" height="90" border="0" />
          </a>

          <br />

          Klicka för att förstora</td>
        </xsl:otherwise>
      </xsl:choose>
    </tr>
  </xsl:template>
</xsl:stylesheet>

