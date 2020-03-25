<?php
	
	class BookingSystem{

		private $con;
		public $userID;
		public $userBranch;
		public $past_termStart;
		public $past_termEnd;
		public $cur_termStart;
		public $cur_termEnd;
		public $future_termStart;
		public $future_termEnd;
		public $today;

		public function __construct($userBranch)
		{
			$host = 'localhost';
			$user = 'show981111';
			$pass = 'dyd1@tmdwlsfl';
			$db = 'show981111';
			$this->con = mysqli_connect($host, $user, $pass, $db) or die('Unable to connect');
			$this->userBranch = $userBranch;

		}

		function getTermList($isEchoNeeded)
		{
			$query = "SELECT startDate,endDate FROM TERMLIST order by UNIX_TIMESTAMP(startDate) ASC "; // get termstart and termEnd 
			$result = mysqli_query($this->con,$query);

			date_default_timezone_set("Asia/Seoul");
			$this->today = date('Y-m-d', time());
			//echo $today;
			$termList = array();
			$count = 0;
			$todayIndex = 0;
			$tempS;
			$tempE;
			$termS;
			$termE;
			if($result)
			{
				while($row = mysqli_fetch_array($result)){
					
					$tempS = $termS;
					$tempE = $termE;
				
					$termS = strtotime(date("Y-m-d",strtotime($row[0]) ));
					$termE = strtotime(date("Y-m-d",strtotime($row[1]) ));

					if(strtotime($this->today) >= $termS && strtotime($this->today) <= $termE )
					{
						$this->cur_termStart = $row[0];
						$this->cur_termEnd = $row[1];
						$this->past_termStart = date("Y-m-d", $tempS );
						$this->past_termEnd = date("Y-m-d", $tempE );
						array_push($termList, array("termStart"=>$this->past_termStart, "termEnd"=>$this->past_termEnd ));
						array_push($termList, array("termStart"=>$this->cur_termStart, "termEnd"=>$this->cur_termEnd ));
						$todayIndex = $count;
					}
					if( ($todayIndex + 1) == $count)
					{
						$this->future_termStart = $row[0];
						$this->future_termEnd = $row[1];
						array_push($termList, array("termStart"=>$this->future_termStart, "termEnd"=>$this->future_termEnd ));
					}
					$count = $count + 1;
				}
				//echo $this->termStart." ".$this->termEnd;
			}
			if($isEchoNeeded == "yes")
			{
				echo json_encode($termList,JSON_UNESCAPED_UNICODE);
			}
			

		}

		function __destruct()
		{
			mysqli_close($this->con);
		}

		function getUser($userID,$userBranch)
		{
			
			$query = "SELECT userID,userBranch,userDuration,userName FROM USER WHERE userID = '$userID' AND userBranch = '$userBranch' ";
			$result = mysqli_query($this->con,$query);

			$response = array();

			if($result)
			{
				while($row = mysqli_fetch_array($result)){
		
					array_push($response, array("userID"=>$row[0], "userBranch"=>$row[1], "userDuration"=>$row[2],"userName"=>$row[3] ));
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

		function getTimeForMonth($courseDay, $courseTeacher, $startDate, $userDuration,$userName)//BOOKEDLIST 를 기준으로 필터링한다 따라서 다음학기예정인 사람같은 경우는 필터안한다
		{
			//echo "step1";
			$timeList = array();
			//if startDate is before termstart -> put fail 
			$this->getTermList("no");
			if(strtotime(date("Y-m-d", strtotime($startDate) ) ) < strtotime(date("Y-m-d", strtotime($this->cur_termStart) )) )
			{
				array_push($timeList, array("regular_Time" => "FAIL" ) );
				echo $timeList;
				return 0;
			}
			$FormattedTermEnd;
			if($userName != "admin")
			{
				$FormattedTermEnd = date("Y-m-d", strtotime($this->cur_termEnd));
				//if user is student -> if startDate is after future Term start -> put fail
				if(strtotime(date("Y-m-d", strtotime($startDate) ) ) > strtotime(date("Y-m-d", strtotime($this->future_termStart) )) )
				{
					array_push($timeList, array("regular_Time" => "FAIL" ) );
					echo $timeList;
					return 0;
				}
			}else
			{
				$FormattedTermEnd = date("Y-m-d", strtotime($this->future_termEnd));
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
					$candidateEndTime = strtotime('+30 minutes',$candidateStartTime);

					$dayofweek = date('w', strtotime($startDate));

					$FormattedStartDate =  date("Y-m-d", strtotime($startDate));
					$strStartDate = strtotime($FormattedStartDate);

					// $FormattedTermEnd = date("Y-m-d", strtotime($this->cur_termEnd));

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
							$Formatet = date('H:i', $candidateEndTime);

							$cand_StartDateTime = date('Y-m-d H:i',strtotime("$Formats $Formatst")); 
							$cand_EndDateTime = date('Y-m-d H:i',strtotime("$Formats $Formatet")); 
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
									//(strtotime($cand_StartDateTime) >= strtotime($formatRow) && strtotime($cand_StartDateTime)<= strtotime($formatRow1)) || (strtotime($cand_EndDateTime) >= strtotime($formatRow) && strtotime($cand_EndDateTime)<= strtotime($formatRow1))
									if( (strtotime($cand_StartDateTime) >= strtotime($formatRow) && strtotime($cand_StartDateTime) < strtotime($formatRow1)) || (strtotime($cand_EndDateTime) > strtotime($formatRow) && strtotime($cand_EndDateTime)<= strtotime($formatRow1)) )
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
						}else if($userDuration == "30")
						{
							$candidateStartTime = strtotime('+30 minutes',$candidateStartTime);
							$candidateEndTime = strtotime('+30 minutes',$candidateEndTime);
						}else if($userDuration == "60")
						{
							$candidateStartTime = strtotime('+60 minutes',$candidateStartTime);
							$candidateEndTime = strtotime('+60 minutes',$candidateEndTime);
						}
						
						//$candidateStartTime = strtotime($FormattedcandidateStartTime);
						//$candidateEndTime = strtotime($FormattedcandidateEndTime)
					}



				}
			}
			echo json_encode($timeList,JSON_UNESCAPED_UNICODE);



		}

		function cancelCourse($userID,$cancelTeacher, $cancelBranch, $startDate, $endDate)
		{
			$checkCredit = "SELECT userCredit FROM USER WHERE userID = '$userID' ";
			$checkCreditRes = mysqli_query($this->con,$checkCredit);
			if($checkCreditRes)
			{
				while($row = mysqli_fetch_array($checkCreditRes)){
					if($row[0] <= 0)
					{
						echo "creditOver";
						return;
					}
				
				}
			}

			$checkRedunt = "SELECT * FROM BOOKEDLIST WHERE userID = '$userID' AND courseTeacher = '$cancelTeacher' AND courseBranch = '$cancelBranch' AND startDate = '$startDate' AND endDate = '$endDate' AND status = 'canceled' ";
			$checkRes = mysqli_query($this->con,$checkRedunt);
			if(mysqli_num_rows($checkRes) > 0)
			{
				echo "already";
				return;
			}


			$cancelQuery = "UPDATE BOOKEDLIST SET status = 'canceled' WHERE userID = '$userID' AND courseTeacher = '$cancelTeacher' AND courseBranch = '$cancelBranch' AND startDate = '$startDate' AND endDate = '$endDate' ";
			$setCredit = "UPDATE USER SET userCredit = userCredit - 1 WHERE userID = '$userID' ";

			$cancelres = mysqli_query($this->con, $cancelQuery);

			if(mysqli_affected_rows($this->con) > 0)
			{
				$setres = mysqli_query($this->con, $setCredit);
				if(mysqli_affected_rows($this->con) > 0)
				{
					$response = "success";
				}else{
					$response = "fail";
				}

			}else
			{
				$response = "fail";
			}
			echo $response;
		}

		function getBookedList($userID, $option)
		{
			$fetchStart = "";
			$fetchEnd = "";

			$this->getTermList("no");
			if($option == "cur")
			{
				$fetchStart= $this->cur_termStart;
				$fetchEnd = $this->cur_termEnd;
			}else if($option == "cancelAll")
			{
				$fetchStart= $this->past_termStart;
				$fetchEnd = $this->cur_termEnd;
			}else if($option == "cancel_cur")
			{
				$fetchStart= $this->cur_termStart;
				$fetchEnd = $this->cur_termEnd;
			}
			else
			{
				$fetchStart= $this->past_termStart;
				$fetchEnd = $this->past_termEnd;
			}
			$response = array();
			if($fetchStart != "" && $fetchEnd != "")
			{
				$query = "";
				if($option == "cancel_cur")
				{
					$query = "SELECT courseTeacher,courseBranch,startDate,endDate,status FROM BOOKEDLIST WHERE userID = '$userID' AND status = 'canceled' order by UNIX_TIMESTAMP(startDate) DESC ";//현재 학기에 취소한 횟수를 알기위한 쿼리
				}else if($option == "cancelAll")
				{
					$query = "SELECT courseTeacher,courseBranch,startDate,endDate,status FROM BOOKEDLIST WHERE userID = '$userID' AND (status = 'canceled' OR status = 'closeCanceled') order by UNIX_TIMESTAMP(startDate) DESC ";//이번학기 지난학기 취소한 수업을 알기위한 쿼리
				}else
				{
					$query = "SELECT courseTeacher,courseBranch,startDate,endDate,status FROM BOOKEDLIST WHERE userID = '$userID' AND status = 'BOOKED' order by UNIX_TIMESTAMP(startDate) DESC ";//예약되어있는 수업을 파싱
				}
				if($query != "")
				{
					$result = mysqli_query($this->con,$query);

					if($result)
					{
						while($row = mysqli_fetch_array($result)){
							$startMonth = date("Y-m-d",strtotime($row[2]));
							$endMonth = date("Y-m-d",strtotime($row[3]));
							if(strtotime($startMonth) < strtotime($fetchStart) && strtotime($endMonth) < strtotime($fetchEnd)  )
							{
								break;//만약 시작날 끝나는 둘다 파싱하려는 시작날보다 작다면 바로 브레이크 내림차순 정렬이니까 
							}
							if( strtotime($fetchStart) <= strtotime($startMonth) && strtotime($fetchStart) <= strtotime($startMonth) && strtotime($fetchEnd) >= strtotime($endMonth) && strtotime($fetchEnd) >= strtotime($endMonth)  )
							{
								array_push($response, array("bookedTeacher"=>$row[0], "bookedBranch"=>$row[1], "bookedStartDate"=>$row[2], "bookedEndDate" => $row[3], "status" => $row[4]));
							}
							
						}

					}
				}else
				{
					echo "fail";
					return;
				}
				
			}
			echo json_encode($response,JSON_UNESCAPED_UNICODE);
		}

		function putExtendTerm($start, $end)//학기 연장은 수정이 거의 불가능하니까 존나 신중해야됨 
		{
			$response;
			if($start == "" && $end == "")
			{
				$this->getTermList("no");
				$start = $this->future_termStart;
				$end = $this->future_termEnd;
			}else{

				if(strtotime($this->today) >= strtotime($start) )//오늘보다 이전의 날짜로 연장할수 없음 
				{
					$response = "fail";
					echo $response;
					return 0;
				}

				$start =  date("Y-m-d", strtotime($start));
				$end =  date("Y-m-d", strtotime($end));
				$this->getTermList("no");

				if(strtotime($start) >= strtotime($this->future_termStart) && strtotime($start) < strtotime($this->future_termEnd) )
				{
					$response = "redunt";
					echo $response;
					return 0;
				}//already extended in termList
				if(strtotime($end) >= strtotime($this->future_termStart) && strtotime($end) < strtotime($this->future_termEnd) )
				{
					$response = "redunt";
					echo $response;
					return 0;
				}//already extended in termList 

				$query = "UPDATE TERMLIST SET startDate = '$start', endDate = '$end' order by UNIX_TIMESTAMP(startDate) ASC LIMIT 1 ";
				$result = mysqli_query($this->con,$query);
				if(mysqli_affected_rows($this->con) > 0)
				{
					$response = "success";
				}else
				{
					$response = "fail";
					return $response;
				}
			}
			//echo $start." ";


			$getRegular = "SELECT courseTeacher,courseBranch,userID,startTime,endTime,dow,startDate FROM REGULARSCHEDULE WHERE extendedDate <> '$start' AND status <> 'false' ";//가장 최근 정기예약 날짜 확인 
			$result1 = mysqli_query($this->con,$getRegular);                                                    //이미 연장된 학생은 뺼수잇도록 
			$addedStart= strtotime($start);
			//$dayofweek = date('w', strtotime($startDate));
			$regularStart;

			if(mysqli_num_rows ( $result1 ) <= 0){
				$response = "nothing";
			}

			while($row = mysqli_fetch_array($result1))
			{
				$formatStartTime = date('H:i',strtotime($row[3]));
				$formatEndTime = date('H:i',strtotime($row[4]));
				//echo "//format Start ".$formatStartTime. " ". "formatEndTime ".$formatEndTime." dow".$row[5]." " .date('w', $addedStart) ." //";
				//$cand_StartDateTime = date('Y-m-d H:i',strtotime("$Formats $Formatst")); 
				
				if(strtotime($row[6]) > strtotime($start) )//if startDate is after termStart 
				{
					$addedStart = strtotime($row[6]);
				}else{
					while(date('w', $addedStart) != $row[5])//find the first Date that matches dow 다음학기 정기 첫날짜 찾기 위해서 
					{
						$addedStart = strtotime("+1 day",$addedStart);
						//echo " //addedStart ". date("Y-m-d",$addedStart);
					}
				}

				$regularStart = date('Y-m-d',$addedStart);
				$cand_StartDateTime = date('Y-m-d H:i',strtotime("$regularStart $formatStartTime")); 
				$cand_EndDateTime = date('Y-m-d H:i',strtotime("$regularStart $formatEndTime")); 
				//echo " //cand start ".$cand_StartDateTime. " cand End ".$cand_EndDateTime;

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


				$insertRecentExtend = "UPDATE REGULARSCHEDULE SET extendedDate = '$start' WHERE userID = '$row[2]' ";
				$updatequery = mysqli_query($this->con,$insertRecentExtend);       
				if(mysqli_affected_rows($this->con) > 0)
				{
					$response = "success";
				}else
				{
					$response = "notupdated";
				}
			}



			echo $response;
			return $response;
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

		function putWaitList($courseTeacher, $courseBranch, $userID, $startTime, $endTime, $dow,$startDate, $todayDateTime)
		{
			$response = "fail";

			$findNow = "SELECT startTime, endTime, dow FROM REGULARSCHEDULE WHERE userID = 'userID' AND courseTeacher = '$courseTeacher' AND courseBranch = '$courseBranch' ";

			$NowRes = mysqli_query($this->con,$findNow);
			if(mysqli_num_rows ( $NowRes ) > 0)
			{
				$response = "alreadyBooked";
				echo $response;
				return 0;

			}

			$findquery = "SELECT * FROM WAITLIST WHERE courseTeacher = '$courseTeacher' AND courseBranch = '$courseBranch' AND userID = '$userID' AND startTime = '$startTime' AND endTime = '$endTime' AND dow = '$dow' AND startDate = '$startDate' ";

			$result = mysqli_query($this->con,$findquery);

			if(mysqli_num_rows ( $result ) > 0)
			{
				$response = "already";
				echo $response;
				return 0;

			}

			$insertquery = "INSERT INTO WAITLIST (courseTeacher, courseBranch, userID, startTime, endTime, dow, startDate, askDate) VALUES ('$courseTeacher', '$courseBranch', '$userID', '$startTime', '$endTime', '$dow', '$startDate', '$todayDateTime'  ) ";

			$pushquery = mysqli_query($this->con,$insertquery);

			if($pushquery)
			{
				$response = "success";
			}else
			{
				$response = "internet_fail";
			}

			echo $response;

		}

		function acceptRegular($pt_courseTeacher, $pt_courseBranch,$startTime, $endTime ,$startDate, $pt_userID,$dow,$userName)
		{
			$response;
			$this->getTermList("no");
			$selectSame = "SELECT * FROM REGULARSCHEDULE WHERE courseTeacher = '$pt_courseTeacher'AND courseBranch = '$pt_courseBranch'AND startTime = '$startTime'AND endTime = '$endTime'AND dow = '$dow' AND  userID = '$pt_userID' ";
			$sameRes = mysqli_query($this->con,$selectSame);
			if(mysqli_num_rows ( $sameRes ) > 0)
			{
				$response = "already";
				if($userName != "admin")
				{
					$response = $this->deleteWaitList($pt_courseTeacher, $pt_courseBranch,$startTime, $endTime ,$startDate, $pt_userID,$dow);
					if($response == "success")
					{
						$response = "already";
					}
				}
				echo $response;
				return 0;
				
			}
			//filterMonth($pt_courseTeacher, $pt_courseBranch,$startTime, $endTime ,$startDate, $endChangeDate)
			$filterRes = $this->filterMonth($pt_courseTeacher, $pt_courseBranch,$startTime, $endTime ,$startDate, $this->cur_termEnd, $dow);
			if($filterRes == "notEmpty")//해당 정기예약이 비어있지 않는 경우...
			{
				echo "notEmpty";
				return 0;
			}else
			{
				$updatequery = "UPDATE REGULARSCHEDULE SET courseTeacher = '$pt_courseTeacher',courseBranch = '$pt_courseBranch', startTime = '$startTime', endTime = '$endTime', startDate = '$startDate',dow = '$dow',extendedDate = '$this->cur_termStart' WHERE userID = '$pt_userID' ";
				$updateResult = mysqli_query($this->con,$updatequery);
				if(mysqli_affected_rows($this->con) > 0)
				{
					$response = "success";
				}else
				{
					$insertquery = "INSERT INTO REGULARSCHEDULE (courseTeacher, courseBranch, startTime, endTime, dow, startDate, extendedDate, userID) VALUES ('$pt_courseTeacher', '$pt_courseBranch', '$startTime', '$endTime', '$dow', '$startDate', '$this->cur_termStart', '$pt_userID'  ) ";

					$insertResult = mysqli_query($this->con,$insertquery);
					if(mysqli_affected_rows($this->con) > 0)
					{
						$response = "success";
					}else
					{
						$response = "internet_fail_insert_Regular";
						echo $response;
						return 0;
					}
				}

				if(strtotime($startDate) >= strtotime($this->cur_termStart) && strtotime($startDate) <= strtotime($this->cur_termEnd) )//현 학기를 신청햇다면 
				{	

					$response = $this->changeRegular($pt_courseTeacher, $pt_courseBranch,$startTime, $endTime ,$startDate, $pt_userID, $this->cur_termEnd);//유저가 신청했을 경우 현학기 내부에서만 조절 가능하므로 끝나는 날은 현학기 종료일
					//echo $response. "changeRegular";
					echo $userName;
					if($response == "success" && $userName != "admin")
					{
						$response = $this->deleteWaitList($pt_courseTeacher, $pt_courseBranch,$startTime, $endTime ,$startDate, $pt_userID,$dow,"no");
					}
				}
				echo $response;
			}
		}

		function changeRegular($pt_courseTeacher, $pt_courseBranch,$startTime, $endTime ,$startDate, $pt_userID,$endChangeDate)
		{
			$response;
			$selectquery = "SELECT startDate FROM BOOKEDLIST WHERE userID = '$pt_userID' AND status = 'BOOKED' AND ownerID = '$pt_userID' ";
			$fetch = mysqli_query($this->con,$selectquery);
			if(mysqli_num_rows ( $fetch ) > 0)// 기존 사용자의 경우 기존 정기예약이 있겟지!
			{
				while($row = mysqli_fetch_array($fetch))
				{
					$FormatStartDate = date('Y-m-d', strtotime($row[0]));

					$changedStartDate = strtotime($startDate);
					$tochangeStartDate = strtotime($FormatStartDate);

					$isSameWeek = date('oW', $changedStartDate) === date('oW', $tochangeStartDate) && date('Y', $changedStartDate) === date('Y', $tochangeStartDate);
					if($isSameWeek || strtotime($startDate) < strtotime($row[0]))
					{
						$deletequery = "DELETE FROM BOOKEDLIST WHERE userID = '$pt_userID' AND status = 'BOOKED' AND ownerID = '$pt_userID' AND startDate = '$row[0]' ";
						$delete = mysqli_query($this->con,$deletequery);
						if(mysqli_affected_rows($this->con) > 0)
						{
							$response = "success";
						}else
						{
							$response = "internet_fail_delete_BookedList";
							return $response;
						}
					}//같은 주에 있는 거부터 변경된 날 이후로 다 삭제
					

				}
				
				// 아래랑 똑같다 ;; //
				$FormatStart = date('Y-m-d', strtotime($startDate) );
				$FormatStartTime = date('H:i', strtotime($startTime));
				$FormatEndTime = date('H:i', strtotime($endTime));

				$tempStart = date('Y-m-d H:i',strtotime("$FormatStart $FormatStartTime")); 
				$tempEnd = date('Y-m-d H:i',strtotime("$FormatStart $FormatEndTime")); 

				while(strtotime($tempStart) <= strtotime($endChangeDate) && strtotime($tempStart) <= strtotime($this->cur_termEnd) )//시작부터 끝까지(최대 현학기 끝) 삽입해줘 
				{
					$insertNewDate = "INSERT BOOKEDLIST (courseTeacher, courseBranch, startDate, endDate, userID, ownerID, status) VALUES ('$pt_courseTeacher', '$pt_courseBranch', '$tempStart', '$tempEnd', '$pt_userID', '$pt_userID', 'BOOKED'  ) ";
					$insertNewRes = mysqli_query($this->con, $insertNewDate);
					if($insertNewRes)
					{
						$response = "success";
					}else
					{
						$response = "internet_fail_insert_BookedList";
					}

					
					$tempStart = date('Y-m-d H:i',strtotime("+7 day",strtotime($tempStart) ));
					$tempEnd = date('Y-m-d H:i',strtotime("+7 day",strtotime($tempEnd) ));
					//echo " FFFFFLAG ".$flag;
				}
				// 아래랑 똑같다 ;; //
				
			}else
			{
				// 아래랑 똑같다 ;; //
				$FormatStart = date('Y-m-d', strtotime($startDate) );
				$FormatStartTime = date('H:i', strtotime($startTime));
				$FormatEndTime = date('H:i', strtotime($endTime));

				$tempStart = date('Y-m-d H:i',strtotime("$FormatStart $FormatStartTime")); 
				$tempEnd = date('Y-m-d H:i',strtotime("$FormatStart $FormatEndTime")); 

				//echo $tempStart. " ". $tempEnd. " before";
				while(strtotime($tempStart) <= strtotime($endChangeDate) && strtotime($tempStart) <= strtotime($this->cur_termEnd) )//시작부터 끝까지(최대 현학기 끝) 삽입해줘 
				{
					$insertNewDate = "INSERT BOOKEDLIST (courseTeacher, courseBranch, startDate, endDate, userID, ownerID, status) VALUES ('$pt_courseTeacher', '$pt_courseBranch', '$tempStart', '$tempEnd', '$pt_userID', '$pt_userID', 'BOOKED'  ) ";
					$insertNewRes = mysqli_query($this->con, $insertNewDate);
					if($insertNewRes)
					{
						$response = "success";
						//echo $tempStart. " ". $tempEnd. " llll";
					}else
					{
						$response = "internet_fail_insert_BookedList";
					}
					
					$tempStart = date('Y-m-d H:i',strtotime("+7 day",strtotime($tempStart) ));
					$tempEnd = date('Y-m-d H:i',strtotime("+7 day",strtotime($tempEnd) ));
					//echo " FFFFFLAG ".$flag;
				}
				// 아래랑 똑같다 ;; //
			}

			return $response;
		}

		function deleteWaitList($pt_courseTeacher, $pt_courseBranch,$startTime, $endTime ,$startDate, $pt_userID,$dow,$isEchoNeeded)
		{
			//echo $pt_courseTeacher." ".$pt_courseBranch." ".$startTime. " ".$endTime. " ".$pt_userID." ".$dow." ".$startDate;
			$deleteWait = "DELETE FROM WAITLIST WHERE courseTeacher = '$pt_courseTeacher' AND courseBranch ='$pt_courseBranch' AND startTime = '$startTime' AND endTime = '$endTime' AND startDate = '$startDate' AND dow = '$dow' AND userID = '$pt_userID' ";

			$deleteRes = mysqli_query($this->con,$deleteWait);

			if(mysqli_affected_rows($this->con) > 0)
			{
				$response = "success";
			}else
			{
				$response = "internet_fail_delete_WaitList";
			}
			if($isEchoNeeded == "yes")
			{
				echo "delete_success";
			}
			return $response;
		}

		function filterMonth($pt_courseTeacher, $pt_courseBranch,$startTime, $endTime ,$startDate, $endChangeDate, $dow)
		{
			$Formats = date('Y-m-d', strtotime($startDate));
			$Formatst = date('H:i', strtotime($startTime));
			$Formatet = date('H:i', strtotime($endTime));
			$flag = 1;
			$cand_StartDateTime = date('Y-m-d H:i',strtotime("$Formats $Formatst")); 
			$cand_EndDateTime = date('Y-m-d H:i',strtotime("$Formats $Formatet")); 
			//echo "candidate DATETIME".$cand_StartDateTime ."//";
			if(strtotime($cand_StartDateTime) > strtotime($endChangeDate))//다음학기의 예약인 경우 
			{
				$selectSame = "SELECT * FROM REGULARSCHEDULE WHERE courseTeacher = '$pt_courseTeacher'AND courseBranch = '$pt_courseBranch'AND startTime = '$startTime'AND endTime = '$endTime'AND dow = '$dow' AND  status <> 'false' ";
				$sameRes = mysqli_query($this->con,$selectSame);
				if(mysqli_num_rows ( $sameRes ) > 0)
				{
					return "notEmpty";
				}
			}
			while(strtotime($cand_StartDateTime) <= strtotime($endChangeDate))
			{
				//날짜 하나에 대해서 검사
				$selectBookedDate =  "SELECT startDate, endDate FROM BOOKEDLIST WHERE courseBranch = '$pt_courseBranch' AND courseTeacher = '$pt_courseTeacher' AND status = 'BOOKED' ";
				$resDate = mysqli_query($this->con, $selectBookedDate);
				if($resDate)
				{
					while($rowDate = mysqli_fetch_array($resDate))
					{
						//echo " //rowDate ".$rowDate[0]. " //rowed". $rowDate[1];

						$formatRow = date('Y-m-d H:i',strtotime($rowDate[0]));
						$formatRow1 =  date('Y-m-d H:i',strtotime($rowDate[1]));
						
						if( (strtotime($cand_StartDateTime) >= strtotime($formatRow) && strtotime($cand_StartDateTime) < strtotime($formatRow1)) || (strtotime($cand_EndDateTime) > strtotime($formatRow) && strtotime($cand_EndDateTime)<= strtotime($formatRow1)) )
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

				$cand_StartDateTime = date('Y-m-d H:i',strtotime("+7 day",strtotime($cand_StartDateTime) ));
				$cand_EndDateTime = date('Y-m-d H:i',strtotime("+7 day",strtotime($cand_EndDateTime) ));
				//$endTime = date("H:i",strtotime('+60 minutes',$tmpTime));
			}

			if($flag == 0)
			{
				return "notEmpty";
			}else
			{
				return "success";
			}
		}

		function extendRequest($userID, $extendTeacher,$extendBranch,$extendStartDate )
		{
			//closecanceled, canceled, extending(이번학기,저번학기 다합해서) 조회를 한다 order by asc 제일 첫번째꺼(제일 오래된것) 하나 잡는다 
			// 1) closecancel, cancel 을 잡은경우 -> 그거 status extending 으로 세팅/ 해당 수업의 extendDate 에  
			// 2) extending 을 잡은경우 -> 
		}




	}

	//$userBranch = $_POST['userBranch'];
	//$test = new BookingSystem($userBranch);
	//$test->getCourseTimeLine();
	//$test->getUser($userBranch);

?>