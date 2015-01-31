<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//connect to database
include 'db_connect.php';

if(isset($_POST['venue_id'])) { $venue_id = $_POST['venue_id']; }

//make a query
$query = "SELECT venue_id, v_name, v_type, address, post_code, max_cap, v_desc FROM venue WHERE venue_id = ?";

if ($stmt = $conn->prepare($query)) {
	$stmt-> bind_param('i', $venue_id);

	//send query to db
	$stmt-> execute();
	$stmt->store_result();
	$stmt->bind_result($venue_id, $v_name, $v_type, $address, $post_code, $max_cap, $v_desc);

	//fetch values by looping through each row
	while ($stmt->fetch()) {
		$jsonResponse = array(
			'venueId' => $venue_id,
			'name' => $v_name,
			'type' => $v_type,
			'address' => $address,
			'maxCap' => $max_cap,
			'postcode' => $post_code,
			'description' => $v_desc
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
