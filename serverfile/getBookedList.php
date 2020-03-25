<?php
	
	//consoleTest("hi");
	require_once('bookingsystem.php');

	
	$userID = $_POST['userID'];
	$option = $_POST['option'];
	
	$test = new BookingSystem("");
	
	if(isset($userID) && isset($option))
	{
		$test->getBookedList($userID, $option);
	}
	
	

?>