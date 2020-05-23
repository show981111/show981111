# show981111
SolViolin application ver.1

프론트엔드 : JAVA
백엔드 : PHP 

기능: 회원가입/수업 취소,예약

수업예약관련 구체적 기능 
1. 정기예약 : 앞으로들을 수업에 대한 시간 변경
2. 보강예약 : 당일 수업 취소 및 보강 : 수강가능한 시간을 button grid view 를 통해 보여줌
제한사항들 - 수업시작 4시간 전에는 변경 불가능, 한달에 두번까지만 취소가 가능, 취소했지만 보강받지 못한 수업의 경우
이후 한달까지만 이월이 가능하고 1달이 지나면 보강을 못받음. 갑작스러운 학원 휴원 및 선생님 사정으로 인한 수업 취소의 경우 
취소한 횟수에 들어가지 않음


데이터베이스와의 통신으로는 OKhttp 를 사용했으며 JSON DATA를 받는데는 GSON 을 사용
