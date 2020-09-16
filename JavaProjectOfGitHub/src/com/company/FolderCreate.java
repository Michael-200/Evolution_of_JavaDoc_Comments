package com.company;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderCreate {

    protected static String folderName;
    protected static File folder;
    private static int count = 2;
    public static boolean isCreated = false;

    protected FolderCreate() {

        folderName = "Project Folder(1)";


        for (Path path = Paths.get(Main.PuthToFile + folderName); Files.exists(path, new LinkOption[0]); ++count) {

            folderName = "Project Folder(" + count + ")";
            path = Paths.get(Main.PuthToFile + folderName);
        }

        folder = new File(Main.PuthToFile + folderName);

        folder.mkdir();
    }

    private FolderCreate(String folderName) {
        FolderCreate.folderName = folderName;
    }
}