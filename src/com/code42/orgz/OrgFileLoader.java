package com.code42.orgz;

import java.io.InputStream;
import java.util.*;

public final class OrgFileLoader {
    public final static String NULL_STRING = "null";

    public static OrgLoadResult Load(InputStream orgStream, InputStream userStream) throws NullPointerException {
        Map<Integer, InMemoryOrgBean> orgs = new HashMap<>();

        if (orgStream == null)
            throw new NullPointerException("orgStream");
        if (userStream == null)
            throw new NullPointerException("userStream");

        List<String> errors = new ArrayList<>();

        // load orgs
        Scanner scanner = new Scanner(orgStream);
        int lineNo = 0;
        while (scanner.hasNextLine()) {
            lineNo++;
            String line = scanner.nextLine();
            String[] parts = line.split("\\s*,\\s*");
            if (parts.length != 3) {
                errors.add(String.format("Organization file, line %d has %d items; three are expected: org ID, parent org ID (or null), and name.",
                        lineNo, parts.length));
                continue;
            }

            int initialErrorCount = errors.size();
            int orgId = parseOrReport(parts[0], lineNo, errors, "Organization", "org ID");
            Integer parentId = (parts[1].equals(NULL_STRING))
                    ? null
                    : parseOrReport(parts[1], lineNo, errors, "Organization", "parent org ID");

            // name is not examined, since it plays not part

            if (errors.size() > initialErrorCount)
                continue;

            if (orgs.getOrDefault(orgId, null) != null) {
                errors.add(String.format("Organization file, line %d: org ID %d has already been provided", lineNo, orgId));
            } else {
                orgs.put(orgId, new InMemoryOrgBean(orgId, parentId));
            }
        }
        scanner.close();

        // load users and add to orgs
        scanner = new Scanner(userStream);
        lineNo = 0;
        while (scanner.hasNextLine()) {
            lineNo++;
            String line = scanner.nextLine();
            String[] parts = line.split("\\s*,\\s*");
            if (parts.length != 4) {
                errors.add(String.format("Users file, line %d has %d items; four are expected: user ID, org ID, file count, and byte count.",
                        lineNo, parts.length));
                continue;
            }

            int initialErrorCount = errors.size();
            // user ID is not examined here; there are no specific requirements listed

            int orgId = parseOrReport(parts[1], lineNo, errors, "Users", "org ID");
            int numFiles = parseOrReport(parts[2], lineNo, errors, "Users", "file count");
            int numBytes = parseOrReport(parts[3], lineNo, errors, "Users", "byte count");

            if (errors.size() > initialErrorCount)
                continue;

            InMemoryOrgBean org = orgs.getOrDefault(orgId, null);
            if (org == null) {
                errors.add(String.format("Users file, line %d: org ID %d was not found", lineNo, orgId));
                continue;
            }

            org.applyUser(numFiles, numBytes);
        }
        scanner.close();

        // build org hierarchy
        List<InMemoryOrgBean> roots = new ArrayList<>();

        for (InMemoryOrgBean org : orgs.values()) {
            Integer parentId = org.getParentId();
            if (parentId == null) {
                roots.add(org);
            } else {
                InMemoryOrgBean parent = orgs.getOrDefault(parentId, null);
                if (parent == null) {
                    errors.add(String.format("Org ID %d (referenced as parent of org ID %d) was not found", parentId, org.getId()));
                }else {
                    parent.applyChild(org);
                }
            }
        }

        // finalize and optimize
        for (InMemoryOrgBean root : roots) {
            root.freeze(errors);
        }

        return new OrgLoadResult(new InMemoryOrgCollection(new ArrayList(roots)), new ArrayList<>(roots), errors);
    }


    private static int parseOrReport(String input, int lineNo, List<String> errors, String file, String part) {
        int result = 0;
        try {
            result = Integer.decode(input);
            if (result < 0)
                errors.add(String.format("%s file, line %d: %s was negative (%d); should be 0 or positive", file, lineNo, part, result));

        } catch (NumberFormatException x) {
            errors.add(String.format("%s file, line %d: %s '%s' is not parsable; should be an integer", file, lineNo, part, input));
        }
        return result;
    }
}
