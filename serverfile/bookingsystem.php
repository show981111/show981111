<?php
	
	class BookingSystem{

		private $con;
		public $userID;
		public $userBranch;
		public $termStart;
		public $termEnd;
		public $today;

		public function __construct($userBranch)
		{
			$host = 'localhost';
			$user = 'show981111';
			$pass = 'dyd1@tmdwlsfl';
			$db = 'show981111';
			$this->con = mysqli_connect($host, $user, $pass, $db) or die('Unable to connect');
			$this->userBranch = $userBranch;

			$query = "SELECT startDate,endDate FROM TERMLIST"; // get termstart and termEnd 
			$result = mysqli_query($this->con,$query);

			date_default_timezone_set("Asia/Seoul");
			$this->today = date('Y-m-d', time());
			//echo $today;
			if($result)
			{
				while($row = mysqli_fetch_array($result)){
					$termS = strtotime(date("Y-m-d",strtotime($row[0]) ));
					$termE = strtotime(date("Y-m-d",strtotime($row[1]) ));

					if(strtotime($this->today) >= $termS && strtotime($this->today) <= $termE )
					{
						$this->termStart = $row[0];
						$this->termEnd = $row[1];
					}
				}
				//echo $this->termStart." ".$this->termEnd;
			}

		}

		function __destruct()
		{
			mysqli_close($this->con);
		}

		function getUser()
		{
			
			$query = "SELECT userID,userPhone,userBranch FROM USER";
			$result = mysqli_query($this->con,$query);

			$response = array();

			if($result)
			{
				while($row = mysqli_fetch_array($result)){
		
					array_push($response, array("userID"=>$row[0], "userPhone"=>$row[1], "userBranch"=>$row[2]));
				}
			}

			echo json_encode($response,JSON_UNESCAPED_UNICODE);
		}

		function getCourseTimeLine()
		{
			$query = "SELECT courseTeacher,courseDay,startTime,endTime FROM COURSETIMELINE WHERE courseBranch = '$this->userBranch' ";
			$result = mysqli_query($this->con,$query);

			$response = array();

			if($result)
			{
				while($row = mysqli_fetch_array($result)){
		
					array_push($response, array("courseTeacher"=>$row[0], "courseDay"=>$row[1], "startTime"=>$row[2], "endTime" => $row[3]));
				}
			}

			echo json_encode($response,JSON_UNESCAPED_UNICODE);
		}

		function getTimeForMonth($courseDay, $courseTeacher, $startDate, $userDuration)
		{
			//echo "step1";
			$timeList = array();
			//if startDate is before termstart -> put fail 
			if(strtotime(date("Y-m-d", strtotime($startDate) ) ) < strtotime(date("Y-m-d", strtotime($this->termStart) )) )
			{
				array_push($timeList, array("regular_Time" => "FAIL" ) );

				return 0;
			}


			$query = "SELECT startTime, endTime FROM COURSETIMELINE WHERE courseBranch = '$this->userBranch' AND courseDay = '$courseDay' AND courseTeacher = '$courseTeacher' ";

			$result = mysqli_query($this->con, $query);

			$count = 0;
			if($result)
			{

				while($row = mysqli_fetch_array($result)){

					$FormattedStart = date("H:i", strtotime($row[0]));
					$FormattedEnd = date("H:i", strtotime($row[1]));
					//echo " //FormattedStart ".$FormattedStart. " FormattedEnd ".$FormattedEnd. "// ";
					$strStart = strtotime($FormattedStart);
					$strEnd = strtotime($FormattedEnd);

					$candidateStartTime = $strStart;
					$candidateEndTime = strtotime('+30 minutes',$candidateStartTime);;

					$dayofweek = date('w', strtotime($startDate));

					$FormattedStartDate =  date("Y-m-d", strtotime($startDate));
					$strStartDate = strtotime($FormattedStartDate);

					$FormattedTermEnd = date("Y-m-d", strtotime($this->termEnd));

					//echo " //Term End".$FormattedTermEnd. "//";
					
					while($candidateStartTime < $strEnd && $candidateEndTime <= $strEnd)
					{	
						//echo " //candidateStartTime ".date("H:i",$candidateStartTime). " candidateEndTime ".date("H:i",$candidateEndTime). "// ";
						
						$flag = 1; // 해당 시간대가 4주를 다 통과햇나 체크하는 거 
						//시간대 하나 뽑았다 그 해당 하나의 시간대에 대해서 4주짜리가 다있는지 체크해줘야한다. 
						//echo "-----------!";
						while($strStartDate <= strtotime($FormattedTermEnd))
						{
							//$combinedDT = date('Y-m-d H:i:s', strtotime("$date $time"));
							$Formats = date('Y-m-d', $strStartDate);
							$Formatst = date('H:i', $candidateStartTime);

							$cand_StartDateTime = date('Y-m-d H:i',strtotime("$Formats $Formatst")); 
							//echo "candidate DATETIME".$cand_StartDateTime ."//";

							$selectBookedDate =  "SELECT startDate, endDate FROM BOOKEDLIST WHERE courseBranch = '$this->userBranch' AND courseTeacher = '$courseTeacher' AND status = 'BOOKED' ";
							$resDate = mysqli_query($this->con, $selectBookedDate);
							if($resDate)
							{
								while($rowDate = mysqli_fetch_array($resDate))
								{
									//echo " //rowDate ".$rowDate[0]. " //rowed". $rowDate[1];

									$formatRow = date('Y-m-d H:i',strtotime($rowDate[0]));
									$formatRow1 =  date('Y-m-d H:i',strtotime($rowDate[1]));

									if(strtotime($formatRow) <= strtotime($cand_StartDateTime) && strtotime($formatRow1) > strtotime($cand_StartDateTime) )
									{
										$flag = 0;
										break;
									}else
									{
										$flag = 1;
									}
								}
							}

							if($flag == 0) break;

							$strStartDate = strtotime("+7 day",$strStartDate);
							//echo " FFFFFLAG ".$flag;
						}
						$strStartDate = strtotime($FormattedStartDate);

						//echo "!-------------";

						if($flag == 1)
						{
							//echo "pushed ". date("H:i",$candidateStartTime);
							array_push($timeList, array("regular_Time" => date("H:i",$candidateStartTime) ) );
						}	
						

						if($userDuration == "45")
						{
							$candidateStartTime = strtotime('+45 minutes',$candidateStartTime);
							$candidateEndTime = strtotime('+45 minutes',$candidateEndTime);
						}else
						{
							$candidateStartTime = strtotime('+30 minutes',$candidateStartTime);
							$candidateEndTime = strtotime('+30 minutes',$candidateEndTime);
						}
						
						//$candidateStartTime = strtotime($FormattedcandidateStartTime);
						//$candidateEndTime = strtotime($FormattedcandidateEndTime)
					}



				}
			}
			echo json_encode($timeList,JSON_UNESCAPED_UNICODE);



		}

		function getBookedList($userID)
		{
			$query = "SELECT courseTeacher,courseDay,startTime,endTime FROM BOOKEDLIST WHERE userID = '$this->userBranch' ";
			$result = mysqli_query($this->con,$query);

			$response = array();

			if($result)
			{
				while($row = mysqli_fetch_array($result)){
		
					array_push($response, array("courseTeacher"=>$row[0], "courseDay"=>$row[1], "startTime"=>$row[2], "endTime" => $row[3]));
				}
			}

			echo json_encode($response,JSON_UNESCAPED_UNICODE);
		}

		function putExtendTerm($start, $end)
		{
			if(strtotime($this->today) >= strtotime($start) )//오늘보다 이전의 날짜로 연장할수 없음 
			{
				$response = "fail";
				echo $response;
				return 0;
			}

			$start =  date("Y-m-d", strtotime($start));
			$end =  date("Y-m-d", strtotime($end));
			$query = "UPDATE TERMLIST SET startDate = '$start', endDate = '$end' order by UNIX_TIMESTAMP(startDate) ASC LIMIT 1 ";
			$result = mysqli_query($this->con,$query);
			$response;
			if( $mysqli->affected_rows >= 0)
			{
				$response = "success";
			}else
			{
				$response = "fail";
			}

			$getRegular = "SELECT courseTeacher,courseBranch,userID,startTime,endTime,dow FROM REGULARSCHEDULE ";
			$result1 = mysqli_query($this->con,$getRegular);
			$addedStart= strtotime($start);
			//$dayofweek = date('w', strtotime($startDate));
			$regularStart;
			while($row = mysqli_fetch_array($result1))
			{
				$formatStartTime = date('H:i',strtotime($row[3]));
				$formatEndTime = date('H:i',strtotime($row[4]));
				//echo "//format Start ".$formatStartTime. " ". "formatEndTime ".$formatEndTime. " //";
				//$cand_StartDateTime = date('Y-m-d H:i',strtotime("$Formats $Formatst")); 

				while(date('w', $addedStart) != $row[5])//find the first Date that matches dow 다음학기 정기 첫날짜 찾기 위해서 
				{
					$addedStart = strtotime("+1 day",strtotime($start));
					//echo " //addedStart ". date("Y-m-d",$addedStart);
				}
				$regularStart = date('Y-m-d',$addedStart);
				$cand_StartDateTime = date('Y-m-d H:i',strtotime("$regularStart $formatStartTime")); 
				$cand_EndDateTime = date('Y-m-d H:i',strtotime("$regularStart $formatEndTime")); 
				echo " //cand start ".$cand_StartDateTime. " cand End ".$cand_EndDateTime;

				$cand_endDate = date('Y-m-d',strtotime($cand_EndDateTime));

				while(strtotime($cand_endDate) <= strtotime($end) ) // +7씩 해나가면서 텀 끝날때까지 삽입 해준다! 
				{
					$insertquery = "INSERT INTO BOOKEDLIST (courseTeacher, courseBranch, startDate, endDate, userID, ownerID, status) VALUES ('$row[0]', '$row[1]', '$cand_StartDateTime', '$cand_EndDateTime', '$row[2]', '$row[2]', 'BOOKED'  ) ";
					$pushquery = mysqli_query($this->con,$insertquery);
					//$strStartDate = strtotime("+7 day",$strStartDate);
					$cand_StartDateTime = date('Y-m-d H:i',strtotime("+7 day", strtotime($cand_StartDateTime))); 
					$cand_EndDateTime = date('Y-m-d H:i',strtotime("+7 day", strtotime($cand_EndDateTime))); 
					$cand_endDate = date('Y-m-d',strtotime($cand_EndDateTime));
					//echo "////INSIDE while cand start ".$cand_StartDateTime. " cand End ".$cand_EndDateTime." END WHILE////";



				}
			}


			echo $response;
		}

		function getWaitList()
		{
			$query = "SELECT courseTeacher,courseBranch,userID,startTime,endTime,dow,startDate FROM WAITLIST ";
			$result = mysqli_query($this->con, $query);
			$response = array();

			if($result)
			{
				while($row = mysqli_fetch_array($result)){
					$dow;
					switch ($row[5]) {
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
					$startDateAndDow = $row[6]. " ".$dow;
					$timeTillEnd = $row[3]. " ~ ".$row[4];
					array_push($response, array("wl_userID"=>$row[2], "wl_userBranch"=>$row[1], "wl_courseTeacher"=>$row[0], "wl_startDate" => $startDateAndDow, "wl_Time" => $timeTillEnd));
				}
			}

			echo json_encode($response,JSON_UNESCAPED_UNICODE); 
		}


	}

	//$userBranch = $_POST['userBranch'];
	//$test = new BookingSystem($userBranch);
	//$test->getCourseTimeLine();
	//$test->getUser($userBranch);

?>