package com.ewb.Utilities;

public class escapeFunctions {
    
    public static String escape(String s) {
        String retvalue = s.trim();
        if ( s.indexOf("'") != -1 ) {
            StringBuffer hold = new StringBuffer();
            char c;
            for ( int i = 0; i < s.length(); i++ ) {
                if ( (c=s.charAt(i)) == '\'' ) {
                    hold.append("''");
                } else {
                    hold.append(c);
                }
            }
            retvalue = hold.toString();
        }
        
        return retvalue;
        
    } // end method    
    
    public static String stringToHTMLString(String string) {

        // hungarian characters
        string = string.replaceAll("&aacute;", "&#225;");
        string = string.replaceAll("&eacute;", "&#233;");
        string = string.replaceAll("&iacute;", "&#237;");
        string = string.replaceAll("&oacute;", "&#243;");
        string = string.replaceAll("&ouml;", "&#246;");
        string = string.replaceAll("&uacute;", "&#250;");
        string = string.replaceAll("&uuml;", "&#252;");
        string = string.replaceAll("&Aacute;", "&#193;");
        string = string.replaceAll("&Eacute;", "&#201;");
        string = string.replaceAll("&Iacute;", "&#205;");
        string = string.replaceAll("&Oacute;", "&#211;");
        string = string.replaceAll("&Ouml;", "&#214;");
        string = string.replaceAll("&Uacute;", "&#218;");
        string = string.replaceAll("&Uuml;", "&#220;");

        // romanian characters
        string = string.replaceAll("&Agrave;", "&#192;");
        string = string.replaceAll("&Aacute;", "&#193;");
        string = string.replaceAll("&Acirc;", "&#194;");
        string = string.replaceAll("&Atilde;", "&#195;");
        string = string.replaceAll("&Auml;", "&#196;");
        string = string.replaceAll("&Aring;", "&#197;");
        string = string.replaceAll("&AElig;", "&#198;");
        string = string.replaceAll("&Ccedil;", "&#199;");
        string = string.replaceAll("&Egrave;", "&#200;");
        string = string.replaceAll("&Eacute;", "&#201;");
        string = string.replaceAll("&Ecirc;", "&#202;");
        string = string.replaceAll("&Euml;", "&#203;");
        string = string.replaceAll("&Igrave;", "&#204;");
        string = string.replaceAll("&Iacute;", "&#205;");
        string = string.replaceAll("&Icirc;", "&#206;");
        string = string.replaceAll("&Iuml;", "&#207;");
        string = string.replaceAll("&Ntilde;", "&#209;");
        string = string.replaceAll("&Ograve;", "&#210;");
        string = string.replaceAll("&Oacute;", "&#211;");
        string = string.replaceAll("&Ocirc;", "&#212;");
        string = string.replaceAll("&Otilde;", "&#213;");
        string = string.replaceAll("&Ouml;", "&#214;");
        string = string.replaceAll("&Oslash;", "&#216;");
        string = string.replaceAll("&Ugrave;", "&#217;");
        string = string.replaceAll("&Uacute;", "&#218;");
        string = string.replaceAll("&Ucirc;", "&#219;");
        string = string.replaceAll("&Uuml;", "&#220;");
        string = string.replaceAll("&Yacute;", "&#221;");
        string = string.replaceAll("&szlig;", "&#223;");
        string = string.replaceAll("&aacute;", "&#225;");
        string = string.replaceAll("&acirc;", "&#226;");
        string = string.replaceAll("&atilde;", "&#227;");
        string = string.replaceAll("&auml;", "&#228;");
        string = string.replaceAll("&aring;", "&#229;");
        string = string.replaceAll("&aelig;", "&#230;");
        string = string.replaceAll("&ccedil;", "&#231;");
        string = string.replaceAll("&egrave;", "&#232;");
        string = string.replaceAll("&eacute;", "&#233;");
        string = string.replaceAll("&icirc;", "&#238;");
        string = string.replaceAll("&ocirc;", "&#244;");
        string = string.replaceAll("&yacute;", "&#253;");
        string = string.replaceAll("&scaron;", "&#353;");
        string = string.replaceAll("&Scaron;", "&#352;");
        string = string.replaceAll("&ecirc;", "&#234;");
        string = string.replaceAll("&euml;", "&#235;");
        string = string.replaceAll("&igrave;", "&#236;");
        string = string.replaceAll("&iacute;", "&#237;");
        string = string.replaceAll("&iuml;", "&#239;");
        string = string.replaceAll("&ntilde;", "&241;");
        string = string.replaceAll("&ograve;", "&#242;");
        string = string.replaceAll("&oacute;", "&#243;");
        string = string.replaceAll("&otilde;", "&#245;");
        string = string.replaceAll("&ouml;", "&#246;");
        string = string.replaceAll("&otilde;", "&#248;");
        string = string.replaceAll("&ugrave;", "&#249;");
        string = string.replaceAll("&uacute;", "&#250;");
        string = string.replaceAll("&ucirc;", "&#251;");
        string = string.replaceAll("&uuml;", "&#252;");
        string = string.replaceAll("&thorn;", "&#254;");
        string = string.replaceAll("&yuml;", "&#255;");

        // currencies
        string = string.replaceAll("&pound;", "&#163;");
        string = string.replaceAll("&euro;", "&#8364;");

        // other
        string = string.replaceAll("á", "&#225;");
        string = string.replaceAll("é", "&#233;");
        string = string.replaceAll("í", "&#237;");
        string = string.replaceAll("ú", "&#250;");
        string = string.replaceAll("ó", "&#243;");
        string = string.replaceAll("ö", "&#246;");
        string = string.replaceAll("ő", "&#337;");
        string = string.replaceAll("ü", "&#252;");
        string = string.replaceAll("Á", "&#193;");
        string = string.replaceAll("É", "&#201;");
        string = string.replaceAll("Í", "&#205;");
        string = string.replaceAll("Ó", "&#211;");
        string = string.replaceAll("Ö", "&#214;");
        string = string.replaceAll("Ú", "&#218;");
        string = string.replaceAll("Ü", "&#220;");
        string = string.replaceAll("ű", "&#369;");
        string = string.replaceAll("Ő", "&#336;");
        string = string.replaceAll("Ű", "&#368;");

        // punctuation marks
        string = string.replaceAll("&quot;", "&#34;");
        string = string.replaceAll("&ndash;", "&#8211;");
        string = string.replaceAll("&rsquo;", "&#8217;");
        string = string.replaceAll("&ldquo;", "&#8220;");
        string = string.replaceAll("&rdquo;", "&#8221;");
        string = string.replaceAll("&bdquo;", "&#8222;");

        string = string.replaceAll("&nbsp;", "&#160;");


        // final corrections
        string = string.replaceAll("&", "&amp;");
        string = string.replaceAll("&amp;amp;", "&amp;");
        string = string.replaceAll("&amp;#", "&#");
        //string = string.replaceAll("<", "&lt;");
        //string = string.replaceAll(">", "&gt;");
        return string;
    }    
}
