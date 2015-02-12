<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//array for JSON response
$response = array();

//connect to db
include 'db_connect.php';

//check the value is set or not
if(isset($_POST['event_id'])) { 
	$event_id = $_POST['event_id']; 
	//make a query
	$query = "DELETE FROM event WHERE event_id = ?";
	
	//prepare statement
	if ($stmt = $conn->prepare($query)) {
		$stmt-> bind_param('i', $event_id);
		$stmt-> execute();
		
		//delete successful
		$response["success"] = 1;
		$response["message"] = "Event deleted successfully";
		
		echo json_encode($response);
		
	} else {
		//no event found
		$response["success"] = 0;
		$response["message"] = "No event found";
		
		echo json_encode($response);
		
		//close statement
		$stmt-> close();
	}
	
	//close connection
	$conn-> close();
}
?>
