package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Connect {

    public static Thread current;

    public static List<String> arraylistOfCommits = new ArrayList();
    public static List<String> arraylistOfDate = new ArrayList();
    public static File out;
    public static File fileToDelete;

    private boolean isFirst = false;

    protected void Connect(String newString, String link, String args) throws Exception, ConnectException {

        HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(newString)).openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

        String line;
        String inputLine;
        for (line = ""; (inputLine = bufferedReader.readLine()) != null; line = line + "\n" + inputLine) {
        }

        bufferedReader.close();
        Arrays.stream(line.split("\\[\\{\"sha\":\"")).skip(1L).map((L) -> {
            return L.split("\"")[0];
        }).forEach((L) -> {
            if (!isFirst) {
                arraylistOfCommits.add(L);
                isFirst = true;
            }
        });
        Arrays.stream(line.split("]},\\{\"sha\":\"")).skip(1L).map((L) -> {
            return L.split("\"")[0];
        }).forEach((L) -> {
            if (!arraylistOfCommits.contains(L)) {
                arraylistOfCommits.add(L);
            }
        });
        Arrays.stream(line.split("\"date\":\"")).skip(1L).map((G) -> {
            return G.split("\"")[0];
        }).forEach((G) -> {
            if (!arraylistOfDate.contains(G)) {
                String time = G.replace("T", " ").replace("Z", "");
                arraylistOfDate.add(time);
            }
        });

        for (String downloaded : CheckForDownloadedData.arrayOfDownloadedFiles) {
            for (String commits : arraylistOfCommits) {
                if (downloaded == commits) {
                    arraylistOfDate.remove(arraylistOfCommits.indexOf(commits));
                    arraylistOfCommits.remove(commits);
                    CheckForDownloadedData.arrayOfDownloadedFiles.remove(downloaded);
                }
            }
        }


        while (0 < arraylistOfCommits.size()) {
            String links = link + "/archive/" + arraylistOfCommits.get(0) + ".zip";
            out = new File(Main.pathToFile + Main.folderName + "\\" + arraylistOfCommits.get(0) + ".zip");
            out.deleteOnExit();
            current = new Thread(new Download(links, out));
            current.start();
            current.join();
            DeleteDirectory.isSafe = false;
            DeleteDirectory.DeleteDirectory(Main.pathToFile + FolderCreate.folder.getName() + UnZip.arraylist.get(0), args);
            DeleteDirectory.isSafe = true;
            MainOfAnalyze mainOfAnalyze = new MainOfAnalyze();
            mainOfAnalyze.mainOfAnalyze(UnZip.arraylist.get(0));
            fileToDelete = new File(FolderCreate.folder + UnZip.arraylist.get(0));
            ZipFile.Zip();
            ZipFile.jsonToDelete.delete();
            while (fileToDelete.exists()) {
                DeleteDirectory.DeleteDirectory(Main.pathToFile + FolderCreate.folder.getName() + UnZip.arraylist.get(0), args);
            }
            if (ZipFile.zipToDelete.length() < 400) {
                ZipFile.zipToDelete.delete();
                while (ZipFile.zipToDelete.exists()) {
                    Thread.sleep(500);
                    ZipFile.zipToDelete.delete();
                }
            }
            UnZip.arraylist.remove(0);
            arraylistOfDate.remove(0);
            arraylistOfCommits.remove(0);
        }
    }
}