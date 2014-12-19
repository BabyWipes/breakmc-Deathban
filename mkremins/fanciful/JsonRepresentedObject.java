package mkremins.fanciful;

import com.google.gson.stream.*;
import java.io.*;

interface JsonRepresentedObject
{
    void writeJson(JsonWriter p0) throws IOException;
}
