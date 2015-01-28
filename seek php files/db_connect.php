<?php
//set connection variables
$host = "mysql.hostinger.co.uk";
$user = "u559877148_seek";
$pass = "tester123";
$db = "u559877148_seek";

//connect to database
$conn = new mysqli($host, $user, $pass, $db);

//check for connection error
if($conn->connect_error){
die('Error: ('. $conn->connect_errno .') '. $conn->connect_error);
}


/*if($conn == true) {
echo 'Success!';
} else {
echo 'Fail!';
}*/



?>
