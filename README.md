# SqlFirstMapper: the inverse approach - make SQL write your java code!

Use @SqlSource annotation to generate java classes from plain SQL.
 
You can even place your SQL to Javadoc. One little thing to do is to add type defining suffixes to parameters and result fields of SQL query.     

```java
@SqlSourceFile
public interface LetterSample {

    /**
     * SELECT
     *      letter.date_send       send_date__d,
     *      letter.sender_name     sender_name__s
     * FROM letter WHERE
     *   sender_name = :sender__s
     *   #if(${start_date__d})  AND letter.date_send  between :start_date__d and :end_date__d #end
     */
    @SqlSource
    void findSampleLetter();

    /**
     * INSERT into letter (sender_name, date_send) 
     * VALUES (:sender_name__s, :send_date__d)
     */
    @SqlSource
    void insertSampleLetter(InsertSampleLetterReq req, Connection conn);

}
```

Having maven dependency added just compile your project, `Dao` and `Request` and `Response` classes will be generated at compile time: 

```java
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

    public void insertSampleLetter(InsertSampleLetterReq req, Connection conn) {
        super.executeUpdate(getTemplate(PACKAGE_NAME, "InsertSampleLetter"), req, conn);
    }
}
```

Dao layer is ready to be used:

```java
try (Connection conn = obtainConnection()) {
    LetterSampleDao dao = LetterSampleDao.getInstance();
    InsertSampleLetterReq insertReq = new InsertSampleLetterReq().withSenderName(someName);
    insertReq.setSendDate(someDate);
    dao.insertSampleLetter(insertReq, conn);
    
    FindSampleLetterReq findReq = new FindSampleLetterReq().withSender(someName);
    List<FindSampleLetterRes> foundList = dao.findSampleLetter(findReq, conn);
    
    Assert.assertEquals(foundList.size(), 1);
    Assert.assertEquals(foundList.get(0).getSendDate(), someDate);
    Assert.assertEquals(foundList.get(0).getSenderName(), someName);
}
```

This is an early draft of concept proof, hope that someone might find it interesting and will help moving it towards production ready release :)