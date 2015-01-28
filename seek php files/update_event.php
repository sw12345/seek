<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//establish connection with db
include 'db_connect.php';

if (isset($_POST['event_id'])) {
	$event_id = $_POST['event_id'];
	$e_name = $_POST['e_name'];
	$start_date = $_POST['start_date'];
	$end_date = $_POST['end_date'];
	$reg_link = $_POST['reg_link'];
	$post_code1 = $_POST['post_code1'];
	$post_code2 = $_POST['post_code2'];
	$post_code = $_POST['post_code'];
	$max_cap = $_POST['max_cap'];
	$e_desc = $_POST['e_desc'];
	
	//make a query
	$query = "UPDATE event SET
	e_name = ?,
	start_date = ?,
	end_date = ?,
	reg_link = ?,
	post_code1 = ?,
	post_code2 = ?,
	post_code = ?,
	max_cap = ?,
	e_desc = ?
	
	WHERE event_id = ?";
	
	//prepare statement
	if ($stmt = $conn->prepare($query)) {
		$stmt->bind_param(
			"sssssssisi",
			$_POST['e_name'],
			$_POST['start_date'],
			$_POST['end_date'],
			$_POST['reg_link'],
			$_POST['post_code1'],
			$_POST['post_code2'],
			$_POST['post_code'],
			$_POST['max_cap'],
			$_POST['e_desc'],
			$_POST['event_id']
			);
		$stmt->execute();
		
		//update successful
		$response["success"] = 1;
		$response["message"] = "Event updated";
		
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
