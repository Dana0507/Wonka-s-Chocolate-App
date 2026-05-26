package com.example.wonka;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public abstract class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final Response.Listener<NetworkResponse> mListener;
    private final String boundary = "apiclient-" + System.currentTimeMillis();

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // Text params
            Map<String, String> params = getParams();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    bos.write(("--" + boundary + "\r\n").getBytes());
                    bos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n").getBytes());
                    bos.write((entry.getValue() + "\r\n").getBytes());
                }
            }

            // File params
            Map<String, DataPart> fileParams = getByteData();
            if (fileParams != null) {
                for (Map.Entry<String, DataPart> entry : fileParams.entrySet()) {
                    DataPart file = entry.getValue();
                    bos.write(("--" + boundary + "\r\n").getBytes());
                    bos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"; filename=\"" + file.getFileName() + "\"\r\n").getBytes());
                    bos.write(("Content-Type: image/jpeg\r\n\r\n").getBytes());
                    bos.write(file.getContent());
                    bos.write("\r\n".getBytes());
                }
            }

            bos.write(("--" + boundary + "--\r\n").getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bos.toByteArray();
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    public abstract Map<String, String> getParams() throws AuthFailureError;
    public abstract Map<String, DataPart> getByteData() throws AuthFailureError;

    public static class DataPart {
        private final String fileName;
        private final byte[] content;

        public DataPart(String fileName, byte[] content) {
            this.fileName = fileName;
            this.content = content;
        }

        public String getFileName() { return fileName; }
        public byte[] getContent() { return content; }
    }
}