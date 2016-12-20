package com.lombardrisk.testCase;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.lombardrisk.pages.*;
import com.lombardrisk.utils.TestTemplate;

/**
 * Created by zhijun dai on 9/20/2016.
 */
public class Admin_Calendar extends TestTemplate
{
	@Test
	public void test3462() throws Exception
	{
		String caseID = "3462";
		logger.info("====Verify Calendar page is correct as design[case id=" + caseID + "]====");
		boolean testRst = true;
		CalendarPage calendarPage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Calendar, nodeName);
			String calendarName = testdata.get(0).trim();
			String labelName = testdata.get(1).trim();
			String labelWeekends = testdata.get(2).trim();
			String labelNonWDList = testdata.get(3).trim();
			String labelNonWDPattern = testdata.get(4).trim();

			ListPage listPage = super.m.listPage;
			calendarPage = listPage.openCalendarPage();

			// Check the UI is correct.
			Assert.assertTrue(calendarPage.isEditButtonExist(calendarName));
			Assert.assertTrue(calendarPage.isDelButtonExist(calendarName));
			Assert.assertTrue(calendarPage.isAddCalButtonExist());
			calendarPage = calendarPage.calendarAddClick();
			Assert.assertTrue(calendarPage.isSpecialLabelExist(labelName));
			Assert.assertTrue(calendarPage.isSpecialLabelExist(labelWeekends));
			Assert.assertTrue(calendarPage.isSpecialLabelExist(labelNonWDList));
			Assert.assertTrue(calendarPage.isSpecialLabelExist(labelNonWDPattern));
			Assert.assertTrue(calendarPage.isSaveCalButtonExist());

		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// calendarPage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_Calendar");
		}
	}

	@Test
	public void test3463() throws Exception
	{
		String caseID = "3463";
		logger.info("====Verify Schedule page is correct as design[case id=" + caseID + "]====");
		boolean testRst = true;
		SchedulePage schedulePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Calendar, nodeName);
			String scheduleName = testdata.get(0).trim();
			String recPatternType = testdata.get(1).trim();
			String labelName = testdata.get(2).trim();
			String labelDesc = testdata.get(3).trim();
			String labelRecPattern = testdata.get(4).trim();
			String labelBasedOnCal = testdata.get(5).trim();
			String labelDaily1 = testdata.get(6).trim();
			String labelDaily2 = testdata.get(7).trim();
			String labelDaily3 = testdata.get(8).trim();
			String labelDaily4 = testdata.get(9).trim();
			String dailyDropdown1 = testdata.get(10).trim();
			String dailyDropdown2 = testdata.get(11).trim();
			String dailyDropdown3 = testdata.get(12).trim();
			String recPatTab1 = testdata.get(13).trim();
			String recPatTab2 = testdata.get(14).trim();
			String recPatTab3 = testdata.get(15).trim();
			String labelWeekly1 = testdata.get(16).trim();
			String labelWeekly2 = testdata.get(17).trim();
			String weeklyDropdown1 = testdata.get(18).trim();
			String weeklyDropdown2 = testdata.get(19).trim();
			String weeklyDropdown3 = testdata.get(20).trim();
			String weeklyDropdown4 = testdata.get(21).trim();
			String labelMonthly1 = testdata.get(22).trim();
			String monthlyDropdown1 = testdata.get(23).trim();
			String monthlyDropdown2 = testdata.get(24).trim();
			String labelYearly = testdata.get(25).trim();
			String labelBOC1 = testdata.get(26).trim();
			String labelBOC2 = testdata.get(27).trim();
			String labelBOC3 = testdata.get(28).trim();

			ListPage listPage = super.m.listPage;
			schedulePage = listPage.openSchedulePage();

			// Add a schedule
			schedulePage.addSchedule(scheduleName, scheduleName, recPatternType);

			// Check the UI is correct.
			Assert.assertTrue(schedulePage.isEditSchBtnExist(scheduleName));
			Assert.assertTrue(schedulePage.isDelSchBtnExist(scheduleName));
			schedulePage = schedulePage.addScheduleClick();
			Assert.assertTrue(schedulePage.isLabelNameExist(labelName));
			Assert.assertTrue(schedulePage.isLabelNameExist(labelDesc));
			Assert.assertTrue(schedulePage.isLabelNameExist(labelRecPattern));
			Assert.assertTrue(schedulePage.isLabelNameExist(labelBasedOnCal));

			// Check the daily tab
			Assert.assertTrue(schedulePage.isLabelNameExist(labelDaily1));
			Assert.assertTrue(schedulePage.isLabelNameExist(labelDaily2));
			Assert.assertTrue(schedulePage.isLabelNameExist(labelDaily3));
			Assert.assertTrue(schedulePage.isLabelNameExist(labelDaily4));

			List<String> fromDailyList = new ArrayList<>();
			fromDailyList.add("");
			for (int i = 1; i <= 31; i++)
			{
				fromDailyList.add(i + "");
			}
			Assert.assertEquals(schedulePage.getRecPattDropdownListOptions(dailyDropdown1), fromDailyList);
			Assert.assertEquals(schedulePage.getRecPattDropdownListOptions(dailyDropdown2), fromDailyList);
			Assert.assertEquals(schedulePage.getRecPattDropdownListOptions(dailyDropdown3), fromDailyList);

			// Check weekly tab
			schedulePage.clickRecPatternTabBtn(recPatTab1);
			Assert.assertTrue(schedulePage.isLabelNameExist(labelWeekly1));
			Assert.assertTrue(schedulePage.isLabelNameExist(labelWeekly2));

			List<String> fromWeeklyList = new ArrayList<>();
			fromWeeklyList.add("");
			fromWeeklyList.add("Sunday");
			fromWeeklyList.add("Monday");
			fromWeeklyList.add("Tuesday");
			fromWeeklyList.add("Wednesday");
			fromWeeklyList.add("Thursday");
			fromWeeklyList.add("Friday");
			fromWeeklyList.add("Saturday");
			Assert.assertEquals(schedulePage.getRecPattDropdownListOptions(weeklyDropdown1), fromWeeklyList);
			Assert.assertEquals(schedulePage.getRecPattDropdownListOptions(weeklyDropdown2), fromWeeklyList);
			Assert.assertEquals(schedulePage.getRecPattDropdownListOptions(weeklyDropdown4), fromWeeklyList);

			List<String> lastList = new ArrayList<>();
			lastList.add("");
			lastList.add("First");
			lastList.add("Second");
			lastList.add("Third");
			lastList.add("Fourth");
			lastList.add("Last");
			Assert.assertEquals(schedulePage.getRecPattDropdownListOptions(weeklyDropdown3), lastList);

			// Check the monthly
			schedulePage.clickRecPatternTabBtn(recPatTab2);
			Assert.assertTrue(schedulePage.isLabelNameExist(labelMonthly1));
			Assert.assertEquals(schedulePage.getRecPattDropdownListOptions(monthlyDropdown1), fromDailyList.subList(0, 13));
			Assert.assertEquals(schedulePage.getRecPattDropdownListOptions(monthlyDropdown2), fromDailyList.subList(0, 13));

			// Check the yearly
			schedulePage.clickRecPatternTabBtn(recPatTab3);
			Assert.assertTrue(schedulePage.isLabelNameExist(labelYearly));

			// Check the based on calendar
			Assert.assertTrue(schedulePage.isLabelNameExist(labelBOC1));
			Assert.assertTrue(schedulePage.isLabelNameExist(labelBOC2));
			Assert.assertTrue(schedulePage.isLabelNameExist(labelBOC3));
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// schedulePage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_Calendar");
		}
	}

	@Test
	public void test3464() throws Exception
	{
		String caseID = "3464";
		logger.info("====Verify Form setup page is correct as design[case id=" + caseID + "]====");
		boolean testRst = true;
		FormSchedulePage formSchedulePage = null;
		try
		{
			ListPage listPage = super.m.listPage;
			formSchedulePage = listPage.openFormSchedulePage();

			// Check the UI is correct.
			Assert.assertTrue(formSchedulePage.isRegulatorDropdownExist());
			Assert.assertTrue(formSchedulePage.isFormDropdownExist());
			Assert.assertTrue(formSchedulePage.isEntityDropdownExist());
			Assert.assertTrue(formSchedulePage.isScheduleDropdownExist());
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			writeTestResultToFile(caseID, testRst, "Admin_Calendar");
		}
	}

	@Test
	public void test3531() throws Exception
	{
		String caseID = "3531";
		logger.info("====Verify schedule can not be added with not uniqued name [case id=" + caseID + "]====");
		boolean testRst = true;
		SchedulePage schedulePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Calendar, nodeName);
			String name1 = testdata.get(0).trim();
			String description1 = testdata.get(1).trim();
			String dailyAddButton1 = testdata.get(2).trim();
			String dailyAddButton2 = testdata.get(3).trim();
			String dailyAddButton3 = testdata.get(4).trim();
			String dailyAddButton4 = testdata.get(5).trim();
			String dailyFrom = testdata.get(6).trim();
			String dailyTo = testdata.get(7).trim();
			String dailyInterval = testdata.get(8).trim();
			String basedOnCal1 = testdata.get(9).trim();
			String dailyDropdown1 = testdata.get(10).trim();
			String dailyDropdown2 = testdata.get(11).trim();
			String dailyInput = testdata.get(12).trim();
			String description2 = testdata.get(13).trim();
			String recPatternTab = testdata.get(14).trim();
			String weeklyButton = testdata.get(15).trim();
			String basedOnCal2 = testdata.get(16).trim();
			String msg = testdata.get(17).trim();

			ListPage listPage = super.m.listPage;
			schedulePage = listPage.openSchedulePage();

			// Add a schedule
			schedulePage.addScheduleClick();
			schedulePage.addScheduleName(name1);
			schedulePage.addScheduleDescription(description1);
			schedulePage.selectRecPattDropdownListOption(dailyDropdown1, dailyFrom);
			schedulePage.selectRecPattDropdownListOption(dailyDropdown2, dailyTo);
			schedulePage.inputRecPatternInterval(dailyInput, dailyInterval);
			schedulePage.addDailyRecPattern(dailyAddButton1);
			schedulePage.addDailyRecPattern(dailyAddButton2);
			schedulePage.addDailyRecPattern(dailyAddButton3);
			schedulePage.addDailyRecPattern(dailyAddButton4);
			schedulePage.selectBasedOnCal(basedOnCal1, null);
			schedulePage.saveScheduleClick();
			Assert.assertTrue(schedulePage.isScheduleExist(name1));

			// Add a schedule2 with same name with before
			schedulePage.addScheduleClick();
			schedulePage.addScheduleName(name1);
			schedulePage.addScheduleDescription(description2);
			schedulePage.clickRecPatternTabBtn(recPatternTab);
			schedulePage.addDailyRecPattern(weeklyButton);
			schedulePage.selectBasedOnCal(basedOnCal2, null);
			String message = schedulePage.saveScheduleClick();
			Assert.assertEquals(message, msg);
			Assert.assertTrue(schedulePage.isScheduleExist(name1));
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// schedulePage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_Calendar");
		}
	}

	@Test
	public void test3530() throws Exception
	{
		String caseID = "3530";
		logger.info("====Verify calendar can not be added with not uniqued name[case id=" + caseID + "]====");
		boolean testRst = true;
		CalendarPage calendarPage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Calendar, nodeName);
			String calendarName = testdata.get(0).trim();
			String saturday = testdata.get(1).trim();
			String sunday = testdata.get(2).trim();
			String monday = testdata.get(3).trim();
			String msg = testdata.get(4).trim();

			ListPage listPage = super.m.listPage;
			calendarPage = listPage.openCalendarPage();

			// Add calendar1
			calendarPage.calendarAddClick();
			calendarPage.inputCalendarName(calendarName);
			calendarPage.selectWeekends(saturday, sunday);
			calendarPage.saveCalendarClick();
			Assert.assertTrue(calendarPage.isCalendarExist(calendarName));
			calendarPage.clickEditButtonOfCalendar(calendarName);
			Assert.assertTrue(calendarPage.isWeekendsChecked(saturday));
			Assert.assertTrue(calendarPage.isWeekendsChecked(sunday));

			// Add same calendar1
			calendarPage.calendarAddClick();
			calendarPage.inputCalendarName(calendarName);
			calendarPage.selectWeekends(monday, sunday);
			String message = calendarPage.saveCalendarClick();
			Assert.assertEquals(message, msg);
			Assert.assertTrue(calendarPage.isCalendarExist(calendarName));
			calendarPage.clickEditButtonOfCalendar(calendarName);
			Assert.assertTrue(calendarPage.isWeekendsChecked(saturday));
			Assert.assertTrue(calendarPage.isWeekendsChecked(sunday));
			Assert.assertFalse(calendarPage.isWeekendsChecked(monday));
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// calendarPage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_Calendar");
		}
	}

	@Test
	public void test3494() throws Exception
	{
		String caseID = "3494";
		logger.info("====Verify Calendar can be added, edited, deleted correctly [case id=" + caseID + "]====");
		boolean testRst = true;
		CalendarPage calendarPage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Calendar, nodeName);
			String calendarName1 = testdata.get(0).trim();
			String saturday = testdata.get(1).trim();
			String sunday = testdata.get(2).trim();
			String nonWorkingDayListDesc = testdata.get(3).trim();
			String nonWorkingDayListDate = testdata.get(4).trim();
			String calendarName2 = testdata.get(5).trim();
			String monday = testdata.get(6).trim();
			String nonWorkingDayPatDesc = testdata.get(7).trim();
			String dailyAddButton = testdata.get(8).trim();
			String msg = testdata.get(9).trim();

			ListPage listPage = super.m.listPage;
			calendarPage = listPage.openCalendarPage();

			// Add calendar1
			calendarPage.calendarAddClick();
			calendarPage.inputCalendarName(calendarName1);
			calendarPage.selectWeekends(saturday, sunday);
			NonWorkingDayListPage nonWorkingDayListPage = calendarPage.nonWorkingDaysListAddClick();
			nonWorkingDayListPage.setDescription(nonWorkingDayListDesc);
			nonWorkingDayListPage.setFixDate(nonWorkingDayListDate);
			calendarPage = nonWorkingDayListPage.saveButtonClick();
			calendarPage.saveCalendarClick();
			Assert.assertTrue(calendarPage.isCalendarExist(calendarName1));
			calendarPage.clickEditButtonOfCalendar(calendarName1);
			Assert.assertTrue(calendarPage.isWeekendsChecked(saturday));
			Assert.assertTrue(calendarPage.isWeekendsChecked(sunday));
			Assert.assertTrue(calendarPage.getExistNonWorkingDaysList().contains(nonWorkingDayListDesc));

			// Add calendar2
			calendarPage.calendarAddClick();
			calendarPage.inputCalendarName(calendarName2);
			calendarPage.selectWeekends(monday, sunday);
			NonWorkingDaysPatternPage nonWorkingDaysPatternPage = calendarPage.clickNonWorkingDaysPatternAddBtn();
			nonWorkingDaysPatternPage.inputDescField(nonWorkingDayPatDesc);
			nonWorkingDaysPatternPage.clickDailyAddBtn(dailyAddButton);
			calendarPage = nonWorkingDaysPatternPage.clickSaveBtn();
			calendarPage.saveCalendarClick();
			Assert.assertTrue(calendarPage.isCalendarExist(calendarName2));

			// Edit calendar2
			calendarPage.clickEditButtonOfCalendar(calendarName2);
			nonWorkingDayListPage = calendarPage.nonWorkingDaysListAddClick();
			nonWorkingDayListPage.setDescription(nonWorkingDayListDesc);
			nonWorkingDayListPage.setFixDate(nonWorkingDayListDate);
			calendarPage = nonWorkingDayListPage.saveButtonClick();
			calendarPage.saveCalendarClick();
			calendarPage.clickEditButtonOfCalendar(calendarName2);
			Assert.assertTrue(calendarPage.getExistNonWorkingDaysList().contains(nonWorkingDayListDesc));

			// Click delete button and cancel
			calendarPage.clickDeleteCal(calendarName2);
			Assert.assertEquals(calendarPage.getDeleteMsg(), msg);
			calendarPage.cancelDeleteCal();
			Assert.assertTrue(calendarPage.isCalendarExist(calendarName2));

			// Click delete button and confirm
			calendarPage.clickDeleteCal(calendarName2);
			calendarPage.confirmDeleteCal();
			Assert.assertFalse(calendarPage.isCalendarExist(calendarName2));
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// calendarPage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_Calendar");
		}
	}

	@Test
	public void test3529() throws Exception
	{
		String caseID = "3529";
		logger.info("====Verify Form can be set up with schedule correctly [case id=" + caseID + "]====");
		boolean testRst = true;
		FormSchedulePage formSchedulePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Calendar, nodeName);
			String entityName = testdata.get(0).trim();
			String prefix = testdata.get(1).trim();
			String[] returns = testdata.get(2).trim().split("#");
			String[] ugs = testdata.get(3).trim().split("#");
			String[] pgs = testdata.get(4).trim().split("#");
			String schName = testdata.get(5).trim();
			String dailyType = testdata.get(6).trim();
			String regulator = testdata.get(7).trim();

			// Assign 16 return to entity
			ListPage listPage = super.m.listPage;
			EntityPage entityPage = listPage.EnterEntityPage();
			entityPage.addRootEntityIfNotExist(entityName, entityName, entityName, true);
			entityPage.assignReturnToEntity(entityName, prefix, returns, ugs, pgs);
			listPage = entityPage.backToDashboard();

			// Add schedule
			SchedulePage schedulePage = listPage.openSchedulePage();
			schedulePage.addSchedule(schName, schName, dailyType);
			listPage = schedulePage.backToDashboard();
			formSchedulePage = listPage.openFormSchedulePage();

			// Binding form with schedule
			for (int i = 0; i < returns.length; i++)
			{
				formSchedulePage.bindingFormWithSch(regulator, returns[i], entityName, schName);
			}
			String count = formSchedulePage.getCountOfBindingRecords();
			Assert.assertEquals(count, "15");
			Assert.assertEquals(formSchedulePage.getCurrentPageNumber(), "1");
			formSchedulePage.clickNextPage();
			Assert.assertEquals(formSchedulePage.getCurrentPageNumber(), "2");
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			// formSchedulePage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_Calendar");
		}
	}

	@Test
	public void test3528() throws Exception
	{
		String caseID = "3528";
		logger.info("====Dashboard DBAC-01-05 Verify Schedule can be added, edited, deleted correctly [case id=" + caseID + "]====");
		boolean testRst = true;
		SchedulePage schedulePage = null;
		try
		{
			String nodeName = "C" + caseID;
			List<String> testdata = getElementValueFromXML(testData_Calendar, nodeName);
			String schName1 = testdata.get(0).trim();
			String desc1 = testdata.get(1).trim();
			String dailyAddBtn1 = testdata.get(2).trim();
			String dailyFromDropdown = testdata.get(3).trim();
			String dailyToDropdown = testdata.get(4).trim();
			String dailyInput = testdata.get(5).trim();
			String dailyFromValue = testdata.get(6).trim();
			String dailyToValue = testdata.get(7).trim();
			String dailyIntervalValue = testdata.get(8).trim();
			String dailyAddBtn2 = testdata.get(9).trim();
			String basedOnCalendar1 = testdata.get(10).trim();
			String schName2 = testdata.get(11).trim();
			String desc2 = testdata.get(12).trim();
			String recPatternTab = testdata.get(13).trim();
			String weeklyAddBtn1 = testdata.get(14).trim();
			String basedOnCal2 = testdata.get(15).trim();
			String value = testdata.get(16).trim();
			String existPat1 = testdata.get(17).trim();
			String weeklyAddBtn2 = testdata.get(18).trim();
			String existPat2 = testdata.get(19).trim();
			String deleteMsg = testdata.get(20).trim();

			// Add schedule 1
			ListPage listPage = super.m.listPage;
			schedulePage = listPage.openSchedulePage();
			schedulePage.addScheduleClick();
			schedulePage.addScheduleName(schName1);
			schedulePage.addScheduleDescription(desc1);
			schedulePage.addDailyRecPattern(dailyAddBtn1);
			schedulePage.selectRecPattDropdownListOption(dailyFromDropdown, dailyFromValue);
			schedulePage.selectRecPattDropdownListOption(dailyToDropdown, dailyToValue);
			schedulePage.inputRecPatternInterval(dailyInput, dailyIntervalValue);
			schedulePage.addDailyRecPattern(dailyAddBtn2);
			schedulePage.selectBasedOnCal(basedOnCalendar1, null);
			schedulePage.saveScheduleClick();
			Assert.assertTrue(schedulePage.isScheduleExist(schName1));

			// Add sch2
			schedulePage.addScheduleClick();
			schedulePage.addScheduleName(schName2);
			schedulePage.addScheduleDescription(desc2);
			schedulePage.clickRecPatternTabBtn(recPatternTab);
			schedulePage.addDailyRecPattern(weeklyAddBtn1);
			schedulePage.selectBasedOnCal(basedOnCal2, value);
			schedulePage.saveScheduleClick();
			Assert.assertTrue(schedulePage.isScheduleExist(schName1));
			schedulePage.clickEditButtonOfSchedule(schName2);
			schedulePage.clickRecPatternTabBtn(recPatternTab);
			Assert.assertTrue(schedulePage.getExistRecPattern(recPatternTab).contains(existPat1));
			Assert.assertFalse(schedulePage.getExistRecPattern(recPatternTab).contains(existPat2));

			// Edit sch2
			schedulePage.addDailyRecPattern(weeklyAddBtn2);
			schedulePage.saveScheduleClick();
			schedulePage.clickEditButtonOfSchedule(schName2);
			schedulePage.clickRecPatternTabBtn(recPatternTab);
			Assert.assertTrue(schedulePage.getExistRecPattern(recPatternTab).contains(existPat1));
			Assert.assertFalse(schedulePage.getExistRecPattern(recPatternTab).contains(existPat2));

			// Delete sch2 and cancel.
			schedulePage.clickDeleteButtonOfSchedule(schName2);
			Assert.assertEquals(schedulePage.getDeleteMsg(), deleteMsg);
			schedulePage.cancelDeleteSch();
			Assert.assertTrue(schedulePage.isScheduleExist(schName2));

			// Delete sch2 and confirm
			schedulePage.clickDeleteButtonOfSchedule(schName2);
			schedulePage.confirmDeleteSch();
			Assert.assertFalse(schedulePage.isScheduleExist(schName2));
		}
		catch (RuntimeException e)
		{
			testRst = false;
			// e.printStackTrace();
		}
		finally
		{
			schedulePage.backToDashboard();
			writeTestResultToFile(caseID, testRst, "Admin_Calendar");
		}
	}
}
