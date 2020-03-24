<?php
	
	//consoleTest("hi");
	require_once('bookingsystem.php');
	
	$courseTeacher = $_POST['courseTeacher'];
	$courseBranch = $_POST['courseBranch'];
	$userID = $_POST['userID'];
	$startTime = $_POST['startTime'];//15:00 ~ 16:00
	$userDuration = $_POST['userDuration'];// 2020-02-06 월
	$courseDay = $_POST['courseDay'];
	$startDate = $_POST['startDate'];

	// $courseBranch = $_POST['courseBranch'];
	// $courseDay = $_POST['courseDay'];
	// $courseTeacher = $_POST['courseTeacher'];
	// $startDate = $_POST['startDate'];
	// $userDuration = $_POST['userDuration'];
	// $userID = $_POST['userID'];
	// $startTime = $_POST['startTime'];

	

	// $pt_courseTeacher = "이채정";
	// $pt_courseBranch = "교대";
	// $pt_userID = "testt";
	// $pt_startTime = "15:00 ~ 16:00";//15:00 ~ 16:00
	// $pt_startDateAndDow = "2020-03-16 월";// 2020-02-06 월
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
			$test->acceptRegular($courseTeacher, $courseBranch,$startTime, $endTime ,$startDate, $userID, $dow, "admin");
		}

	}else
	{
		echo "fail";
	}
	

?>