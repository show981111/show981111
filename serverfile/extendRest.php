<?php
	
	require_once('bookingsystem.php');
	
	$userBranch = "";

	// $termStart = "2020-06-01";
	// $termEnd = "2020-06-29";

	$test = new BookingSystem($userBranch);
	$test->putExtendTerm("", "");
	
	
?>