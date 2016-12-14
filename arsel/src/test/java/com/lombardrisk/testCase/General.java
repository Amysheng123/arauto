package com.lombardrisk.testCase;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.testng.annotations.Test;

import com.lombardrisk.pages.AboutPage;
import com.lombardrisk.pages.ListPage;
import com.lombardrisk.utils.TestTemplate;

/**
 * Created by Leo Tu on 11/25/2016.
 */
public class General extends TestTemplate
{

	@Test
	public void test6487() throws Exception
	{
		String caseID = "6586";
		boolean testRst = false;
		try
		{
			ListPage listPage = super.m.listPage;
			AboutPage aboutPage = listPage.enterAboutPage();
			aboutPage.sortColumn("asc", 1);
			List<String> columns = aboutPage.getConfigurationsColumns(1);
			assertThat(columns.get(0)).isEqualTo("ECR");
			assertThat(columns.get(0)).isEqualTo("ECRInternal");

			aboutPage.sortColumn("desc", 1);
			columns = aboutPage.getConfigurationsColumns(1);
			assertThat(columns.get(0)).isEqualTo("TESTPRODUCT");
			assertThat(columns.get(0)).isEqualTo("TESTALLOWNULLIMP");
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
			writeTestResultToFile(caseID, testRst, "General");
		}
	}

	@Test
	public void test6483() throws Exception
	{
		String caseID = "6483";
		boolean testRst = false;
		try
		{
			ListPage listPage = super.m.listPage;
			AboutPage aboutPage = listPage.enterAboutPage();
			List<String> row = aboutPage.getConfigurationsRow("FED");
			assertThat(row.get(1)).isEqualTo("US FED Reserve");
			assertThat(row.get(4)).isEqualTo("");
			row = aboutPage.getConfigurationsRow("OSFAA_DW_DT_NEW");
			assertThat(row.get(1)).isEqualTo("OFSAA DWDTICON");
			assertThat(row.get(4)).isEqualTo("");
			row = aboutPage.getConfigurationsRow("FED_DS");
			assertThat(row.get(1)).isEqualTo("US1 FED Reserve DS");
			assertThat(row.get(4)).isEqualTo("OSFAA_DS_DT");
			row = aboutPage.getConfigurationsRow("FED_DS");
			assertThat(row.get(1)).isEqualTo("US1 FED Reserve DS");
			assertThat(row.get(4)).isEqualTo("OSFAA_DS_DT");
			row = aboutPage.getConfigurationsRow("MAS");
			assertThat(row.get(1)).isEqualTo("Monetary Authority of Singapore");
			assertThat(row.get(4)).isEqualTo("REPORTER");
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
			writeTestResultToFile(caseID + ",6484,6482,6612", testRst, "General");
		}
	}

}
