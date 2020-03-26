<?php
	
	//consoleTest("hi");
	require_once('bookingsystem.php');


	$userID = $_POST['userID'];
	$courseBranch = $_POST['courseBranch'];
	$courseTeacher = $_POST['courseTeacher'];
	$userDuration = $_POST['userDuration'];
	$selectedDate = $_POST['selectedDate'];
	$userName = $_POST['userName'];

	// $userID ="test1";
	// $courseBranch = "교대";
	// $courseTeacher ="김은솔";
	// $userDuration = "30";
	// $selectedDate = "2020-03-26";
	// $userName = "test1";
	
	$test = new BookingSystem("");
	$dow =  date('w', strtotime($selectedDate));

	switch ($dow) {
		case "0":
			$dow = "일";
			break;
		case "1":
			$dow = "월";
			break;
		case "2":
			$dow = "화";
			break;
		case "3":
			$dow = "수";
			break;
		case "4":
			$dow = "목";
			break;
		case "5":
			$dow = "금";
			break;
		case "6":
			$dow = "토";
			break;
	}

	
	if(isset($userID) && isset($courseBranch) && isset($courseTeacher) && isset($userDuration) && isset($selectedDate) && isset($userName))
	{
		$test->getTimeForDay($userID,$userDuration, $selectedDate, $userName, $courseTeacher, $courseBranch, $dow);
	}
	
	

?>