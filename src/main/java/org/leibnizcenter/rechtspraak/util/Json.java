//package org.leibnizcenter.rechtspraak.util;
//
//import com.google.common.base.Charsets;
//import com.google.gson.stream.JsonReader;
//import com.google.gson.stream.JsonToken;
//import com.squareup.okhttp.HttpUrl;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.Reader;
//
///**
// * Created by maarten on 23-12-15.
// */
//public class Json {
//    public static void cycleToEndObject(JsonReader reader) throws IOException {
//        while (!reader.peek().equals(JsonToken.END_OBJECT)) {
//            reader.skipValue();
//        }
//        reader.endObject();
//    }
//
//    public static void cycleToKeyValue(JsonReader reader, String lookFor) throws IOException {
//        while (!reader.peek().equals(JsonToken.END_OBJECT)) {
//            String key = reader.nextName();
//            if (lookFor.equals(key)) {
//                return;
//            } else {
//                reader.skipValue();
//            }
//        }
//        throw new IllegalStateException("Could not find key " + lookFor);
//    }
//
//    public static JsonReader setupReader(HttpUrl url) throws IOException {
//        Response res = new OkHttpClient().newCall(new Request.Builder().url(url).build()).execute();
//        InputStream is = res.body().byteStream();
//
//        Reader isr = new InputStreamReader(is, Charsets.UTF_8);
//        JsonReader reader = new JsonReader(isr);
//        reader.beginObject();
//
//        cycleToKeyValue(reader, "rows");
//        return reader;
//    }
//}
