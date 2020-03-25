<?php
	
	require_once('bookingsystem.php');
	
	$userID = $_POST['userID'];
	$extendTeacher = $_POST['extendTeacher'];
	$extendBranch = $_POST['extendBranch'];
	$extendStartDate = $_POST['extendStartDate'];
	
	$test = new BookingSystem($userBranch);
	$test->extendRequest($userID, $extendTeacher,$extendBranch,$extendStartDate );
	

?>