<?php
	
	require_once('bookingsystem.php');
	
	// $userID = $_POST['userID'];
	// $extendTeacher = $_POST['extendTeacher'];
	// $extendBranch = $_POST['extendBranch'];
	// $extendStartDate = $_POST['extendStartDate'];

	$userID = "test1";
	$extendTeacher = "이채정";
	$extendBranch = "교대";
	$extendStartDate = "2020-03-23 14:00";
	
	$test = new BookingSystem($userBranch);
	$test->extendRequest($userID, $extendTeacher,$extendBranch,$extendStartDate );
	

?>