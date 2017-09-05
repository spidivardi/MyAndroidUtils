package david.vardi.com.exifinformationdemo;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;


public class ShowPdfFragment extends Fragment implements DownloadHelper.DownloadFileListener {


    private static final String LINK_KEY = "LINK_KEY";

    private PDFViewPager mPdfViewPager;

    private PDFPagerAdapter mAdapter;

    private String mLink;


    public static ShowPdfFragment newInstance(String link) {

        ShowPdfFragment fragment = new ShowPdfFragment();

        Bundle bundle = new Bundle();

        bundle.putString(LINK_KEY, link);

        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        init(view);

        return view;
    }

    private void init(View view) {

        initVar(view);

        if(isStoragePermissionGranted()){

            downLoadFile();
        }

    }

    private void downLoadFile() {

        DownloadHelper helper = new DownloadHelper(getContext(), this);

        helper.downloadFile(mLink, null);
    }



    private void initVar(View view) {

        mPdfViewPager = (PDFViewPager) view.findViewById(R.id.pdfViewPager);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mAdapter.close();
    }

    public boolean isStoragePermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {

            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                return true;

            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                return false;
            }
        } else {

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            downLoadFile();

        }
    }


    @Override
    public void onPostExecute(File file) {

        mAdapter = new PDFPagerAdapter(getContext(), file.getAbsolutePath());

        mPdfViewPager.setAdapter(mAdapter);
    }
}
