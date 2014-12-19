package mkremins.fanciful;

import org.bukkit.configuration.serialization.*;
import javax.annotation.concurrent.*;
import com.google.gson.stream.*;
import java.io.*;
import java.util.*;

@Immutable
final class JsonString implements JsonRepresentedObject, ConfigurationSerializable
{
    private String _value;
    
    public JsonString(final String value) {
        super();
        this._value = value;
    }
    
    @Override
    public void writeJson(final JsonWriter writer) throws IOException {
        writer.value(this.getValue());
    }
    
    public String getValue() {
        return this._value;
    }
    
    public Map<String, Object> serialize() {
        final HashMap<String, Object> theSingleValue = new HashMap<String, Object>();
        theSingleValue.put("stringValue", this._value);
        return theSingleValue;
    }
    
    public static JsonString deserialize(final Map<String, Object> map) {
        return new JsonString(map.get("stringValue").toString());
    }
    
    @Override
    public String toString() {
        return this._value;
    }
}
