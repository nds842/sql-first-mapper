package com.github.nds842.sqlfirst.base;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

public class TemplateUtils {
    
    private static VelocityEngine velocityEngine;
    
    private TemplateUtils() {
    }
    
    public static String prepareTemplate(String query, Map<String, Object> queryParamMap) {
        if (velocityEngine == null) {
            initVelocity();
        }
        VelocityContext ctx = new VelocityContext();
        queryParamMap.forEach(ctx::put);
        StringWriter writer = new StringWriter();
        velocityEngine.evaluate(ctx, writer, "prepareQueryWithNamedParameters", query);
        return writer.toString();
    }
    
    private static void initVelocity() {
        VelocityEngine velocityEngineLocal = new VelocityEngine();
        velocityEngineLocal.setProperty("input.encoding", MiscUtils.UTF_8);
        velocityEngineLocal.setProperty("output.encoding", MiscUtils.UTF_8);
        
        try {
            velocityEngineLocal.init();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
        velocityEngine = velocityEngineLocal;
    }
    
    
    /**
     * Get velocity template by query name and package name, reads file from resources and returns as string
     *
     * @param packageName name of package
     * @param queryName name of query
     * @return string with velocity template file contents
     */
    public static String getTemplate(String packageName, String queryName) {
        String resourceName = packageName.replace(".", "/") + "/sql/" + queryName + ".sql";
        InputStream inputStream = TemplateUtils.class.getClassLoader().getResourceAsStream(resourceName);
        try {
            return IOUtils.toString(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Unable to find " + resourceName, e);
        }
    }
    
}
