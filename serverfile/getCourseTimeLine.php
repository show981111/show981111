<?php
	
	require_once('bookingsystem.php');
	
	$userBranch = $_POST['userBranch'];
	$test = new BookingSystem($userBranch);
	$test->getCourseTimeLine();
	

?>