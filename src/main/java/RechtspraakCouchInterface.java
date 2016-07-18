//import alpenite.openlaws.communicationLayer.data.Judgment;
//import com.alpenite.openlaws.neo4j.data.abstracts.Law;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import okhttp3.HttpUrl;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Utility methods to fetch docs from http://rechtspraak.cloudant.com/ecli
// */
//public class RechtspraakCouchInterface {
//    /**
//     * Client for doing HTTP requests
//     */
//    private static final OkHttpClient sHttpClient = new OkHttpClient();
//    /**
//     * JSON parser
//     */
//    private static final Gson mGson = new GsonBuilder()
//            .registerTypeAdapter(Judgment.class, new Judgment.JudgmentDeserializer())
////                .registerTypeAdapter(BWBWork.class, new BWBWork.BWBDocDeserializer())
//            .create();
//
//    /**
//     * Do not instantiate
//     */
//    private RechtspraakCouchInterface() {
//    }
//
//    /**
//     * Returns metadata for all judgments
//     *
//     * @param skip  number of docs to offset. Ignored if < 0
//     * @param limit size of list to return. Ignored if < 0
//     * @return List of judgment metadata
//     * @throws IOException
//     */
//    public static List<Law> getAllJudgments(int skip, int limit, String... keys) throws IOException {
//        HttpUrl.Builder builder = getEcliDbUrl()
//                .addPathSegment("_all_docs")
//                .addQueryParameter("include_docs", "true");
//        if (keys != null && keys.length > 0) {
//            builder.setQueryParameter("keys", mGson.toJson(keys));
//        } else {
//            builder.addQueryParameter("startkey", "\"ECLI:\"");
//            builder.addQueryParameter("endkey", "\"ECLJ\"");
//        }
//        if (skip > 0) builder.setQueryParameter("skip", skip + "");
//
//        final List<Law> list;
//        if (limit > 0) {
//            builder.setQueryParameter("limit", limit + "");
//            list = new ArrayList<>(limit);
//        } else {
//            list = new ArrayList<>(500000);
//        }
//
//
//        // Make request
//        Request request = new Request.Builder()
//                .url(builder.build())
//                .build();
//        Response response = sHttpClient.newCall(request).execute();
//        String json = response.body().string();
//
//        //Parse request
//        if (!response.isSuccessful()) {
//            System.err.println(response.body());
//            throw new Error("Could not open " + builder.build().toString() + "; error " + response.code());
//        }
//        JsonObject jsonObj = mGson.fromJson(json, JsonObject.class);
//        JsonArray rows = jsonObj.getAsJsonArray("rows");
//        for (int i = 0; i < rows.size(); i++) {
//            Judgment work = mGson.fromJson(rows.get(i).getAsJsonObject().get("doc"), Judgment.class);
//            list.add(work);
//        }
//
//        //Return docs
//        return list;
//    }
//
//    /**
//     *
//     * @return URL to the CouchDB containing case law
//     */
//    private static HttpUrl.Builder getEcliDbUrl() {
//        return new HttpUrl.Builder()
//                .scheme("http")
//                .host("rechtspraak.cloudant.com")
//                .addPathSegment("ecli");
//    }
//
//    /**
//     * @param ecli ECLI identifier
//     * @return URL to HTML manifestation
//     */
//    public static String getHtmlSnippetUrl(String ecli) {
//        return getEcliDbUrl().addPathSegment(ecli).addPathSegment("data.htm").build().toString();
//    }
//}
