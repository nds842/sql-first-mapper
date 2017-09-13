package com.github.nds842.sqlfirst;


import com.github.nds842.sqlfirst.base.MiscUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MiscUtilsTest {

    private static final String TEST_STRING = "testString";
    private static final String TEST_STRING_WITH_DOT = "test_String.with.dot";


    @Test
    public void test() {
        Assert.assertEquals(MiscUtils.getLastWordAfterDot(TEST_STRING), TEST_STRING);
        Assert.assertEquals(MiscUtils.getLastWordAfterDot(TEST_STRING_WITH_DOT), "dot");
        Assert.assertEquals(MiscUtils.prepareJavadoc(TEST_STRING, 0), "/**\n * testString\n */");
        Assert.assertEquals(MiscUtils.prepareJavadoc(TEST_STRING, 2), "  /**\n   * testString\n   */");
        Assert.assertEquals(MiscUtils.underscores(TEST_STRING), "TEST_STRING");
        Assert.assertEquals(MiscUtils.underscores(TEST_STRING_WITH_DOT), "TEST_STRING_WITH_DOT");
        Assert.assertEquals(MiscUtils.prepareNameString(TEST_STRING), TEST_STRING);
        Assert.assertEquals(MiscUtils.prepareNameString(TEST_STRING_WITH_DOT), "testStringWithDot");
    }
}
