package com.code42.orgz.tests;

import com.code42.orgz.*;
import org.testng.annotations.Test;
import sun.jvm.hotspot.utilities.Assert;

import java.io.InputStream;

public class OrgFileLoaderTest {

    @Test
    public void simpleLoad_succeeds() throws Exception {
        verifyLoad("OrgInput1.data", "UserInput1.data", new OrgExpectation(1, 2, 43, 11400));
    }

    @Test
    public void badInt_throws() throws Exception {
        verifyLoadError("OrgInput2.data", "UserInput2.data", 1);
    }

    public void verifyLoad(String orgResourceName, String usersResourceName, OrgExpectation expectation) {
        InputStream orgs = getClass().getResourceAsStream(orgResourceName);
        InputStream users = getClass().getResourceAsStream(usersResourceName);

        OrgLoadResult result = OrgFileLoader.Load(orgs, users);

        Assert.that(!result.hasErrors(), "should not have errors");
        OrgCollection collection = result.getCollection();
        OrgBean org = collection.getOrg(expectation.getId());
        Assert.that(org != null, "should have expected org ID");
        Assert.that(org.getTotalNumUsers() == expectation.getTotalNumUsers(), "should have expected user count");
        Assert.that(org.getTotalNumFiles() == expectation.getTotalNumFiles(), "should have expected file count");
        Assert.that(org.getTotalNumBytes() == expectation.getTotalNumBytes(), "should have expected byte count");
    }

    public void verifyLoadError(String orgResourceName, String usersResourceName, int errorCount) {
        InputStream orgs = getClass().getResourceAsStream(orgResourceName);
        InputStream users = getClass().getResourceAsStream(usersResourceName);

        OrgLoadResult result = OrgFileLoader.Load(orgs, users);

        Assert.that(result.hasErrors(), "should have errors");
        Assert.that(result.getErrors().size() == errorCount, "should have the expected count of errors");
    }
}