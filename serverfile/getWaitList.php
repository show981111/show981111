<?php
	
	require_once('bookingsystem.php');
	$userBranch = "";

	$test = new BookingSystem($userBranch);
	$test->getWaitList();
	
	
?>