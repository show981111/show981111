<?php
	
	//consoleTest("hi");
	require_once('bookingsystem.php');
	
	$courseBranch = $_POST['courseBranch'];
	$courseDay = $_POST['courseDay'];
	$courseTeacher = $_POST['courseTeacher'];
	$startDate = $_POST['startDate'];
	$userDuration = $_POST['userDuration'];
	$userID = $_POST['userID'];
	$startTime = $_POST['startTime'];

	// $courseBranch = "교대";
	// $courseDay = "목";
	// $courseTeacher = "이채정";
	// $startDate = "2020-03-26";
	// $userDuration ="30";
	// $userID = "testtt";
	// $startTime = "14:00";

	date_default_timezone_set("Asia/Seoul");
	$todayDateTime = date('Y-m-d H:i:s', time());
	
	$dow = "fail";
	switch ($courseDay) {
		case "월":
			$dow = "1";
			break;
		case "화":
			$dow = "2";
			break;
		case "수":
			$dow = "3";
			break;
		case "목":
			$dow = "4";
			break;
		case "금":
			$dow = "5";
			break;
		case "토":
			$dow = "6";
			break;
		case "일":
			$dow = "0";
			break;
		
	}

	$endTime = "error";
	
	$tmpTime = strtotime($startTime);

	if($userDuration == "45")
	{
		$endTime = date("H:i",strtotime('+45 minutes',$tmpTime));

	}else if($userDuration == "30")
	{
		$endTime = date("H:i",strtotime('+30 minutes',$tmpTime));

	}else if($userDuration == "60")
	{
		$endTime = date("H:i",strtotime('+60 minutes',$tmpTime));
	}

	$dayofweek = date('w', strtotime($startDate));

	if($dayofweek == $dow )
	{
		if($endTime == "error")
		{
			echo "fail";
		}else
		{
			$test = new BookingSystem($courseBranch);
			$test->putWaitList($courseTeacher, $courseBranch, $userID, $startTime, $endTime, $dow,$startDate, $todayDateTime);
		}

	}else
	{
		echo "fail";
	}

	

?>