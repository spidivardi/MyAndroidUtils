package com.il.tikkun.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * j
 * Created by David vardi on 7/12/2016.
 */
public class PDFReader {

    private String mUri;

    private onPDFReaderListener mListener;

    private Context mContext;

    private String mPath;

    private static final String GOOGLE_DOCS_URL = "https://docs.google.com/gview?embedded=true&url=";

    public PDFReader(Context mContext) {

        this.mContext = mContext;
    }

    public Boolean showPDFFromURLDownload(String fileUrl, String folderName, String fileName, onPDFReaderListener listener) {

        mListener = listener;

        mUri = fileUrl;

        mPath = "/" + folderName + "/" + fileName + ".pdf";

        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

        File folder = new File(extStorageDirectory, folderName);

        folder.mkdir();

        final File file = new File(folder, fileName + ".pdf");

        try {
            if (file.createNewFile()) {

                new Download().execute(file);

                return true;

            } else {

                return false;

            }
        } catch (IOException e) {

            e.printStackTrace();

            return false;

        }


    }

    private class Download extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {

            return downloadFile(mUri, (File) params[0]);

        }

        @Override
        protected void onPostExecute(Boolean finish) {

            mListener.onTaskComplete(finish);

            if (finish) {

                startIntentPDF();

            }
        }
    }

    private Boolean downloadFile(String fileURL, File directory) {

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(directory);

            URL url = new URL(fileURL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setDoOutput(true);

            connection.connect();

            InputStream in = connection.getInputStream();

            byte[] buffer = new byte[1024];

            int len1;

            while ((len1 = in.read(buffer)) > 0) {

                fileOutputStream.write(buffer, 0, len1);

            }

            fileOutputStream.close();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }

    }

    private void startIntentPDF() {

        File file = new File(Environment.getExternalStorageDirectory() + mPath);

        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_VIEW);

        intent.setDataAndType(Uri.fromFile(file), "application/pdf");

        mContext.startActivity(intent);
    }


    public void openWebPDF(WebView webView, String link) {

        webView.setWebViewClient(new MyBrowser(link));

        webView.getSettings().setLoadsImagesAutomatically(true);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + link);
    }


    private class MyBrowser extends WebViewClient {

        String mLink;

        public MyBrowser(String link) {

            this.mLink = link;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.contains(GOOGLE_DOCS_URL + mLink)) {

                view.loadUrl(url);

            } else {

                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                mContext.startActivity(i);
            }
            return true;
        }
    }

    public interface onPDFReaderListener {

        void onTaskComplete(Boolean complete);
    }
}
