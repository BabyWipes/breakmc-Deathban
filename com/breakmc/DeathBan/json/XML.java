package com.breakmc.DeathBan.json;

import java.util.*;

public class XML
{
    public static final Character AMP;
    public static final Character APOS;
    public static final Character BANG;
    public static final Character EQ;
    public static final Character GT;
    public static final Character LT;
    public static final Character QUEST;
    public static final Character QUOT;
    public static final Character SLASH;
    
    public static String escape(final String string) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0, length = string.length(); i < length; ++i) {
            final char c = string.charAt(i);
            switch (c) {
                case '&': {
                    sb.append("&amp;");
                    break;
                }
                case '<': {
                    sb.append("&lt;");
                    break;
                }
                case '>': {
                    sb.append("&gt;");
                    break;
                }
                case '\"': {
                    sb.append("&quot;");
                    break;
                }
                case '\'': {
                    sb.append("&apos;");
                    break;
                }
                default: {
                    sb.append(c);
                    break;
                }
            }
        }
        return sb.toString();
    }
    
    public static void noSpace(final String string) throws JSONException {
        final int length = string.length();
        if (length == 0) {
            throw new JSONException("Empty string.");
        }
        for (int i = 0; i < length; ++i) {
            if (Character.isWhitespace(string.charAt(i))) {
                throw new JSONException("'" + string + "' contains a space character.");
            }
        }
    }
    
    private static boolean parse(final XMLTokener x, final JSONObject context, final String name) throws JSONException {
        JSONObject jsonobject = null;
        Object token = x.nextToken();
        if (token == XML.BANG) {
            final char c = x.next();
            if (c == '-') {
                if (x.next() == '-') {
                    x.skipPast("-->");
                    return false;
                }
                x.back();
            }
            else if (c == '[') {
                token = x.nextToken();
                if ("CDATA".equals(token) && x.next() == '[') {
                    final String string = x.nextCDATA();
                    if (string.length() > 0) {
                        context.accumulate("content", string);
                    }
                    return false;
                }
                throw x.syntaxError("Expected 'CDATA['");
            }
            int i = 1;
            do {
                token = x.nextMeta();
                if (token == null) {
                    throw x.syntaxError("Missing '>' after '<!'.");
                }
                if (token == XML.LT) {
                    ++i;
                }
                else {
                    if (token != XML.GT) {
                        continue;
                    }
                    --i;
                }
            } while (i > 0);
            return false;
        }
        if (token == XML.QUEST) {
            x.skipPast("?>");
            return false;
        }
        if (token == XML.SLASH) {
            token = x.nextToken();
            if (name == null) {
                throw x.syntaxError("Mismatched close tag " + token);
            }
            if (!token.equals(name)) {
                throw x.syntaxError("Mismatched " + name + " and " + token);
            }
            if (x.nextToken() != XML.GT) {
                throw x.syntaxError("Misshaped close tag");
            }
            return true;
        }
        else {
            if (token instanceof Character) {
                throw x.syntaxError("Misshaped tag");
            }
            final String tagName = (String)token;
            token = null;
            jsonobject = new JSONObject();
            while (true) {
                if (token == null) {
                    token = x.nextToken();
                }
                if (token instanceof String) {
                    final String string = (String)token;
                    token = x.nextToken();
                    if (token == XML.EQ) {
                        token = x.nextToken();
                        if (!(token instanceof String)) {
                            throw x.syntaxError("Missing value");
                        }
                        jsonobject.accumulate(string, stringToValue((String)token));
                        token = null;
                    }
                    else {
                        jsonobject.accumulate(string, "");
                    }
                }
                else if (token == XML.SLASH) {
                    if (x.nextToken() != XML.GT) {
                        throw x.syntaxError("Misshaped tag");
                    }
                    if (jsonobject.length() > 0) {
                        context.accumulate(tagName, jsonobject);
                    }
                    else {
                        context.accumulate(tagName, "");
                    }
                    return false;
                }
                else {
                    if (token != XML.GT) {
                        throw x.syntaxError("Misshaped tag");
                    }
                    while (true) {
                        token = x.nextContent();
                        if (token == null) {
                            if (tagName != null) {
                                throw x.syntaxError("Unclosed tag " + tagName);
                            }
                            return false;
                        }
                        else if (token instanceof String) {
                            final String string = (String)token;
                            if (string.length() <= 0) {
                                continue;
                            }
                            jsonobject.accumulate("content", stringToValue(string));
                        }
                        else {
                            if (token == XML.LT && parse(x, jsonobject, tagName)) {
                                if (jsonobject.length() == 0) {
                                    context.accumulate(tagName, "");
                                }
                                else if (jsonobject.length() == 1 && jsonobject.opt("content") != null) {
                                    context.accumulate(tagName, jsonobject.opt("content"));
                                }
                                else {
                                    context.accumulate(tagName, jsonobject);
                                }
                                return false;
                            }
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    public static Object stringToValue(final String string) {
        if ("".equals(string)) {
            return string;
        }
        if ("true".equalsIgnoreCase(string)) {
            return Boolean.TRUE;
        }
        if ("false".equalsIgnoreCase(string)) {
            return Boolean.FALSE;
        }
        if ("null".equalsIgnoreCase(string)) {
            return JSONObject.NULL;
        }
        if ("0".equals(string)) {
            return new Integer(0);
        }
        try {
            char initial = string.charAt(0);
            boolean negative = false;
            if (initial == '-') {
                initial = string.charAt(1);
                negative = true;
            }
            if (initial == '0' && string.charAt(negative ? 2 : 1) == '0') {
                return string;
            }
            if (initial >= '0' && initial <= '9') {
                if (string.indexOf(46) >= 0) {
                    return Double.valueOf(string);
                }
                if (string.indexOf(101) < 0 && string.indexOf(69) < 0) {
                    final Long myLong = new Long(string);
                    if (myLong == (int)(Object)myLong) {
                        return new Integer((int)(Object)myLong);
                    }
                    return myLong;
                }
            }
        }
        catch (Exception ex) {}
        return string;
    }
    
    public static JSONObject toJSONObject(final String string) throws JSONException {
        final JSONObject jo = new JSONObject();
        final XMLTokener x = new XMLTokener(string);
        while (x.more() && x.skipPast("<")) {
            parse(x, jo, null);
        }
        return jo;
    }
    
    public static String toString(final Object object) throws JSONException {
        return toString(object, null);
    }
    
    public static String toString(Object object, final String tagName) throws JSONException {
        final StringBuffer sb = new StringBuffer();
        if (object instanceof JSONObject) {
            if (tagName != null) {
                sb.append('<');
                sb.append(tagName);
                sb.append('>');
            }
            final JSONObject jo = (JSONObject)object;
            final Iterator keys = jo.keys();
            while (keys.hasNext()) {
                final String key = keys.next().toString();
                Object value = jo.opt(key);
                if (value == null) {
                    value = "";
                }
                if (value instanceof String) {
                    final String string = (String)value;
                }
                else {
                    final String string = null;
                }
                if ("content".equals(key)) {
                    if (value instanceof JSONArray) {
                        final JSONArray ja = (JSONArray)value;
                        for (int length = ja.length(), i = 0; i < length; ++i) {
                            if (i > 0) {
                                sb.append('\n');
                            }
                            sb.append(escape(ja.get(i).toString()));
                        }
                    }
                    else {
                        sb.append(escape(value.toString()));
                    }
                }
                else if (value instanceof JSONArray) {
                    final JSONArray ja = (JSONArray)value;
                    for (int length = ja.length(), i = 0; i < length; ++i) {
                        value = ja.get(i);
                        if (value instanceof JSONArray) {
                            sb.append('<');
                            sb.append(key);
                            sb.append('>');
                            sb.append(toString(value));
                            sb.append("</");
                            sb.append(key);
                            sb.append('>');
                        }
                        else {
                            sb.append(toString(value, key));
                        }
                    }
                }
                else if ("".equals(value)) {
                    sb.append('<');
                    sb.append(key);
                    sb.append("/>");
                }
                else {
                    sb.append(toString(value, key));
                }
            }
            if (tagName != null) {
                sb.append("</");
                sb.append(tagName);
                sb.append('>');
            }
            return sb.toString();
        }
        if (object.getClass().isArray()) {
            object = new JSONArray(object);
        }
        if (object instanceof JSONArray) {
            final JSONArray ja = (JSONArray)object;
            for (int length = ja.length(), i = 0; i < length; ++i) {
                sb.append(toString(ja.opt(i), (tagName == null) ? "array" : tagName));
            }
            return sb.toString();
        }
        final String string = (object == null) ? "null" : escape(object.toString());
        return (tagName == null) ? ("\"" + string + "\"") : ((string.length() == 0) ? ("<" + tagName + "/>") : ("<" + tagName + ">" + string + "</" + tagName + ">"));
    }
    
    static {
        AMP = new Character('&');
        APOS = new Character('\'');
        BANG = new Character('!');
        EQ = new Character('=');
        GT = new Character('>');
        LT = new Character('<');
        QUEST = new Character('?');
        QUOT = new Character('\"');
        SLASH = new Character('/');
    }
}
