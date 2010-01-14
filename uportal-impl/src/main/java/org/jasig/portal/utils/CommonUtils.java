/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.portal.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * CommonUtils class contains base useful utilities
 * @author <a href="mailto:mvi@immagic.com">Michael Ivanov</a>
 * @version $Revision$
 */
public class CommonUtils {
	
	/**
     * Sets "no cache" directives to the given response
     * @param res <code>HttpServletResponse</code> response
     */
	public static void setNoCache( HttpServletResponse res ) {
	    res.setHeader("Pragma","no-cache");
	    res.setHeader("Cache-Control","no-cache");
	    res.setDateHeader("Expires",0);
	}

    /**
     * Replaces "sourceString" with "replaceString" if sourceString equals null
     * @param sourceStr the string to replace
     * @param replaceStr the replacement string
     * @return the processed string
     */
    public static String nvl( String sourceStr, String replaceStr ) {
      return (sourceStr != null)?sourceStr:replaceStr;
    }
    
	/**
		 * Replaces "sourceString" with "replaceString" if sourceString is null or empty
		 * @param sourceStr the string to replace
		 * @param replaceStr the replacement string
		 * @return the processed string
		 */
	public static String envl( String sourceStr, String replaceStr ) {
		  return (sourceStr != null && sourceStr.trim().length() > 0 )?sourceStr:replaceStr;
	}

    /**
     * Replaces "sourceString" with "replaceString" if sourceString equals null
     * @param sourceStr the string to replace
     * @param replaceStr the replacement string
     * @param prefix the prefix
     * @return the processed string
     */
    public static String nvl( String sourceStr, String replaceStr, String prefix ) {
      String nvlStr = nvl(sourceStr,replaceStr);
      if ( !nvlStr.trim().equals("") )
        return new String(prefix+nvlStr);
      else
        return nvlStr;
    }

    /**
     * Replaces "sourceString" with "" if sourceString equals null
     * @param sourceStr string to replace
     * @return the processed string
     */
    public static String nvl( String sourceStr ) {
      return nvl(sourceStr,"");
    }

    /**
     * replaces "replacedString" with "newString" in "text"
     * @param text text where to replace
     * @param replacedString string to replace
     * @param newString new string
     * @return new text
     */
    public static String replaceText(String text, String replacedString, String newString) {
        int i=text.indexOf(replacedString);
        if(i>=0){
            StringBuffer buffer = new StringBuffer(text.length());
            int from=0;
            int rl = replacedString.length(); 
            while(i>=0){
                buffer.append(text.substring(from,i)).append(newString);
                from=i+rl;
                i=text.indexOf(replacedString,from);
            }
            buffer.append(text.substring(from));
            return buffer.toString();
        }
        return text;
    }   

    public static void replaceSubstVariables(Hashtable original, Hashtable subst) {

      for (Enumeration original_keys = original.keys(); original_keys.hasMoreElements(); ) {
        String original_key = (String)original_keys.nextElement();
        String original_value = (String)original.get(original_key);

        for (Enumeration subst_keys = subst.keys(); subst_keys.hasMoreElements(); ) {
          String subst_key = (String)subst_keys.nextElement();
          String subst_value = (String)subst.get(subst_key);

          original_value = replaceText(original_value, subst_key, subst_value);
        }

        original.put(original_key, original_value);

      }

    }

    public static String stackTraceToString(Exception e) {
      StringWriter strwrt = new StringWriter();
      e.printStackTrace(new PrintWriter(strwrt));
      return strwrt.toString();
    }


    /**
     * This method gets an array of strings from given string splitted by commas.
     * @param str a string value
     * @param delim a delimeter
     * @return an array of strings
     */
    public static String[] getSplitStringByCommas ( String str, String delim ) {
      if ( str == null ) return null;
     StringTokenizer st = new StringTokenizer(str,delim);
      String[] strArray = new String[st.countTokens()];
      for ( int i = 0; st.hasMoreTokens(); i++ ) {
       strArray[i] = st.nextToken().trim();
      }
      return strArray;
    }

    /**
     * This method gets a properties of strings from given string splitted by commas.
     * @param keys a string keys for properties
     * @param values a string value
     * @param delim a delimeter
     * @return an array of strings
     */
    public static Properties getSplitStringByCommas ( String keys, String values, String delim ) {
      if ( values == null || keys == null ) 
        return null;
      StringTokenizer stValues = new StringTokenizer(values,delim);
      StringTokenizer stKeys = new StringTokenizer(keys,delim);
      Properties props = new Properties();
      while ( stValues.hasMoreTokens() && stKeys.hasMoreTokens() ) {
        props.put(stKeys.nextToken().trim(),stValues.nextToken().trim());
      }
      return props;
    }

    /**
     * This method gets a properties of int from given string splitted by commas.
     * @param str a string keys for properties
     * @param delim a delimeter
     * @param def a default value
     * @return an array of int
     */
    public static int[] getSplitIntByCommas ( String str, String delim, int def ) {
      if (str==null) 
        str = "";
      String [] strarr = getSplitStringByCommas(str, delim);
      int[] intarr = new int[strarr.length];
      for (int i=0; i < strarr.length; i++) {
        intarr[i] = parseInt(strarr[i],def);
      }
      return intarr;
    }

