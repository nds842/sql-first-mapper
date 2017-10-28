package com.github.nds842.sqlfirst.springsample;

import com.github.nds842.sqlfirst.apc.SqlSource;
import com.github.nds842.sqlfirst.apc.SqlSourceDao;
import org.junit.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class AllDaoTest extends AbstractTransactionalTestNGSpringContextTests {
    
    @Autowired
    private List<SqlSourceDao> repositoryList;
    
    private Object prepareReq(Method method) throws InvocationTargetException, IllegalAccessException {
        
        Class<?> reqClass = method.getParameterTypes()[0];
        
        Object t;
        try {
            t = reqClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        PropertyDescriptor[] properties = BeanUtils.getPropertyDescriptors(reqClass);
        for (PropertyDescriptor p : properties) {
            Method writeMethod = p.getWriteMethod();
            if (writeMethod == null || writeMethod.getParameterCount() != 1) {
                continue;
            }
            writeMethod.invoke(t, prepareValue(writeMethod.getParameterTypes()[0], "1"));
        }
        return t;
    }
    
    private Object prepareValue(Class<?> clazz, String s) {
        if (Integer.class.isAssignableFrom(clazz)) {
            return Integer.valueOf(s);
        } else if (Long.class.isAssignableFrom(clazz)) {
            return Long.valueOf(s);
        } else if (String.class.isAssignableFrom(clazz)) {
            return s;
        } else if (Date.class.isAssignableFrom(clazz)) {
            return new Date();//TODO add date parsing
        }
        throw new RuntimeException("Can not convert " + s + " to " + clazz);
    }
    
    
    @DataProvider
    public Object[][] daoMethodsForTest() {
        List<Object[]> daoMethodList = new ArrayList<>();
        for (SqlSourceDao daoInstance : repositoryList) {
            Class<? extends SqlSourceDao> unproxiedClass = daoInstance.getUnproxiedClass();
            for (Method m : unproxiedClass.getMethods()) {
                SqlSource annotation = AnnotationUtils.findAnnotation(m, SqlSource.class);
                if (annotation == null || annotation.skipTest()) {
                    continue;
                }
                
                int parameterCount = m.getParameterCount();
                Assert.assertTrue(parameterCount <= 1);
                daoMethodList.add(new Object[]{m.getName(), daoInstance});
            }
        }
        return daoMethodList.toArray(new Object[][]{});
    }
    
    
    @Test(dataProvider = "daoMethodsForTest")
    public void testDaoMethods(String methodName, SqlSourceDao daoInstance) throws IOException, InvocationTargetException, IllegalAccessException {
        Method m = BeanUtils.findDeclaredMethodWithMinimalParameters(daoInstance.getUnproxiedClass(), methodName);
        
        int length = m.getParameterTypes().length;
        
        if (length == 0) {
            m.invoke(daoInstance);
        } else {
            m.invoke(daoInstance, prepareReq(m));
        }
    }
}