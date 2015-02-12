<?php
//set connection variables
$host = "";
$user = "";
$pass = "";
$db = "";

//connect to database
$conn = new mysqli($host, $user, $pass, $db);

//check for connection error
if($conn->connect_error){
die('Error: ('. $conn->connect_errno .') '. $conn->connect_error);
}

//check if connection is working or not
/*if($conn == true) {
echo 'Success!!';
} else {
echo 'Fail!';
}*/



?>
