/*
 *    Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.downloader.httpclient;

import com.downloader.request.DownloadRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

/**
 * Created by amitshekhar on 13/11/17.
 */

public class DefaultHttpClient implements HttpClient {

    private URLConnection connection;

    public DefaultHttpClient() {

    }

    @Override
    public void connect(DownloadRequest request) throws IOException {
        connection = new URL(request.getUrl()).openConnection();
        connection.setReadTimeout(request.getReadTimeout());
        connection.setConnectTimeout(request.getConnectTimeout());
        if (request.getDownloadedBytes() != 0) {
            final String range = String.format(Locale.ENGLISH,
                    "bytes=%d-", request.getDownloadedBytes());
            connection.addRequestProperty("Range", range);
        }
        connection.connect();
    }

    @Override
    public int getResponseCode() throws IOException {
        int responseCode = 0;
        if (connection instanceof HttpURLConnection) {
            responseCode = ((HttpURLConnection) connection).getResponseCode();
        }
        return responseCode;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return connection.getInputStream();
    }

    @Override
    public long getContentLength() {
        String length = connection.getHeaderField("Content-Length");
        try {
            return Long.parseLong(length);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public String getResponseHeaderForKey(String key) {
        return connection.getHeaderField(key);
    }

    @Override
    public void close() {
        // no operation
    }
}
