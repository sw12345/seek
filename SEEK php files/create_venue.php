<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//if new user input
if($_POST) {
	$address = $_POST['address'];
	// connect to google geocode api
	$trimaddress = str_replace(" ","+", trim($address));
	$url = "https://maps.googleapis.com/maps/api/geocode/json?address=$trimaddress+UK&key=AIzaSyCaweuQFb5W4JYsPurl5y71DYqLiD6XjaU";
	$json = file_get_contents($url);
	$geocode = json_decode($json, true);

	// get latitude & longitude of the post code / address
	$latitude = $geocode['results'][0]['geometry']['location']['lat'];
	$longitude = $geocode['results'][0]['geometry']['location']['lng'];

	//connect to database
	include 'db_connect.php';
	
	//array for JSON response
	$response = array();
	
	//sql query
	/* For testing purpose, the user_id is set to 1 because it's the foreign key that connects the tables. */
	$query = "INSERT INTO venue 
	(user_id, v_name, v_type, address, post_code, max_cap, v_desc, latitude, longitude)
	VALUES(6, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	//if the statement was prepared
	if($statement = $conn->prepare($query)){
		$statement->bind_param(
			"ssssisss",
			$_POST['v_name'],
			$_POST['v_type'],
			$_POST['address'],
			$_POST['post_code'],
			$_POST['max_cap'],
			$_POST['v_desc'],
			$latitude,
			$longitude
			);
		
		//execute the insert query
		if($statement->execute()) {
			// successfully inserted into database
			$response["success"] = 1;
			$response["message"] = "Venue created successfully.";
			
			// echoing JSON response
			echo json_encode($response);
			
		} else {
			// failed to insert row
			$response["success"] = 0;
			$response["message"] = "Failed to create venue.";
			
			// echoing JSON response
			echo json_encode($response);
		}
		
	}else {
		die("Unable to prepare statement.");
	}
	$statement->close();
	//close the db
	$conn->close(); 
	
	
}	
?>
