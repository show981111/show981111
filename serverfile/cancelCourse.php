<?php
	
	//consoleTest("hi");
	require_once('bookingsystem.php');
	
	$userID = $_POST['userID'];
	$cancelTeacher = $_POST['cancelTeacher'];
	$cancelBranch = $_POST['cancelBranch'];
	$startDate = $_POST['startDate'];
	$endDate = $_POST['endDate'];
	
	
	//$userBranch = "교대";

	$test = new BookingSystem($cancelBranch);
	//echo "good";
	//$test->getTimeForMonth("목","이채정","2020-04-02","30","admin");
	$test->cancelCourse($userID,$cancelTeacher, $cancelBranch, $startDate, $endDate);
	// getTimeForMonth($courseDay, $courseTeacher, $startDate, $userDuration)
	

?>