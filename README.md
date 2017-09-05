# SqlFirstMapper: the inverse approach - make SQL write your java code!

Use @SqlSource annotation to generate java classes from plain SQL.
 
You can even place your SQL to Javadoc. One little thing to do is to add type defining suffixes to parameters and result fields of SQL query.     

### 1. Write sql
```dbn-sql
SELECT
    letter.date_send       send_date
    letter.sender_name     sender_name
FROM letter WHERE
 sender_name = :sender
 AND letter.date_send  between :start_date and :end_date
```
### 2. Add type suffixes to sql request and response parameters
```dbn-sql
SELECT
     letter.date_send       send_date__d,
     letter.sender_name     sender_name__s
FROM letter WHERE
  sender_name = :sender__s
  #if(${start_date__d})  AND letter.date_send  >  :start_date__d #end
```
Velocity markup can be used to create dynamic SQL queries, in this example `start_date` condition will be applied only if not null value is passed 
as `start_date` parameter

### 3. Put prepared SQL to javadoc of @SqlSource annotated method 

```java
@SqlSourceFile
public interface LetterSample {

    /**
     * SELECT
     *      letter.date_send       send_date__d,
     *      letter.sender_name     sender_name__s
     * FROM letter WHERE
     *   sender_name = :sender__s
     *   #if(${start_date__d})  AND letter.date_send  > :start_date__d #end
     */
    @SqlSource
    void findSampleLetter();
}
```

### 4. Compile project - get query Request and Response parameters as Java objects and Repository methods 
Add maven dependency:
``` 
<dependency>
   <groupId>com.gitgub.nds842</groupId>
   <artifactId>sql-first-apc</artifactId>
   <version>1.0-SNAPSHOT</version>
</dependency>
```
Compile and check that files are created in `target/generated-sources` folder:
```java
public class FindSampleLetterRes extends com.github.nds842.sqlfirst.base.BaseDto {
    //lines skipped
    public java.util.Date getStartDate(){
        return getValue(map, SEND_DATE);
    }
    public void setSendDate(java.util.Date sendDate){
         map.put(SEND_DATE, sendDate);
    }
    public java.lang.String getSenderName(){
        return getValue(map, SENDER_NAME);
    }
    public void setSenderName(java.lang.String senderName){
         map.put(SENDER_NAME, senderName);
    }
}
public class FindSampleLetterReq extends com.github.nds842.sqlfirst.base.BaseDto {
    //lines skipped
    public java.lang.String getSender(){
       return getValue(map, SENDER);
    }  
    public void setSender(java.lang.String sender){
        map.put(SENDER, sender);
    }    
    public java.util.Date getStartDate(){
       return getValue(map, START_DATE);
    }
    public void setStartDate(java.util.Date startDate){
        map.put(START_DATE, startDate);
    }
 }

public class LetterSampleDao extends BaseDao   { 
    //some lines skipped
    public List<FindSampleLetterRes> findSampleLetter(FindSampleLetterReq req, Connection conn) {
        QueryResultTransformer<FindSampleLetterRes> tr = (rs, rsNames) -> {
            FindSampleLetterRes dto = new FindSampleLetterRes();
            dto.setSendDate(getDateSafely(rs, rsNames, FindSampleLetterRes.SEND_DATE));
            dto.setSenderName(getStringSafely(rs, rsNames, FindSampleLetterRes.SENDER_NAME));
            return dto;
        };
        return super.executeQuery(getTemplate(PACKAGE_NAME, "FindSampleLetter"), req, tr, conn);
    }
}
```

### 5. Tests and usage examples are available in `plain-sql-sample` module.