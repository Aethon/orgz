package com.code42.orgz;

import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.println("USAGE: orgz org_filename users_filename [report_filename]");
            System.exit(1);
            return;
        }

        InputStream orgInputStream;
        try {
            orgInputStream = new FileInputStream(args[0]);
        } catch (FileNotFoundException x) {
            System.out.printf("Could not open %s: %s", args[0], x.getMessage());
            System.exit(1);
            return;
        }

        InputStream usersInputStream;
        try {
            usersInputStream = new FileInputStream(args[1]);
        } catch (FileNotFoundException x) {
            System.out.printf("Could not open %s: %s", args[1], x.getMessage());
            System.exit(1);
            return;
        }

        OrgLoadResult result = OrgFileLoader.Load(orgInputStream, usersInputStream);
        if (result.hasErrors()) {
            result.getErrors().forEach(System.err::println);
            System.exit(1);
            return;
        }

        if (args.length == 3) {
            PrintStream reportStream;
            try {
                reportStream = new PrintStream(args[2]);
            } catch (FileNotFoundException x) {
                System.out.printf("Could not open %s: %s", args[1], x.getMessage());
                System.exit(1);
                return;
            }
            report(reportStream, result.getRoots(), "");
        }
    }

    private static void report(PrintStream stream, List<OrgBean> orgs, String indent) {
        String childIndent = indent + "  ";
        for (OrgBean org : orgs) {
            stream.printf("%s%d,%d,%d,%d", indent, org.getId(), org.getTotalNumUsers(), org.getTotalNumFiles(), org.getTotalNumBytes());
            stream.println();
            report(stream, org.getChildOrgs(), childIndent);
        }
    }
}
