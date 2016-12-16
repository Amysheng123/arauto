package com.lombardrisk.testCase_TS;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.TestTemplate;

/**
 * Created by Leo Tu on 4/25/2016.
 */
public class DeleteReturn extends TestTemplate
{
	@Test
	public void test5629() throws Exception
	{
		boolean testRst = false;
		String caseID = "5629";
		logger.info("====Verify privileged user is able to delete return with comments,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_DeleteReturn, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_DeleteReturn, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_DeleteReturn, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_DeleteReturn, nodeName, "ReferenceDate");
			String random = getRandomString(5);
			// String sql =
			// "SELECT COUNT(*) FROM \"FIN_FORM_INSTANCE_DEL_LOG\" WHERE
			// \"FORM_CODE\"='"
			// + formCode + "' AND \"FORM_VERSION\"=" + version +
			// " AND \"COMMENT\"=\'" + random + "\'";
			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, null, false, false);
			formInstancePage.closeFormInstance();
			listPage.deleteFormInstance(Form, ReferenceDate, random, true);
			assertThat(listPage.isFormExist(Form, ReferenceDate)).as("Deleted form still existed in list!").isEqualTo(false);

			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "DeleteReturn");
		}
	}

	@Test
	public void test5632() throws Exception
	{
		boolean testRst = true;
		String caseID = "5632";
		logger.info("====Verify privileged user is unable to delete locked return,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_DeleteReturn, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_DeleteReturn, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_DeleteReturn, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_DeleteReturn, nodeName, "ReferenceDate");

			String formCode = splitReturn(Form).get(0);

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, ReferenceDate);
			String msg = listPage.getDeleteFormInstanceMessage(Form, ReferenceDate);
			if (!msg.equalsIgnoreCase("Return " + formCode + " cannot be deleted,Because it is currently locked."))
			{
				testRst = false;
				logger.error("Delete return message is incorrect, now prompt[" + msg + "]!");
			}
			if (!listPage.isFormExist(Form, ReferenceDate))
			{
				testRst = false;
				logger.error("The locked return should not be deleted!");
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());

		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "DeleteReturn");
		}
	}

	@Test
	public void test5631() throws Exception
	{
		boolean testRst = true;
		String caseID = "5631";
		logger.info("====Verify privileged user is unable to delete return without comments,case[" + caseID + "]====");
		try
		{
			String nodeName = "C" + caseID;
			String Regulator = getElementValueFromXML(testData_DeleteReturn, nodeName, "Regulator");
			String Entity = getElementValueFromXML(testData_DeleteReturn, nodeName, "Entity");
			String Form = getElementValueFromXML(testData_DeleteReturn, nodeName, "Form");
			String ReferenceDate = getElementValueFromXML(testData_DeleteReturn, nodeName, "ReferenceDate");

			ListPage listPage = super.m.listPage;
			listPage.getProductListPage(Regulator, Entity, Form, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, null, false, false);
			formInstancePage.closeFormInstance();
			String msg = listPage.getDeleteFormInstanceMessage(Form, ReferenceDate);
			if (!msg.equalsIgnoreCase("Delete comment is required"))
			{
				testRst = false;
				logger.error("Delete return message is incorrect, now prompt[" + msg + "]!");
			}
			if (!listPage.isFormExist(Form, ReferenceDate))
			{
				testRst = false;
				logger.error("Delete return without comment should not be deleted!");
			}

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "DeleteReturn");
		}
	}
}
