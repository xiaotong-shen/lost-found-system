package data_access;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import entity.User;
import entity.UserFactory;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * The DAO for user data.
 */
public class DBUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface {
    private static final int SUCCESS_CODE = 200;
    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String STATUS_CODE_LABEL = "status_code";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MESSAGE = "message";
    private final UserFactory userFactory;

    public DBUserDataAccessObject(UserFactory userFactory) {
        this.userFactory = userFactory;
        System.out.println("DEBUG: DBUserDataAccessObject initialized with userFactory: " + userFactory.getClass().getSimpleName());
        // No need to do anything to reinitialize a user list! The data is the cloud that may be miles away.
    }

    @Override
    public User get(String username) {
        System.out.println("\n=== DEBUG: get() method called ===");
        System.out.println("DEBUG: Requesting user with username: '" + username + "'");
        
        // Make an API call to get the user object.
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        final String url = String.format("http://vm003.teach.cs.toronto.edu:20112/user?username=%s", username);
        System.out.println("DEBUG: Making API call to URL: " + url);
        
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", CONTENT_TYPE_JSON)
                .build();
        try {
            final Response response = client.newCall(request).execute();
            final String responseBodyString = response.body().string();
            
            System.out.println("DEBUG: API Response Code: " + response.code());
            System.out.println("DEBUG: API Response Body: " + responseBodyString);

            final JSONObject responseBody = new JSONObject(responseBodyString);

            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                final JSONObject userJSONObject = responseBody.getJSONObject("user");
                final String name = userJSONObject.getString(USERNAME);
                final String password = userJSONObject.getString(PASSWORD);

                System.out.println("DEBUG: Successfully retrieved user:");
                System.out.println("DEBUG:   - Name: '" + name + "'");
                System.out.println("DEBUG:   - Password: '" + password + "'");
                System.out.println("DEBUG:   - Password length: " + password.length());
                
                User user = userFactory.create(name, password);
                System.out.println("DEBUG: Created user object: " + user.getName());
                return user;
            }
            else {
                String errorMessage = responseBody.getString(MESSAGE);
                System.out.println("DEBUG: API returned error: " + errorMessage);
                throw new RuntimeException(errorMessage);
            }
        }
        catch (IOException | JSONException ex) {
            System.out.println("DEBUG: Exception in get(): " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setCurrentUsername(String name) {
        System.out.println("DEBUG: setCurrentUsername called with: '" + name + "'");
        // this isn't implemented for the lab
    }

    @Override
    public boolean existsByName(String username) {
        System.out.println("\n=== DEBUG: existsByName() method called ===");
        System.out.println("DEBUG: Checking if user exists: '" + username + "'");
        
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final String url = String.format("http://vm003.teach.cs.toronto.edu:20112/checkIfUserExists?username=%s", username);
        System.out.println("DEBUG: Making API call to URL: " + url);
        
        final Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try {
            final Response response = client.newCall(request).execute();
            final String responseBodyString = response.body().string();
            
            System.out.println("DEBUG: API Response Code: " + response.code());
            System.out.println("DEBUG: API Response Body: " + responseBodyString);

            final JSONObject responseBody = new JSONObject(responseBodyString);
            final boolean exists = responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE;
            
            System.out.println("DEBUG: User exists: " + exists);
            return exists;
        }
        catch (IOException | JSONException ex) {
            System.out.println("DEBUG: Exception in existsByName(): " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void save(User user) {
        System.out.println("\n=== DEBUG: save() method called ===");
        System.out.println("DEBUG: Saving user: '" + user.getName() + "'");
        System.out.println("DEBUG: Password length: " + user.getPassword().length());
        
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        // POST METHOD
        final MediaType mediaType = MediaType.parse(CONTENT_TYPE_JSON);
        final JSONObject requestBody = new JSONObject();
        requestBody.put(USERNAME, user.getName());
        requestBody.put(PASSWORD, user.getPassword());
        final String requestBodyString = requestBody.toString();
        
        System.out.println("DEBUG: Request body: " + requestBodyString);
        
        final RequestBody body = RequestBody.create(requestBodyString, mediaType);
        final Request request = new Request.Builder()
                .url("http://vm003.teach.cs.toronto.edu:20112/user")
                .method("POST", body)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try {
            System.out.println("DEBUG: Making POST request to save user");
            final Response response = client.newCall(request).execute();
            final String responseBodyString = response.body().string();
            
            System.out.println("DEBUG: API Response Code: " + response.code());
            System.out.println("DEBUG: API Response Body: " + responseBodyString);

            final JSONObject responseBody = new JSONObject(responseBodyString);

            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                System.out.println("DEBUG: User saved successfully!");
            }
            else {
                String errorMessage = responseBody.getString(MESSAGE);
                System.out.println("DEBUG: API returned error: " + errorMessage);
                throw new RuntimeException(errorMessage);
            }
        }
        catch (IOException | JSONException ex) {
            System.out.println("DEBUG: Exception in save(): " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void changePassword(User user) {
        System.out.println("\n=== DEBUG: changePassword() method called ===");
        System.out.println("DEBUG: Changing password for user: '" + user.getName() + "'");
        System.out.println("DEBUG: New password length: " + user.getPassword().length());
        
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        // PUT METHOD
        final MediaType mediaType = MediaType.parse(CONTENT_TYPE_JSON);
        final JSONObject requestBody = new JSONObject();
        requestBody.put(USERNAME, user.getName());
        requestBody.put(PASSWORD, user.getPassword());
        final String requestBodyString = requestBody.toString();
        
        System.out.println("DEBUG: Request body: " + requestBodyString);
        
        final RequestBody body = RequestBody.create(requestBodyString, mediaType);
        final Request request = new Request.Builder()
                .url("http://vm003.teach.cs.toronto.edu:20112/user")
                .method("PUT", body)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try {
            System.out.println("DEBUG: Making PUT request to change password");
            final Response response = client.newCall(request).execute();
            final String responseBodyString = response.body().string();
            
            System.out.println("DEBUG: API Response Code: " + response.code());
            System.out.println("DEBUG: API Response Body: " + responseBodyString);

            final JSONObject responseBody = new JSONObject(responseBodyString);

            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                System.out.println("DEBUG: Password changed successfully!");
            }
            else {
                String errorMessage = responseBody.getString(MESSAGE);
                System.out.println("DEBUG: API returned error: " + errorMessage);
                throw new RuntimeException(errorMessage);
            }
        }
        catch (IOException | JSONException ex) {
            System.out.println("DEBUG: Exception in changePassword(): " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getCurrentUsername() {
        System.out.println("DEBUG: getCurrentUsername() called - returning null");
        return null;
    }
}
