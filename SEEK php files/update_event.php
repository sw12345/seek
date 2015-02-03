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
	$post_code = $_POST['post_code'];
	$max_cap = $_POST['max_cap'];
	$e_desc = $_POST['e_desc'];
	$address = $_POST['address'];

	// connect to google geocode api
	$trimaddress = str_replace(" ","+", trim($address));
	$trimpostcode = str_replace(" ","+", trim($postcode));
	$url = "https://maps.googleapis.com/maps/api/geocode/json?address=$trimaddress+$trimpostcode+UK&key=AIzaSyCaweuQFb5W4JYsPurl5y71DYqLiD6XjaU";
	$json = file_get_contents($url);
	$geocode = json_decode($json, true);

	// get latitude & longitude of the post code / address
	$latitude = $geocode['results'][0]['geometry']['location']['lat'];
	$longitude = $geocode['results'][0]['geometry']['location']['lng'];

	//make a query
	$query = "UPDATE event SET
	e_name = ?,
	start_date = ?,
	end_date = ?,
	reg_link = ?,
	post_code = ?,
	max_cap = ?,
	e_desc = ?,
	address = ?,
	latitude = ?,
	longitude =?
	
	WHERE event_id = ?";

	//prepare statement
	if ($stmt = $conn->prepare($query)) {
		$stmt->bind_param(
			"sssssissssi",
			$_POST['e_name'],
			$_POST['start_date'],
			$_POST['end_date'],
			$_POST['reg_link'],
			$_POST['post_code'],
			$_POST['max_cap'],
			$_POST['e_desc'],
			$_POST['address'],
			$latitude,
			$longitude,
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