    public static Hashtable getFamilyProps(Properties props, String prefix, String delim, Properties defaultProps) {
      Hashtable hash = new Hashtable();
      Enumeration en = props.propertyNames();
      String propName = null;
      String propShort = null;
      String itemName = null;
      String itemPropName = null;
      String itemPropValue = null;
      StringTokenizer st = null;
      Properties itemProps = null;
      while (en.hasMoreElements()) {
        propName = (String) en.nextElement();
        if (propName.startsWith(prefix)) {
          propShort = propName.substring(prefix.length());
          st = new StringTokenizer(propShort,delim);
          itemName = null;
          itemPropName = null;
          itemPropValue = null;
          if ( st.hasMoreTokens() ) {
            itemName = st.nextToken().trim();
            if ( st.hasMoreTokens() ) {
              itemPropName = st.nextToken().trim();
            }
          }
          if ((itemName!=null)&&(itemPropName!=null)) {
            itemPropValue = props.getProperty(propName, "");
            itemProps = (Properties)hash.get(itemName);
            if (itemProps==null) { 
              itemProps = new Properties(defaultProps); 
            }
            itemProps.put(itemPropName, itemPropValue);
            hash.put(itemName, itemProps);
          }
        }
      }
      return hash;
    }

    public static String[] getFamilyPropertyArrayString(Hashtable hash, String[] keys, String propName, String[] def) {
      String[] st = new String[keys.length];
      for (int i = 0;i<st.length;i++) {
        st[i] = getFamilyPropertyString(hash, keys[i], propName, null);
        if ((def!=null) && (st[i]==null) && (def.length>i)) { st[i] = def[i];}
      }
      return st;
    }

    public static String getFamilyPropertyString(Hashtable hash, String key, String propName, String def) {
      return ((Properties)hash.get(key)).getProperty(propName,def);
    }


    public static String[] getFamilyPropertyArrayString(Hashtable hash, String[] keys, String propName, String def) {
      String[] st = new String[keys.length];
      for (int i = 0;i<st.length;i++) {
        st[i] = def;
      }
      return getFamilyPropertyArrayString(hash, keys,propName,st);
    }

    public static String[] getFamilyPropertyArrayString(Hashtable hash, String[] keys, String propName) {
      return getFamilyPropertyArrayString(hash, keys,propName, (String[])null);
    }


    public static boolean[] getFamilyPropertyArrayBoolean(Hashtable hash, String[] keys, String propName, boolean def) {
      boolean[] st = new boolean[keys.length];
      for (int i = 0;i<st.length;i++) {
        st[i] = parseBoolean(((Properties)hash.get(keys[i])).getProperty(propName), def);
      }
      return st;
    }

    public static boolean[] getFamilyPropertyArrayBoolean(Hashtable hash, String[] keys, String propName) {
      return getFamilyPropertyArrayBoolean(hash, keys, propName, false);
    }


    public static int[] getFamilyPropertyArrayInt(Hashtable hash, String[] keys, String propName) {
      int[] st = new int[keys.length];
      for (int i = 0;i<st.length;i++) {
        st[i] = parseInt(((Properties)hash.get(keys[i])).getProperty(propName));
      }
      return st;
    }


    // parse "yes" and "no"
    public static boolean parseBoolean(String str, boolean defaultValue) {
       boolean res = defaultValue;
       if ("yes".equalsIgnoreCase(str))
          res = true;
       if ("no".equalsIgnoreCase(str))
          res = false;
       return res;
    }


    /**
     * This method returns the String representation of the given boolean value.
     * @param bool a value of boolean type
     * @return "true" if bool is true, "false" - otherwise
     */
    public static String boolToStr ( boolean bool ) {
      return (bool)?"true":"false";
    }

    /**
     * This method returns the boolean value for the given String representation ("true"-"false").
     * @param bool a <code>String</code> value
     * @return true if bool is "true", false - otherwise
     */
    public static boolean strToBool ( String bool ) {
      return ("true".equalsIgnoreCase(bool))?true:false;
    }

    /**
     * This method returns the boolean value for the given String representation ("yes"-"no").
     * @param str a <code>String</code> value
     * @return true if str is "yes", false - otherwise
     */
    public static boolean parseBoolean(String str) {
      return parseBoolean(str,false);
    }


    // Parse integer string value
    public static int parseInt(String str, int defaultValue) {
      try {
        return Integer.parseInt(str);
      } catch ( Exception e ) {
        return defaultValue;
      }
    }

    public static int parseInt(String str) {
      return parseInt(str,-1);
    }

    public static boolean odd(int i) {
      return (i >> 1 << 1 != i);
    }

    /**
     * This method checks if an array of objects is empty or not.
     * @param objects an array of objects
     * @return true if array is empty, false - otherwise
     */
    public static boolean isArrayEmpty ( Object objects[] ) {
      if ( objects == null ) 
        return true;
      for ( int i = 0; i < objects.length; i++ ) {
        if ( objects[i] != null )
        return false;
      }
      return true;
    }

    /**
     * This method checks if an array of strings is empty or not.
     * @param objects an array of strings
     * @return true if array is empty, false - otherwise
     */
    public static boolean isArrayEmpty ( String objects[] ) {
      if ( objects == null ) 
        return true;
      for ( int i = 0; i < objects.length; i++ ) {
        if ( objects[i] != null && !"".equals(objects[i]) )
          return false;
      }
      return true;
    }
}
