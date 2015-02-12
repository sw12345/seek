<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//connect to database
include 'db_connect.php';

if(isset($_POST['venue_id'])) { $venue_id = $_POST['venue_id']; }

//make a query
$query = "SELECT SecondSet.venue_id, SecondSet.v_name, SecondSet.v_type, SecondSet.address, SecondSet.post_code, SecondSet.max_cap, SecondSet.v_desc, FirstSet.email_address, FirstSet.phone_nmbr, SecondSet.latitude, SecondSet.longitude FROM (SELECT user_id, email_address, phone_nmbr FROM users) as FirstSet INNER JOIN (SELECT user_id, venue_id, v_name, v_type, address, max_cap, v_desc, post_code, latitude, longitude FROM venue) as SecondSet on FirstSet.user_id = SecondSet.user_id WHERE venue_id = ?";

if ($stmt = $conn->prepare($query)) {
	$stmt-> bind_param('i', $venue_id);

	//send query to db
	$stmt-> execute();
	$stmt->store_result();
	$stmt->bind_result($venue_id, $v_name, $v_type, $address, $post_code, $max_cap, $v_desc, $email_address, $phone_nmbr, $latitude, $longitude);

	//fetch values by looping through each row
	while ($stmt->fetch()) {
		$jsonResponse = array(
			'venueId' => $venue_id,
			'name' => $v_name,
			'type' => $v_type,
			'address' => $address,
			'postcode' => $post_code,
			'maxCap' => $max_cap,
			'description' => $v_desc,
			'email' => $email_address,
			'phone' => $phone_nmbr,
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
