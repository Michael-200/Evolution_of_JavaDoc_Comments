package com.company;

import java.nio.file.Paths;
import java.util.ArrayList;

public class JavaDocSegment {
    public String NameOfCommits;
    public String Signature;
    public String Namespace;
    public String Range;
    public String Date;
    public ArrayList<String> NGrams;
    public String Location;
    public String Content;

    public JavaDocSegment(String Content, ArrayList<String> DescBlockTokens, String Range, String Date, String Signature, String NameOfCommits, String Namespace, String Location) {
        this.NameOfCommits = NameOfCommits;
        this.Content = Content
                .replaceAll("\\u002a", "")
                .replaceAll("/", "")
                .replaceAll("\n", "")
                .replaceAll(" {2}", "")
                .replaceAll("@", " @");
        this.Location = Paths.get(Location).getFileName().toString();
        this.Signature = Signature
                .replaceAll("\n", "")
                .replaceAll(" {2}", "");
        this.Range = Range;
        this.Date = Date;
        this.Namespace = Namespace;
        NGrams = Ngrams.ngrams(DescBlockTokens, 2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Signature);
        sb.append("#");
        sb.append(Namespace);
        sb.append(" (");
        sb.append(Range);
        sb.append(")   ");
        sb.append("    (");
        sb.append(Paths.get(Location).getFileName().toString());
        sb.append(")");
        sb.append("\n\n");
        sb.append(Content);
        sb.append("\n\n----------------------\n\n");
        return sb.toString();
    }
}
