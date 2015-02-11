<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//establish connection with db
include 'db_connect.php';

if (isset($_POST['venue_id'])) {
	$venue_id = $_POST['venue_id'];
	$v_name = $_POST['v_name'];
	$v_type = $_POST['v_type'];
	$address = $_POST['address'];
	$post_code = $_POST['post_code'];
	$max_cap = $_POST['max_cap'];
	$v_desc = $_POST['v_desc'];

	// connect to google geocode api
	$trimaddress = str_replace(" ","+", trim($address));
	$trimpostcode = str_replace(" ","+", trim($post_code));
	$url = "https://maps.googleapis.com/maps/api/geocode/json?address=$trimaddress+$trimpostcode+UK&key=AIzaSyCaweuQFb5W4JYsPurl5y71DYqLiD6XjaU";
	$json = file_get_contents($url);
	$geocode = json_decode($json, true);

	// get latitude & longitude of the post code / address
	$latitude = $geocode['results'][0]['geometry']['location']['lat'];
	$longitude = $geocode['results'][0]['geometry']['location']['lng'];

	//make a query
	$query = "UPDATE venue SET
				v_name = ?,
				v_type = ?,
				address = ?,
				post_code = ?,
				max_cap = ?,
				v_desc = ?,
				latitude = ?,
				longitude =?
				WHERE venue_id = ?";

	//prepare statement
	if ($stmt = $conn->prepare($query)) {
		$stmt->bind_param(
			"ssssisssi",

			$_POST['v_name'],
			$_POST['v_type'],
			$_POST['address'],
			$_POST['post_code'],
			$_POST['max_cap'],
			$_POST['v_desc'],
			$latitude,
			$longitude,
			$_POST['venue_id']

		/*$latitude,
        $longitude*/
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
