package nl.rechtspraak.schema.rechtspraak_1;

import com.google.gson.JsonElement;

import java.io.NotSerializableException;

/**
 * <p>
 * Interface for {@link Uitspraak} and {@link Conclusie}
 * </p>
 * <p/>
 * Created by Maarten on 29/09/2015.
 */
public interface RechtspraakContent {
    JsonElement toJson() throws NotSerializableException;
}
