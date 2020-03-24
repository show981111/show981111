<?php
	
	require_once('bookingsystem.php');
	
	$userBranch = $_POST['userBranch'];
	$userID = $_POST['userID'];
	
	$test = new BookingSystem($userBranch);
	$test->getUser($userID, $userBranch);
	

?>