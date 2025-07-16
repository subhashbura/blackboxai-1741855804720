package com.provizit.qrscanner.Services;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class EwsClient {

    private static final String EWS_URL = "https://ews.provizit.com/EWS/Exchange.asmx";
    private static final String USERNAME = "serviceuser@provizit.com";
    private static final String PASSWORD = "Skhan@123";

    public void fetchInboxEmails() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .authenticator((route, response) -> {
                    String credential = Credentials.basic(USERNAME, PASSWORD);
                    return response.request().newBuilder()
                            .header("Authorization", credential)
                            .build();
                })
                .build();

//        String soapRequest = getSoapRequest();

        String soapRequest = getGetItemRequest("AAMkADA1ZTdiNGNmLTUyYTctNDJkMC04MWM0LTFhYzUzOWZlMDMyMQBGAAAAAAAfvwzaCRsKTZVmGBjM4qYmBwDouUJ8HWoTQbBhBzQTjPtbAAAAAAENAADouUJ8HWoTQbBhBzQTjPtbAABu2ppqAAA=","DwAAABYAAADouUJ8HWoTQbBhBzQTjPtbAABu2Xgu");
        Request request = new Request.Builder()
                .url(EWS_URL)
                .addHeader("Content-Type", "text/xml; charset=utf-8")
                .addHeader("Accept", "text/xml")
                .addHeader("User-Agent", "Android EWS Client")
                .addHeader("SOAPAction", "http://schemas.microsoft.com/exchange/services/2006/messages/FindItem")
                .post(RequestBody.create(okhttp3.MediaType.parse("text/xml"), soapRequest))
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseXml = response.body().string();
                    System.out.println("EWS Response: " + responseXml);

                    // You can parse XML here to extract subjects, etc.
                } else {
                    System.err.println("EWS Error: " + response.code());
                }
            }
        });
    }

    private String getSoapRequestInbox() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "               xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\">\n" +
                "  <soap:Header>\n" +
                "    <t:RequestServerVersion Version=\"Exchange2010_SP2\"/>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <FindItem xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\"\n" +
                "              Traversal=\"Shallow\">\n" +
                "      <ItemShape>\n" +
                "        <t:BaseShape>IdOnly</t:BaseShape>\n" +
                "        <t:AdditionalProperties>\n" +
                "          <t:FieldURI FieldURI=\"item:Subject\"/>\n" +
                "        </t:AdditionalProperties>\n" +
                "      </ItemShape>\n" +
                "      <ParentFolderIds>\n" +
                "        <t:DistinguishedFolderId Id=\"inbox\"/>\n" +
                "      </ParentFolderIds>\n" +
                "    </FindItem>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
    }
    public String getGetItemRequest(String itemId, String changeKey) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "               xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\">\n" +
                "  <soap:Header>\n" +
                "    <t:RequestServerVersion Version=\"Exchange2010_SP2\"/>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <GetItem xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\">\n" +
                "      <ItemShape>\n" +
                "        <t:BaseShape>Default</t:BaseShape>\n" +
                "        <t:AdditionalProperties>\n" +
                "          <t:FieldURI FieldURI=\"calendar:RequiredAttendees\"/>\n" +
                "          <t:FieldURI FieldURI=\"calendar:OptionalAttendees\"/>\n" +
                "          <t:FieldURI FieldURI=\"calendar:Resources\"/>\n" +
                "        </t:AdditionalProperties>\n" +
                "      </ItemShape>\n" +
                "      <ItemIds>\n" +
                "        <t:ItemId Id=\"" + itemId + "\" ChangeKey=\"" + changeKey + "\"/>\n" +
                "      </ItemIds>\n" +
                "    </GetItem>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
    }

    private String getSoapRequest() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "               xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\">\n" +
                "  <soap:Header>\n" +
                "    <t:RequestServerVersion Version=\"Exchange2010_SP2\"/>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <FindItem xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\"\n" +
                "              Traversal=\"Shallow\">\n" +
                "      <ItemShape>\n" +
                "        <t:BaseShape>Default</t:BaseShape>\n" +
                "        <t:AdditionalProperties>\n" +
                "          <t:FieldURI FieldURI=\"calendar:RequiredAttendees\"/>\n" +
                "          <t:FieldURI FieldURI=\"calendar:OptionalAttendees\"/>\n" +
                "          <t:FieldURI FieldURI=\"calendar:Resources\"/>\n" +
                "        </t:AdditionalProperties>\n" +
                "      </ItemShape>\n" +
                "      <CalendarView StartDate=\"2025-07-01T00:00:00+05:30\" EndDate=\"2025-07-31T23:59:59+05:30\"/>\n" +
                "      <ParentFolderIds>\n" +
                "        <t:DistinguishedFolderId Id=\"calendar\">\n" +
                "          <t:Mailbox>\n" +
                "            <t:EmailAddress>kmurali@provizit.com</t:EmailAddress>\n" +
                "          </t:Mailbox>\n" +
                "        </t:DistinguishedFolderId>\n" +
                "      </ParentFolderIds>\n" +
                "    </FindItem>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
    }



}
