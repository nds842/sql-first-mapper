Very often when the project needs to achieve better performance or you have to deal with complex dynamic SQL queries - the only way is to move from JPA and Spring Data to native SQL.
Dealing with plenty of native SQL in a project can become a painful experience due to lack of type safety and weak relationship between SQL and Java. Sounds familiar?  Then take a look here: 

# SqlFirstMapper: the inverse approach - make SQL write your Java code!

Suppose you have to implement custom repository method using JdbcTemplate that will execute dynamic SQL, where the date parameter should be applied only if it is not null:
  
```dbn-sql
SELECT
    letter.date_send       send_date,
    letter.sender_name     sender_name
FROM letter WHERE
 sender_name = :sender
 AND letter.date_send  >= :start_date 
```
 
Just declare custom repository interface and add `@SqlSource` annotation:
 
```java
public interface LetterRepositoryCustom {

    /**
     * SELECT
     *      letter.date_send       send_date__d,
     *      letter.sender_name     sender_name__s
     * FROM letter WHERE
     *   sender_name = :sender__s
     *   #if(${start_date__d})  AND letter.date_send  >= :start_date__d #end
     */
    @SqlSource
    void findSampleLetter();
}
```

Attach `sql-first-apc` annotation processor to your build by just adding a processor jar dependency.  

RepositoryImpl class with method request and response parameters will be automatically generated at compile time:  

```java
@Generated(
    value = "com.github.nds842.sqlfirst.apc.SqlFirstAnnotationProcessor"
)
@Repository
public class LetterRepositoryImpl implements LetterRepositoryCustom {

    @Autowired
    private SqlFirstSpringQueryExecutor sqlFirstQueryExecutor;

    /**
     *   SELECT
     *      letter.date_send       send_date__d,
     *      letter.sender_name     sender_name__s
     *   FROM letter WHERE
     *     sender_name = :sender__s
     *     #if(${start_date__d})  AND letter.date_send >= :start_date__d #end
     */
    public List<FindSampleLetterRes> findSampleLetter(FindSampleLetterReq req) {
        RowMapper<FindSampleLetterRes> rowMapper = (resultSet, i) -> {
            FindSampleLetterRes dto = new FindSampleLetterRes();
            dto.setSendDate(resultSet.getDate(FindSampleLetterRes.SEND_DATE));
            dto.setSenderName(resultSet.getString(FindSampleLetterRes.SENDER_NAME));
            return dto;
        };
        return sqlFirstQueryExecutor.executeQuery(sqlFirstQueryExecutor.getTemplate(PACKAGE_NAME, "FindSampleLetter"), req, rowMapper);
    }
}
```

Only correct your custom repository interface to conform with generated method signature:
 
`List<FindSampleLetterRes> findSampleLetter(FindSampleLetterReq req);` 

Sample usage examples are available in `spring-sample` module.

Sql source can be supplied as reference to resource file, parent class of generated repository impl can be set up via annotation properties so as the name of generated class.   