package david.vardi.com.exifinformationdemo;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 * David Vardi  === Update 05/09/2017 
 */
class DownloadHelper {

    private static final String TAG = DownloadHelper.class.getSimpleName();

    private ProgressDialog mDialog;

    private Context mContext;

    private String folderName;

    private File mFile;

    private DownloadFileListener mListener;

    DownloadHelper(Context context, DownloadFileListener listener) {

        this.mContext = context;

        this.mListener = listener;

    }


    /**
     * @param url        The link of the file
     * @param folderName If is null the file will be saved on cache folder
     */
    void downloadFile(String url, String folderName) {

        this.folderName = folderName;

        new DownloadFileFromURL().execute(url);

    }


    private class DownloadFileFromURL extends AsyncTask<String, String, String> {

        protected void onPreExecute() {

            super.onPreExecute();

            showDialog();
        }


        @Override
        protected String doInBackground(String... params) {

            int count;

            File folder;

            try {

                URL url = new URL(params[0]);

                if (folderName != null) {

                    folder = new File(Environment.getExternalStorageDirectory().toString(), folderName);

                    if (!folder.exists()) {

                        folder.mkdirs();
                    }
                } else {

                    folder = mContext.getCacheDir();
                }

                mFile = new File(folder, url.getPath().substring(url.getPath().lastIndexOf("/")));

                URLConnection conection = url.openConnection();

                conection.connect();

                int lenghtOfFile = conection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(), 1024);

                OutputStream output = new FileOutputStream(mFile);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {

                    total += count;

                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    output.write(data, 0, count);
                }

                output.flush();

                output.close();

                input.close();

            } catch (Exception e) {

                Log.e(TAG, "Error: " + e.getMessage());


            }

            return null;
        }


        protected void onProgressUpdate(String... progress) {

            mDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String url) {

            dismissDialog();

            mListener.onPostExecute(mFile);


        }

    }

    private void dismissDialog() {

        mDialog.dismiss();

    }


    private void showDialog() {

        mDialog = new ProgressDialog(mContext);

        mDialog.setMessage("Downloading file. Please wait...");

        mDialog.setIndeterminate(false);

        mDialog.setMax(100);

        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        mDialog.setCancelable(true);

        mDialog.show();

    }

    interface DownloadFileListener {

        void onPostExecute(File file);
    }

}
