/**
 * Copyright (c) 2000 The JA-SIG Collaborative.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. Redistributions of any form whatsoever must retain the following
 *    acknowledgment:
 *    "This product includes software developed by the JA-SIG Collaborative
 *    (http://www.jasig.org/)."
 *
 * THIS SOFTWARE IS PROVIDED BY THE JA-SIG COLLABORATIVE "AS IS" AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE JA-SIG COLLABORATIVE OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.jasig.portal;

import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;

import java.io.*;
import java.util.*;
import java.net.URLEncoder;
import org.jasig.portal.layout.*;

/**
 * Methods to accompany dispatch.jsp
 * @author Ken Weiner
 * @version $Revision$
 */
public class DispatchBean extends GenericPortalBean
{
  /**
   * Returns an instance of the current channel
   * @param the servlet request object
   * @return the channel to call
   */
  public IChannel getChannel (HttpServletRequest req)
  {
    org.jasig.portal.IChannel ch = null;

    try
    {
      HttpSession session = req.getSession (false);
      ILayoutBean layoutBean = (ILayoutBean) session.getAttribute ("layoutBean");
      String sChannelID = req.getParameter ("channelID");

      if (sChannelID != null)
        ch = layoutBean.getChannelInstance (sChannelID);
      else
        Logger.log (Logger.ERROR, "To dispatch properly, a channel ID must be included in the query string in the form: \"...&channelID=...\"");
    }
    catch (Exception e)
    {
      Logger.log (Logger.ERROR, e);
    }
    return ch;
  }

  /**
   * This method should be called by a channel when it wants
   * to return to layout.jsp
   * @param the servlet request object
   */
  public static void finish (HttpServletRequest req, HttpServletResponse res)
  {
    try
    {
      res.sendRedirect ("layout.jsp");
    }
    catch (Exception e)
    {
      Logger.log (Logger.ERROR, e);
    }
  }

  /**
   * Builds a url used to send control back to a particular
   * method of a channel
   * @param the name of the channel's method to call
   * @param the channel config object
   * @return a url used to call a method in a channel
   */
  public static String buildURL (String sMethodName, ChannelConfig chConfig)
  {
    try
    {
      return buildURL (sMethodName, chConfig.getChannelID ());
    }
    catch (Exception e)
    {
      Logger.log (Logger.ERROR, e);
    }
    return null;
  }

  /**
   * Builds a url used to send control back to a particular
   * method of a channel
   * @param the name of the channel's method to call
   * @param the channel's ID
   * @return a url used to call a method in a channel
   */
  public static String buildURL (String sMethodName, String sChannelID)
  {
    try
    {
      StringBuffer sbURL = new StringBuffer ("dispatch.jsp");
      sbURL.append ("?method=");
      sbURL.append (URLEncoder.encode (sMethodName));
      sbURL.append ("&channelID=");
      sbURL.append (URLEncoder.encode (sChannelID));
      return sbURL.toString ();
    }
    catch (Exception e)
    {
      Logger.log (Logger.ERROR, e);
    }
    return null;
  }

}