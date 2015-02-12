<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//connect to database
include 'db_connect.php';

//check value is set or not
if(isset($_POST['event_id'])) { $event_id = $_POST['event_id']; }

//make a query
$query = "SELECT event_id, e_name, start_date, end_date, address, post_code, max_cap, reg_link, e_desc, latitude, longitude FROM event WHERE event_id = ?";

if ($stmt = $conn->prepare($query)) {
	$stmt-> bind_param('i', $event_id);

	//send query to db
	$stmt-> execute();
	$stmt->store_result();
	$stmt->bind_result($event_id, $e_name, $start_date, $end_date, $address, $post_code, $max_cap, $reg_link, $e_desc, $latitude, $longitude);

	//fetch values by looping through each row
	while ($stmt->fetch()) {
		$jsonResponse = array(
			'eventId' => $event_id,
			'name' => $e_name,
			'startdate' => $start_date,
			'enddate' => $end_date,
			'maxCap' => $max_cap,
			'address' => $address,
			'postcode' => $post_code,
			'regLink' => $reg_link,
			'description' => $e_desc,
			'latitude' => $latitude,
			'longitude' => $longitude
		);
		json_encode($jsonResponse);
	};

	echo json_encode($jsonResponse);

	//close statement
	$stmt->close();

}

//close connection
$conn->close();

?>
