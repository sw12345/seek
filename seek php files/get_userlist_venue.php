<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//connect to database
include 'db_connect.php';

//make a query
$query = "SELECT venue_id, v_name, v_type, post_code1, post_code2, post_code, max_cap, v_desc FROM venue";

if ($result = $conn->query($query)) {

	//fetch values by looping through each row
	while ($row = $result->fetch_assoc()) {

		$jsonResponse[] = array(
			'venueId' => $row['venue_id'],
			'name' => $row['v_name'],
			'venueType' => $row['v_type'],
			'maxCap' => $row['max_cap'],
			'postcode1' => $row['post_code1'],
			'postcode2' => $row['post_code2'],
			'postCode' => $row['post_code'],
			'description' => $row['v_desc']

		);
	}

	json_encode($jsonResponse);
}

// success
//$jsonResponse["success"] = 1;
echo json_encode($jsonResponse);

//close statement
$result->close();

//close connection
$conn->close();

?>
