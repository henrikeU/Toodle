package com.example.louis_edouard.toodle;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louis_edouard.toodle.moodle.CourseContent;
import com.example.louis_edouard.toodle.moodle.CourseModule;
import com.example.louis_edouard.toodle.moodle.CourseModuleContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CoursFichFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    TextView prof, demo, theoDys, tpDys;
    Button btnPlanCours;
    List<CourseContent> courseContents;
    private String fileUrl, fileName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_cours_fich,container,false);

        prof = (TextView)v.findViewById(R.id.txt_frag_cours_fich_prof);
        demo = (TextView)v.findViewById(R.id.txt_frag_cours_fich_demo);
        theoDys = (TextView)v.findViewById(R.id.txt_frag_cours_fich_theoDys);
        tpDys = (TextView)v.findViewById(R.id.txt_frag_cours_fich_tpDys);
        btnPlanCours = (Button)v.findViewById(R.id.btn_plan_cours);
        btnPlanCours.setOnClickListener(this);

        RunAPI run = new RunAPI();
        run.execute();
        return  v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        fileName = "";
        fileUrl = "";
        List<CourseModule> modules = courseContents.get(0).modules;
        for(CourseModule module : modules){
            if(module.modname.equals("resource")) {
                for (CourseModuleContent content : module.contents) {
                    if (content.type.equals("file")) {
                        fileName = content.filename;
                        fileUrl = content.fileurl;
                        break;
                    }
                }
            }
        }
        fileUrl += "&token=" + CoursContentActivity.USER_TOKEN;

        if(Globals.isExternalStorageWritable())
            new DownloadFile().execute(fileUrl, fileName);
        else {
            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setTitle(getResources().getString(R.string.alert_title_SDcard_missing));
            dialog.setMessage(getResources().getString(R.string.alert_message_SDcard_missing));
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/toodle/" + "plandecours.pdf");
            Uri path = Uri.fromFile(pdfFile);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                dialog.setTitle(getResources().getString(R.string.alert_title_PDFreader_missing));
                dialog.setMessage(getResources().getString(R.string.alert_message_PDFreader_missing));
                dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            String folderName = "toodle";

            File folder = new File(Environment.getExternalStorageDirectory(), folderName);
            if (!folder.exists()) {
                if (folder.mkdirs()) {
                    File pdfFile = new File(folder, fileName);
                    try {
                        pdfFile.createNewFile();
                        Globals.downloadFile(fileUrl, pdfFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    private class RunAPI extends AsyncTask<String, Object, List<CourseContent>> {

        @Override
        protected List<CourseContent> doInBackground(String... params) {
            WebAPI web = new WebAPI(CoursContentActivity.USER_TOKEN);
            try {
                courseContents = web.getCourseContent(CoursContentActivity.COURSE_ID);
            }
            catch(IOException e){ }

            return courseContents;
        }

        @Override
        protected void onPostExecute(List<CourseContent> courseContents){
            super.onPostExecute(courseContents);

            String html = courseContents.get(0).summary;
            Document doc = Jsoup.parseBodyFragment(html);
            Element teacher  = doc.getElementsByClass("teacher").first();
            Element demonstrator  = doc.getElementsByClass("demonstrator").first();
            Elements theories = doc.getElementsByClass("theorie");
            Elements tps = doc.getElementsByClass("horaire-tp");

            String horaireTheorie = "";
            String horaireTp = "";
            for(Element horaire : theories)
                horaireTheorie += horaire.text() + "\n";
            for(Element horaire: tps)
                horaireTp += horaire.text() + "\n";

            prof.setText(teacher.text());
            demo.setText(demonstrator.text());
            theoDys.setText(horaireTheorie.substring(0, horaireTheorie.length() - 1));
            tpDys.setText(horaireTp.substring(0, horaireTp.length() - 1));
        }

    }
}
