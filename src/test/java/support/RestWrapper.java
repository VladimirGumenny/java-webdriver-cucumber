package support;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.MultipartBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RestWrapper {

    private String baseUrl = "https://skryabin.com/recruit/api/v1/";
    private static String loginToken;

    private final String CONTENT_TYPE = "Content-Type";
    private final String JSON = "application/json";
    private final String TOKEN = "x-access-token";
    public static final String POSITIONS = "positions";
    public static final String POSITION = "position";
    public static final String CANDIDATES = "candidates";
    public static final String APPLICATIONS = "applications";

    public RestWrapper login(HashMap<String, String> credentials) throws UnirestException {
         RequestBodyEntity request = Unirest.post(baseUrl + "login")
                        .header(CONTENT_TYPE, JSON)
                        .body(new JSONObject(credentials));

         HttpResponse<JsonNode> response = request.asJson();
         assertThat(response.getStatus()).isEqualTo(200);
         JSONObject body = response.getBody().getObject();
         loginToken = body.getString("token");
         System.out.println("\n\nloginToken=" + loginToken);
         return this;
    }

    public JSONObject createPosition(HashMap<String, String> position) throws UnirestException {
        String dateOpen = position.get("dateOpen");
        String dateOpenISO = new SimpleDateFormat("yyyy-MM-dd").format(new Date(dateOpen));
        dateOpenISO += "T05:00:00.000Z";
        position.put("dateOpen", dateOpenISO);

        JSONObject positionJson = new JSONObject(position);
        RequestBodyEntity request = Unirest.post(baseUrl + POSITIONS)
                .header(CONTENT_TYPE, JSON)
                .header(TOKEN, loginToken)
                .body(positionJson);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(201);
        JSONObject responsePositionJson = response.getBody().getObject();
        System.out.println("\n\nPosition created: " + responsePositionJson);

        TestContext.setTestData(POSITION, responsePositionJson);

        return responsePositionJson;
    }

    public void deletePosition(int positionId) throws UnirestException {
        HttpRequest request = Unirest.delete(baseUrl + POSITIONS + "/" + positionId)
                .header(TOKEN, loginToken);
        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(204);
        System.out.println("\n\nDeleted position id: " + positionId);
    }


    public JSONArray getPositions() throws UnirestException {
        HttpRequest request = Unirest.get(baseUrl + POSITIONS);

        HttpResponse<JsonNode> response = request.asJson();

        assertThat(response.getStatus()).isBetween(200, 204);
        JSONArray positionsJson = response.getBody().getArray();

        return positionsJson;
    }

    public JSONObject updatePosition(HashMap<String, String> fields, int positionId) throws Exception {
        JSONObject fieldsJson = new JSONObject(fields);
        RequestBodyEntity request = Unirest.put(baseUrl + POSITIONS + "/" + positionId)
                .header(CONTENT_TYPE, JSON)
                .header(TOKEN, loginToken)
                .body(fieldsJson);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(200);

        JSONObject responseFieldsJson = response.getBody().getObject();
        System.out.println("\n\nPosition " + positionId + " is updated: " + responseFieldsJson);

        return responseFieldsJson;
    }

    public JSONObject getPositionById(int positionId) throws UnirestException {
        GetRequest request = Unirest.get(baseUrl + POSITIONS + "/" + positionId);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(200);

        JSONObject positionJson = response.getBody().getObject();

        return positionJson;
    }

    public JSONArray getCandidate(HashMap<String, String> candidate) throws UnirestException {
        String email = candidate.get("email");
        String firstName = candidate.get("firstName");
        String lastName = candidate.get("lastName");
        String path = baseUrl + CANDIDATES + "?firstName=" + firstName + "&lastName=" + lastName + "&email=" + email;
        GetRequest request = Unirest.get(path);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(200);

        return response.getBody().getArray();
    }

    public JSONArray getApplications(int candidateId, int positionId) throws UnirestException {
        String path = baseUrl + APPLICATIONS + "?candidateId=" + candidateId + "&positionId=" + positionId;
        GetRequest request = Unirest.get(path);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(200);

        return response.getBody().getArray();
    }

    public JSONArray getApplications() throws UnirestException {
        GetRequest request = Unirest.get(baseUrl + APPLICATIONS);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(200);

        return response.getBody().getArray();
    }

    public JSONObject getApplicationById(int applicationId) throws UnirestException {
        GetRequest request = Unirest.get(baseUrl + APPLICATIONS + "/" + applicationId);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(200);

        return response.getBody().getObject();
    }

    public void deleteApplication(int applicationId) throws UnirestException {
        HttpRequestWithBody request = Unirest.delete(baseUrl + APPLICATIONS + "?id=" + applicationId)
                .header(TOKEN, loginToken);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(204);

        System.out.println("\n\nApplication " + applicationId + " was deleted");
    }

    public void deleteCandidate(int candidateId) throws UnirestException {
        String path = baseUrl + CANDIDATES + "/" + candidateId;
        HttpRequestWithBody request = Unirest.delete(path)
                .header(TOKEN, loginToken);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(204);

        System.out.println("\n\nCandidate " + candidateId + " was deleted.");
    }

    public JSONObject createCandidate(HashMap<String, String> candidate) throws UnirestException {
        JSONObject candidateJson = new JSONObject(candidate);
        RequestBodyEntity request = Unirest.post(baseUrl + CANDIDATES)
                .header(CONTENT_TYPE, JSON)
                .body(candidateJson);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(201);

        JSONObject responseCandidateJson = response.getBody().getObject();
        System.out.println("\n\nCandidate created: " + responseCandidateJson);

        TestContext.setTestData(CANDIDATES, responseCandidateJson);

        return responseCandidateJson;
    }

    public JSONArray getCandidates() throws UnirestException {
        GetRequest request = Unirest.get(baseUrl + CANDIDATES);
        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isBetween(200, 204);
        JSONArray candidatesJsonArray = response.getBody().getArray();
        return candidatesJsonArray;
    }

    public JSONObject updateCandidate(HashMap<String, String> fieldsToUpdate, int candidateId) throws UnirestException {
        JSONObject fieldsJson = new JSONObject(fieldsToUpdate);
        RequestBodyEntity request = Unirest.put(baseUrl + CANDIDATES + "/" + candidateId)
                .header(CONTENT_TYPE, JSON)
                .header(TOKEN, loginToken)
                .body(fieldsJson);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(200);
//        assertObjectsEqual(response.getBody().getObject(), positionJson);

        System.out.println("\n\nCandidate " + candidateId + " was updated.");

        return response.getBody().getObject();
    }

    public JSONObject getCandidateById(int candidateId) throws UnirestException {
        GetRequest request = Unirest.get(baseUrl + CANDIDATES + "/" + candidateId);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(200);

        return response.getBody().getObject();
    }

    public JSONObject applyCandidateToPosition(JSONObject position, JSONObject candidate) throws UnirestException {
        JSONObject applicationBody = new JSONObject();
        applicationBody.put("candidateID", candidate.get("id"));
        applicationBody.put("positionID", position.get("id"));

        RequestBodyEntity request = Unirest.post(baseUrl + APPLICATIONS)
                .header(TOKEN, loginToken)
                .header(CONTENT_TYPE, JSON)
                .body(applicationBody);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(201);

        JSONObject responseApplicationJson = response.getBody().getObject();
        System.out.println("\n\nCandidate " + candidate.get("id") + " was applyed to position " + position.get("id") + ", application: " + responseApplicationJson);

        TestContext.setTestData(RestWrapper.APPLICATIONS, responseApplicationJson);

        return responseApplicationJson;
    }

    public JSONObject updateApplication(HashMap<String, String> fieldsToUpdate, int applicationId) throws UnirestException {
        JSONObject fieldsToUpdateJson = new JSONObject(fieldsToUpdate);
        RequestBodyEntity request = Unirest.put(baseUrl + RestWrapper.APPLICATIONS + "/" + applicationId)
                .header(TOKEN, loginToken)
                .header(CONTENT_TYPE, JSON)
                .body(fieldsToUpdateJson);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(200);

        System.out.println("\n\nApplication " + applicationId + " was updated back.");

        return response.getBody().getObject();
    }

    public void addResumeToCandidate(int candidateId) throws Exception {
        File resumeFile = TestContext.getResumeFile();
        MultipartBody request = Unirest.post(baseUrl + RestWrapper.CANDIDATES + "/" + candidateId + "/resume")
                .header(TOKEN, loginToken)
                .field("resume", resumeFile);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(201);

        System.out.println("\n\nResume was attached for candidate " + candidateId);
    }

    public boolean isResumeAttached(int candidateId) throws UnirestException {
        GetRequest request = Unirest.get(baseUrl + RestWrapper.CANDIDATES + "/" + candidateId + "/resume");

        HttpResponse<InputStream> respone = request.asBinary();
        assertThat(respone.getStatus()).isEqualTo(200);

        System.out.println("\n\nResume was loaded for candidate " + candidateId);

        return true;
    }

    public void deleteResume(int candidateId) throws UnirestException {
        HttpRequestWithBody request = Unirest.delete(baseUrl + RestWrapper.CANDIDATES + "/" + candidateId + "/resume")
                .header(TOKEN, loginToken);

        HttpResponse<JsonNode> response = request.asJson();
        assertThat(response.getStatus()).isEqualTo(204);

        System.out.println("\n\nResume was deleted from candidate " + candidateId);
    }

    public static void assertObjectsEqual(JSONObject actualObject, JSONObject expectedObject) {
        Set<String> keys = expectedObject.keySet();
        for (String key : keys) {
            assertThat(expectedObject.get(key)
                    .equals(actualObject.get(key)))
                    .isTrue()
                    .overridingErrorMessage("Actual: " + actualObject.get(key) + " is not equal expected: " + expectedObject.get(key));
            assertThat(expectedObject.get(key).getClass()).isEqualTo(actualObject.get(key).getClass());
        }
    }

}
