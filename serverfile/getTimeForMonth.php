<?php
	
	//consoleTest("hi");
	require_once('bookingsystem.php');
	
	$userBranch = $_POST['userBranch'];
	$courseDay = $_POST['courseDay'];
	$courseTeacher = $_POST['courseTeacher'];
	$startDate = $_POST['startDate'];
	$userDuration = $_POST['userDuration'];
	$userBranch = "교대";
	$test = new BookingSystem($userBranch);
	//echo "good";
	//$test->getTimeForMonth("화","유혜원","2020-03-03","30");
	$test->getTimeForMonth($courseDay, $courseTeacher, $startDate, $userDuration);
	// getTimeForMonth($courseDay, $courseTeacher, $startDate, $userDuration)
	

?>