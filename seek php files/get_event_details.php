<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//connect to database
include 'db_connect.php';

if(isset($_POST['event_id'])) { $event_id = $_POST['event_id']; }

//make a query
$query = "SELECT event_id, e_name, start_date, end_date, address, post_code1, post_code2, post_code, max_cap, reg_link, e_desc FROM event WHERE event_id = ?";

if ($stmt = $conn->prepare($query)) {
	$stmt-> bind_param('i', $event_id);

	//send query to db
	$stmt-> execute();
	$stmt->store_result();
	$stmt->bind_result($event_id, $e_name, $start_date, $end_date, $address, $post_code1, $post_code2, $post_code, $max_cap, $reg_link, $e_desc);

	//fetch values by looping through each row
	while ($stmt->fetch()) {
		$jsonResponse = array(
			'eventId' => $event_id,
			'name' => $e_name,
			'startdate' => $start_date,
			'enddate' => $end_date,
			'maxCap' => $max_cap,
			'address' => $address,
			'postcode1' => $post_code1,
			'postcode2' => $post_code2,
			'postCode' => $post_code,
			'regLink' => $reg_link,
			'description' => $e_desc
		);
		json_encode($jsonResponse);
	};

	// success
	//$response["success"] = 1;
	echo json_encode($jsonResponse);

	//close statement
	$stmt->close();

}

//close connection
$conn->close();

?>