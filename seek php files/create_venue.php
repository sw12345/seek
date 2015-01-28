<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//if new user input
if($_POST) {
	
	//connect to database
	include 'db_connect.php';
	
	//array for JSON response
	$response = array();
	
	//sql query
	/* For testing purpose, the user_id is set to 1 because it's the foreign key that connects the tables. */
	$query = "INSERT INTO venue 
	(v_name, v_type, post_code1, post_code2, post_code, latitude, longitude, region, max_cap, v_desc)
	VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	//if the statement was prepared
	if($statement = $conn->prepare($query)){
		$statement->bind_param(
			"ssssssssis",
			$_POST['v_name'],
			$_POST['v_type'],
			$_POST['post_code1'],
			$_POST['post_code2'],
			$_POST['post_code'],
			$_POST['latitude'],
			$_POST['longitude'],
			$_POST['region'],
			$_POST['max_cap'],
			$_POST['v_desc']
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
