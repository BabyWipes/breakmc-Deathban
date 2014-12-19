package com.breakmc.DeathBan.serialization;

import com.breakmc.DeathBan.json.*;
import java.io.*;
import java.util.*;

public class Serializer
{
    public static String toString(final JSONObject object) {
        return toString(object, true);
    }
    
    public static String toString(final JSONObject object, final boolean pretty) {
        return toString(object, pretty, 5);
    }
    
    public static String toString(final JSONObject object, final boolean pretty, final int tabSize) {
        try {
            if (pretty) {
                return object.toString(tabSize);
            }
            return object.toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static JSONObject getObjectFromFile(final File file) throws FileNotFoundException, JSONException {
        return getObjectFromStream(new FileInputStream(file));
    }
    
    public static JSONObject getObjectFromStream(final InputStream stream) throws JSONException {
        return new JSONObject(getStringFromStream(stream));
    }
    
    public static String getStringFromFile(final File file) throws FileNotFoundException {
        return getStringFromStream(new FileInputStream(file));
    }
    
    public static String getStringFromStream(final InputStream stream) {
        final Scanner x = new Scanner(stream);
        String str = "";
        while (x.hasNextLine()) {
            str = str + x.nextLine() + "\n";
        }
        x.close();
        return str.trim();
    }
}
