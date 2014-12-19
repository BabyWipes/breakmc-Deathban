package mkremins.fanciful;

import com.google.gson.stream.*;
import org.bukkit.*;
import java.util.logging.*;
import java.io.*;
import java.util.*;
import com.google.common.collect.*;
import org.bukkit.configuration.serialization.*;

final class MessagePart implements JsonRepresentedObject, ConfigurationSerializable, Cloneable
{
    ChatColor color;
    ArrayList<ChatColor> styles;
    String clickActionName;
    String clickActionData;
    String hoverActionName;
    JsonRepresentedObject hoverActionData;
    TextualComponent text;
    static final BiMap<ChatColor, String> stylesToNames;
    
    MessagePart(final TextualComponent text) {
        super();
        this.color = ChatColor.WHITE;
        this.styles = new ArrayList<ChatColor>();
        this.clickActionName = null;
        this.clickActionData = null;
        this.hoverActionName = null;
        this.hoverActionData = null;
        this.text = null;
        this.text = text;
    }
    
    MessagePart() {
        super();
        this.color = ChatColor.WHITE;
        this.styles = new ArrayList<ChatColor>();
        this.clickActionName = null;
        this.clickActionData = null;
        this.hoverActionName = null;
        this.hoverActionData = null;
        this.text = null;
        this.text = null;
    }
    
    boolean hasText() {
        return this.text != null;
    }
    
    public MessagePart clone() throws CloneNotSupportedException {
        final MessagePart obj = (MessagePart)super.clone();
        obj.styles = (ArrayList<ChatColor>)this.styles.clone();
        if (this.hoverActionData instanceof JsonString) {
            obj.hoverActionData = new JsonString(((JsonString)this.hoverActionData).getValue());
        }
        else if (this.hoverActionData instanceof FancyMessage) {
            obj.hoverActionData = ((FancyMessage)this.hoverActionData).clone();
        }
        return obj;
    }
    
    @Override
    public void writeJson(final JsonWriter json) {
        try {
            json.beginObject();
            this.text.writeJson(json);
            json.name("color").value(this.color.name().toLowerCase());
            for (final ChatColor style : this.styles) {
                json.name((String)MessagePart.stylesToNames.get((Object)style)).value(true);
            }
            if (this.clickActionName != null && this.clickActionData != null) {
                json.name("clickEvent").beginObject().name("action").value(this.clickActionName).name("value").value(this.clickActionData).endObject();
            }
            if (this.hoverActionName != null && this.hoverActionData != null) {
                json.name("hoverEvent").beginObject().name("action").value(this.hoverActionName).name("value");
                this.hoverActionData.writeJson(json);
                json.endObject();
            }
            json.endObject();
        }
        catch (IOException e) {
            Bukkit.getLogger().log(Level.WARNING, "A problem occured during writing of JSON string", e);
        }
    }
    
    public Map<String, Object> serialize() {
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("text", this.text);
        map.put("styles", this.styles);
        map.put("color", this.color.getChar());
        map.put("hoverActionName", this.hoverActionName);
        map.put("hoverActionData", this.hoverActionData);
        map.put("clickActionName", this.clickActionName);
        map.put("clickActionData", this.clickActionData);
        return map;
    }
    
    public static MessagePart deserialize(final Map<String, Object> serialized) {
        final MessagePart part = new MessagePart(serialized.get("text"));
        part.styles = serialized.get("styles");
        part.color = ChatColor.getByChar(serialized.get("color").toString());
        part.hoverActionName = serialized.get("hoverActionName").toString();
        part.hoverActionData = serialized.get("hoverActionData");
        part.clickActionName = serialized.get("clickActionName").toString();
        part.clickActionData = serialized.get("clickActionData").toString();
        return part;
    }
    
    static {
        final ImmutableBiMap.Builder<ChatColor, String> builder = (ImmutableBiMap.Builder<ChatColor, String>)ImmutableBiMap.builder();
        for (final ChatColor style : ChatColor.values()) {
            if (style.isFormat()) {
                String styleName = null;
                switch (style) {
                    case MAGIC: {
                        styleName = "obfuscated";
                        break;
                    }
                    case UNDERLINE: {
                        styleName = "underlined";
                        break;
                    }
                    default: {
                        styleName = style.name().toLowerCase();
                        break;
                    }
                }
                builder.put((Object)style, (Object)styleName);
            }
        }
        stylesToNames = (BiMap)builder.build();
        ConfigurationSerialization.registerClass((Class)MessagePart.class);
    }
}
