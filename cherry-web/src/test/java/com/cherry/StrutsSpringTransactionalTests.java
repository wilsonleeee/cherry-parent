package com.cherry;

import org.apache.struts2.StrutsTestCase;
import org.drools.io.impl.EncodedResource;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
	TransactionalTestExecutionListener.class,
	DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class
})
public abstract class StrutsSpringTransactionalTests extends StrutsTestCase implements ApplicationContextAware {

  /**
   * The {@link org.springframework.context.ApplicationContext} that was injected into this test instance
   * via {@link #setApplicationContext(org.springframework.context.ApplicationContext)}.
   */
  protected ApplicationContext applicationContext;


  /**
   * Set the {@link ApplicationContext} to be used by this test instance,
   * provided via {@link ApplicationContextAware} semantics.
   */
  public final void setApplicationContext(final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * The SimpleJdbcTemplate that this base class manages, available to subclasses.
   */
  protected SimpleJdbcTemplate simpleJdbcTemplate;

  private String sqlScriptEncoding;


  /**
   * Specify the encoding for SQL scripts, if different from the platform encoding.
   *
   * @see #executeSqlScript
   */
  public void setSqlScriptEncoding(String sqlScriptEncoding) {
    this.sqlScriptEncoding = sqlScriptEncoding;
  }


  /**
   * Count the rows in the given table.
   *
   * @param tableName table name to count rows in
   * @return the number of rows in the table
   */
  protected int countRowsInTable(String tableName) {
    return SimpleJdbcTestUtils.countRowsInTable(this.simpleJdbcTemplate, tableName);
  }

  /**
   * Convenience method for deleting all rows from the specified tables.
   * Use with caution outside of a transaction!
   *
   * @param names the names of the tables from which to delete
   * @return the total number of rows deleted from all specified tables
   */
  protected int deleteFromTables(String... names) {
    return SimpleJdbcTestUtils.deleteFromTables(this.simpleJdbcTemplate, names);
  }

  /**
   * Execute the given SQL script. Use with caution outside of a transaction!
   * <p>The script will normally be loaded by classpath. There should be one statement
   * per line. Any semicolons will be removed. <b>Do not use this method to execute
   * DDL if you expect rollback.</b>
   *
   * @param sqlResourcePath the Spring resource path for the SQL script
   * @param continueOnError whether or not to continue without throwing an
   *                        exception in the event of an error
   * @throws org.springframework.dao.DataAccessException
   *          if there is an error executing a statement
   *          and continueOnError was <code>false</code>
   */
  protected void executeSqlScript(String sqlResourcePath, boolean continueOnError)
      throws DataAccessException {

    Resource resource = this.applicationContext.getResource(sqlResourcePath);
    SimpleJdbcTestUtils.executeSqlScript(
        this.simpleJdbcTemplate, (Resource) new EncodedResource((org.drools.io.Resource) resource, this.sqlScriptEncoding), continueOnError);
  }

}