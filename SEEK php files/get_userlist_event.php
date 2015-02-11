<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//connect to database
include 'db_connect.php';

/*if(isset($_POST['user_id'])) { $email_address = $_POST['user_id']; }*/

//make a query
$query = "SELECT user_id, event_id, e_name, post_code, max_cap, reg_link, e_desc FROM event";

if ($result = $conn->query($query)) {

	//fetch values by looping through each row
	while ($row = $result->fetch_assoc()) {

		$jsonResponse[] = array(
			'userId' => $row['user_id'],
			'eventId' => $row['event_id'],
			'name' => $row['e_name'],
			'maxCap' => $row['max_cap'],
			'postCode' => $row['post_code'],
			'regLink' => $row['reg_link'],
			'description' => $row['e_desc']

		);
	}

}

echo json_encode($jsonResponse);

//close statement
$result->close();

//close connection
$conn->close();

?>
