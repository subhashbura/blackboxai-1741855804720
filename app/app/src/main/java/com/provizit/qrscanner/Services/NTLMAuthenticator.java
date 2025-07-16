package com.provizit.qrscanner.Services;

// Add JCIFS dependency to your build.gradle
// implementation 'jcifs:jcifs:1.3.18' (check for latest version)

import android.util.Base64;

import java.io.IOException;
import java.util.List;

import jcifs.ntlmssp.Type2Message;
import jcifs.ntlmssp.Type3Message;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class NTLMAuthenticator implements Authenticator {
    private String username;
    private String password;
    private String domain;

    public NTLMAuthenticator(String username, String password, String domain) {
        this.username = username;
        this.password = password;
        this.domain = domain;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        List<String> wwwAuthenticate = response.headers("WWW-Authenticate");
        if (wwwAuthenticate != null) {
            for (String header : wwwAuthenticate) {
                if (header.startsWith("NTLM")) {
                    try {
                        // Step 1: Client sends Type 1 message (Negotiate)
                        Type1Message type1 = new Type1Message(Type1Message.DEFAULT_FLAGS, domain, "");
                        String type1Msg = "NTLM " + Base64.encodeBytes(type1.toByteArray());

                        // Create a new request with the Type 1 message in the Authorization header
                        Request type1Request = response.request().newBuilder()
                                .header("Authorization", type1Msg)
                                .build();

                        Response type1Response = new OkHttpClient.Builder()
                                .build()
                                .newCall(type1Request)
                                .execute();

                        // Step 2: Server sends Type 2 message (Challenge)
                        String type2Header = type1Response.header("WWW-Authenticate");
                        if (type2Header != null && type2Header.startsWith("NTLM ")) {
                            byte[] type2Bytes;
                            type2Bytes = Base64.decode(type2Header.substring(5));
                            Type2Message type2 = new Type2Message(type2Bytes);

                            // Step 3: Client sends Type 3 message (Authenticate)
                            Type3Message type3 = new Type3Message(type2, username, password, domain, "");
                            String type3Msg = "NTLM " + Base64.encodeBytes(type3.toByteArray());

                            return response.request().newBuilder()
                                    .header("Authorization", type3Msg)
                                    .build();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null; // No NTLM header found or authentication failed
    }
}

// Then, when building your OkHttpClient:
// OkHttpClient okHttpClient = new OkHttpClient.Builder()
//         .authenticator(new NTLMAuthenticator(login, password, domain))
//         .build();
