<?php
	
	//consoleTest("hi");
	require_once('bookingsystem.php');
	
	$userBranch = $_POST['userBranch'];
	$courseDay = $_POST['courseDay'];
	$courseTeacher = $_POST['courseTeacher'];
	$startDate = $_POST['startDate'];
	$userDuration = $_POST['userDuration'];
	$userName = $_POST['userName'];
	//$userBranch = "교대";
	$test = new BookingSystem($userBranch);
	//echo "good";
	//$test->getTimeForMonth("목","이채정","2020-04-02","30","admin");
	$test->getTimeForMonth($courseDay, $courseTeacher, $startDate, $userDuration,$userName);
	// getTimeForMonth($courseDay, $courseTeacher, $startDate, $userDuration)
	

?>