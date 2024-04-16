package org.blitzstriker.util;

import org.apache.commons.io.IOUtils;
import org.blitzstriker.tests.vendorportal.model.VendorPortalTestData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Demo {

    public static void main(String[] args) throws IOException {
//        InputStream stream = ResourceLoader.getResource("dummy.txt");
//        String content = IOUtils.toString(stream, StandardCharsets.UTF_8);
//
//        System.out.println(content);
//        VendorPortalTestData testData = JsonUtil.getTestData("test-data/vendor-portal/john.json", VendorPortalTestData.class);
//        assert testData != null;
//        System.out.println(testData.username());
        System.setProperty("browser", "firefox");
        Config.initialize();
    }
}
