<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//connect to database
include 'db_connect.php';

//make a query
$query = "SELECT user_id, venue_id, v_name, v_type, post_code, max_cap, v_desc FROM venue";

if ($result = $conn->query($query)) {
/*	$stmt->bind_param("i", $user_id);
	$stmt->execute();
	$stmt->store_result();
	$stmt->bind_result($venue_id, $v_name, $v_type, $post_code, $max_cap, $v_desc);*/

	//fetch values by looping through each row
	while ($row = $result->fetch_assoc()) {

		$jsonResponse []= array(
			'userId' => $row['user_id'],
			'venueId' => $row['venue_id'],
			'name' => $row['v_name'],
			'venueType' => $row['v_type'],
			'maxCap' => $row['max_cap'],
			'postCode' => $row['post_code'],
			'description' => $row['v_desc'],
			'error' => "You have not created any venue."

		);
	}
}

echo json_encode($jsonResponse);

//close statement
$result->close();

//close connection
$conn->close();

?>
