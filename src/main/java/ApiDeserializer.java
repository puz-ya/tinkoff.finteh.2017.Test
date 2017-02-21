import com.google.gson.*;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * Created by Puzino Yury on 20.02.2017.
 */
public class ApiDeserializer implements JsonDeserializer<ApiResponseObject> {

    @Nullable
    public ApiResponseObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ApiResponseObject apiResponseObject = null;

        //create gson with api & rates deserializers
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(RateObject.class, new RateDeserializer())
                .create();

        if (json.isJsonObject()) {

            //JsonParser parser = new JsonParser();
            //JsonObject array = parser.parse(json.getAsJsonObject()).getAsJsonObject();
            JsonObject array = json.getAsJsonObject();

            if (array.entrySet().size() > 0) {
                String base = gson.fromJson(array.get("base"), String.class);
                String date = gson.fromJson(array.get("date"), String.class);
                RateObject rateObject = gson.fromJson(array.getAsJsonObject("rates"), RateObject.class);
                apiResponseObject = new ApiResponseObject(base, date, rateObject);
            }
        }

        return apiResponseObject;
    }

}
