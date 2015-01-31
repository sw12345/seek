<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//establish connection with db
include 'db_connect.php';

if (isset($_POST['venue_id'])) {
	$venue_id = $_POST['venue_id'];
	$v_name = $_POST['v_name'];
	$v_type = $_POST['v_type'];
	$post_code = $_POST['post_code'];
	$max_cap = $_POST['max_cap'];
	$v_desc = $_POST['v_desc'];
	
	//make a query
	$query = "UPDATE venue SET
	v_name = ?,
	v_type = ?,
	post_code = ?,
	max_cap = ?,
	v_desc = ?
	
	WHERE venue_id = ?";
	
	//prepare statement
	if ($stmt = $conn->prepare($query)) {
		$stmt->bind_param(
			"sssssisi",
			$_POST['v_name'],
			$_POST['v_type'],
			$_POST['post_code'],
			$_POST['max_cap'],
			$_POST['v_desc'],
			$_POST['venue_id']
			);
		$stmt->execute();
		
		//update successful
		$response["success"] = 1;
		$response["message"] = "Venue updated";
		
		echo json_encode($response);
		
	} else {
		//Unable to update
		$response["success"] = 0;
		$response["message"] = "Unable to update";
		
		echo json_encode($response);
		
		//close statement
		$stmt-> close();
	}
	
	//close connection
	$conn-> close();
}
?>
