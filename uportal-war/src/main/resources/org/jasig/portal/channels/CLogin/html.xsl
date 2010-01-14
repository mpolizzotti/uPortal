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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html" indent="no"/>
    
    <!-- ========== VARIABLES & PARAMETERS ========== -->
    <xsl:param name="baseActionURL">default</xsl:param>
    <xsl:param name="unauthenticated">true</xsl:param>
    <xsl:param name="locale">en_US</xsl:param>
    <xsl:param name="mediaPath" select="'media/org/jasig/portal/channels/CLogin'"/>
    <xsl:param name="portalInstitutionName">JA-SIG</xsl:param>
    <xsl:param name="portalName">uPortal</xsl:param>
    <xsl:param name="forgotLoginUrl">http://www.uportal.org/</xsl:param>
    <xsl:param name="contactAdminUrl">http://www.uportal.org/</xsl:param>
    <xsl:param name="casLoginUrl"></xsl:param>
    <xsl:param name="casNewUserUrl"></xsl:param>
  <!-- ========== VARIABLES & PARAMETERS ========== -->
    
    
    <!-- Match on root element then check if the user is NOT authenticated. -->
    <xsl:template match="/">
       <xsl:apply-templates/>
    </xsl:template>
    
    
    <!-- If user is not authenticated insert login form. -->
    <xsl:template match="login-status">
    
      <div id="portalLoginStandard" class="fl-widget-content">
        <xsl:apply-templates/>
        <form id="portalLoginForm" action="Login" method="post">
          <input type="hidden" name="action" value="login"/>
          <label for="userName">Username:</label>
          <input type="text" name="userName" size="15" value="{failure/@attemptedUserName}"/>
          <label for="password">Password:</label>
          <input type="password" name="password" size="15"/>
          <input type="submit" value="Sign In" name="Login" id="portalLoginButton" class="portlet-form-button"/>
        </form>
        <p><a id="portalLoginForgot" href="{$forgotLoginUrl}">Forgot your username or password?</a></p>
      </div>
      
    </xsl:template>
    
    
    <!-- If user login fails present error message box. -->
    <xsl:template match="failure">
    
    	<div id="portalLoginMessage" class="portlet-msg-alert">
        <h2>Important Message</h2>
        <p>The username and password you entered do not match any accounts on record. Please make sure that you have correctly entered the username associated with your <xsl:value-of select="$portalName"/> account.</p>
        <p><a id="portalLoginErrorForgot" href="{$forgotLoginUrl}">Forgot your username or password?</a></p>
      </div>
        
    </xsl:template>
    
    
    <!-- If user login encounters an error present error message box. -->
    <xsl:template match="error">
    
      <div id="portalLoginMessage" class="portlet-msg-error">
        <h2>Important Message</h2>
        <p><xsl:value-of select="$portalName"/> is unable to complete your login request at this time. It is possible the system is down for maintenance or other reasons. Please try again later. If this problem persists, contact <a href="{$contactAdminUrl}"><xsl:value-of select="$portalInstitutionName"/></a></p>
      </div>
        
    </xsl:template>
    
    
    <!-- Error message box. -->
    <xsl:template name="message">
    
       <xsl:param name="messageString"/>
       <div id="portalLoginMessage">
					<h2>Important Message</h2>
          <xsl:value-of select="$messageString"/>
       </div>
       
    </xsl:template>
    
    
</xsl:stylesheet>
