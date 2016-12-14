package com.lombardrisk.testCase;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.lombardrisk.pages.DWIntegrationPage;
import com.lombardrisk.pages.FormInstancePage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.TestTemplate;

/**
 * Create by Leo Tu on 9/18/2016
 */

public class Contextual extends TestTemplate
{
	@Test
	public void test4011() throws Exception
	{
		String caseID = "4011";
		logger.info("====Verify can add contextual buttons at Data Warehouse Integration page[case id=" + caseID + "]====");
		boolean testRst = false;
		String nodeName = "C" + caseID;
		List<String> testdata = getElementValueFromXML(testData_Contextual, nodeName);
		String DW = testdata.get(0);
		String Name = testdata.get(1);
		String URL = testdata.get(2);
		String Desc = testdata.get(3);
		DWIntegrationPage dWIntegrationPage = null;
		try
		{
			ListPage listPage = m.listPage;
			dWIntegrationPage = listPage.EnterDWIntegrationPage();
			dWIntegrationPage.deleteAllContextual(DW);
			dWIntegrationPage.addContextual(DW, Name, Arrays.asList(URL.split("#")), Desc, true);

			assertThat(dWIntegrationPage.getExistedContextual(DW)).contains(Name);
			testRst = true;

		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Contextual");
		}
	}

	@Test
	public void test4012() throws Exception
	{
		String caseID = "4012";
		logger.info("====Verify can edit contextual buttons at Data Warehouse Integration page[case id=" + caseID + "]====");
		boolean testRst = false;
		String nodeName = "C" + caseID;
		List<String> testdata = getElementValueFromXML(testData_Contextual, nodeName);
		String DW = testdata.get(0);
		String Name = testdata.get(1);
		String NewName = testdata.get(2);
		String NewURL = testdata.get(3);
		String NewDesc = testdata.get(4);
		DWIntegrationPage dWIntegrationPage = null;
		try
		{
			ListPage listPage = m.listPage;
			dWIntegrationPage = listPage.EnterDWIntegrationPage();
			dWIntegrationPage.editContextual(DW, Name, NewName, Arrays.asList(NewURL.split("#")), NewDesc);

			assertThat(dWIntegrationPage.getExistedContextual(DW)).contains(Name);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Contextual");
		}
	}

	@Test
	public void test4013() throws Exception
	{
		String caseID = "4013";
		logger.info("====Verify can cancel to add contextual buttons at Data Warehouse Integration page[case id=" + caseID + "]====");
		boolean testRst = false;
		String nodeName = "C" + caseID;
		List<String> testdata = getElementValueFromXML(testData_Contextual, nodeName);
		String DW = testdata.get(0);
		String Name = testdata.get(1);
		String URL = testdata.get(2);
		String Desc = testdata.get(3);
		DWIntegrationPage dWIntegrationPage = null;
		try
		{
			ListPage listPage = m.listPage;
			dWIntegrationPage = listPage.EnterDWIntegrationPage();
			dWIntegrationPage.addContextual(DW, Name, Arrays.asList(URL.split("#")), Desc, false);

			assertThat(Name).isNotIn(dWIntegrationPage.getExistedContextual(DW));
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Contextual");
		}
	}

	@Test
	public void test4014() throws Exception
	{
		String caseID = "4014";
		logger.info("====Verify can delete contextual buttons at Data Warehouse Integration page[case id=" + caseID + "]====");
		boolean testRst = false;
		String nodeName = "C" + caseID;
		List<String> testdata = getElementValueFromXML(testData_Contextual, nodeName);
		String DW = testdata.get(0);
		String Name = testdata.get(1);
		String URL = testdata.get(2);
		String Desc = testdata.get(3);
		DWIntegrationPage dWIntegrationPage = null;
		try
		{
			ListPage listPage = m.listPage;
			dWIntegrationPage = listPage.EnterDWIntegrationPage();
			dWIntegrationPage.addContextual(DW, Name, Arrays.asList(URL.split("#")), Desc, true);
			assertThat(dWIntegrationPage.getExistedContextual(DW)).contains(Name);
			dWIntegrationPage.deleteContextual(DW, Name);
			assertThat(Name).isNotIn(dWIntegrationPage.getExistedContextual(DW));
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Contextual");
		}
	}

	@Test
	public void test4015() throws Exception
	{
		String caseID = "4015";
		logger.info("====Verify can open a new page with the appropriate URL when click icon[case id=" + caseID + "]====");
		boolean testRst = false;
		String nodeName = "C" + caseID;
		List<String> testdata = getElementValueFromXML(testData_Contextual, nodeName);
		String Regulator = testdata.get(0);
		String Entity = testdata.get(1);
		String Form = testdata.get(2);
		String ReferenceDate = testdata.get(3);
		String DW = testdata.get(4);
		String ContextualName = testdata.get(5);
		String URL = testdata.get(6);
		String Desc = testdata.get(7);
		String Cell = testdata.get(8);
		String URL2 = testdata.get(9).replace("~", "&");
		try
		{
			ListPage listPage = m.listPage;
			DWIntegrationPage dWIntegrationPage = listPage.EnterDWIntegrationPage();
			dWIntegrationPage.deleteAllContextual(DW);
			dWIntegrationPage.addContextual(DW, ContextualName, Arrays.asList(URL.split("#")), Desc, true);
			dWIntegrationPage.backToDashboard();
			listPage.getProductListPage(Regulator, Entity, null, null);
			FormInstancePage formInstancePage = listPage.createNewForm(Entity, ReferenceDate, Form, null, false, false);
			String actualURL = formInstancePage.getNewWindowURL(ContextualName, Cell);

			assertThat(actualURL).isEqualTo(URL2);
			testRst = true;
		}
		catch (RuntimeException e)
		{
			testRst = false;
			//e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally
		{
			writeTestResultToFile(caseID + ",4016,6471", testRst, "Contextual");
		}
	}
}
