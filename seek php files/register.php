<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

include 'db_connect.php';

//array for JSON response
$response = array();

if (!empty($_POST)) {
	// Check for no empty fields
	if (empty($_POST['first_name']) || empty($_POST['last_name']) || empty($_POST['email_address']) || empty($_POST['email_address_cnfrm']) || empty($_POST['user_pass']) || empty($_POST['user_pass_cnfrm']) || empty($_POST['terms_cond'])) {
		$response["success"] = 0;
		$response["message"] = "Fields cannot be empty";

		die(json_encode($response));
	}

	// Check for existing email address
	if (isset($_POST['email_address'])) {
		$email_address = $_POST['email_address'];

		$query_exists = "SELECT email_address FROM users WHERE email_address = ?";

		$stmt = $conn->prepare($query_exists);
		$stmt->bind_param('s', $email_address);
		$stmt->execute();
		$stmt->bind_result($email_address);
		$stmt->store_result();
		$stmt->fetch();
		$exists = $stmt->num_rows;
		//echo json_encode($stmt->num_rows);

		// If email address already existed
		if ($exists != 0) {
			$response["success"] = 0;
			$response["message"] = "Email address already exists!";
			die(json_encode($response));
		}
	}

	// Confirmed email address
	if ($_POST['email_address'] != $_POST['email_address_cnfrm']) {
		$response["success"] = 0;
		$response["message"] = "Email addresses don't match";
		die(json_encode($response));
	}

	// Check that user tick the terms & conditions checkbox
	if ($_POST['terms_cond'] == "false") {
		$response["success"] = 0;
		$response["message"] = "You must agree to the term & policy";
		die(json_encode($response));
	}

	// Confirmed password then register user
	if ($_POST['user_pass'] != $_POST['user_pass_cnfrm']) {
		$response["success"] = 0;
		$response["message"] = "Passwords don't match";
		die(json_encode($response));
	}
	//query for insert to database
	$query_insert = "INSERT INTO users (first_name, last_name, email_address, user_pass, terms_cond) VALUES (?, ?, ?, ?, ?)";

	$statement = $conn->prepare($query_insert);
	$statement->bind_param(
		"sssss",
		$_POST['first_name'],
		$_POST['last_name'],
		$_POST['email_address'],
		$_POST['user_pass'],
		$_POST['terms_cond']
	);

	//execute the insert query
	if ($statement->execute()) {
		// successfully inserted into database
		$response["success"] = 1;
		$response["message"] = "User registered successfully.";

		// echoing JSON response
		echo json_encode($response);

	} else {
		// failed to insert row
		$response["success"] = 0;
		$response["message"] = "Failed to register user.";

		// echoing JSON response
		echo json_encode($response);
	}
	$stmt->close();
}

$conn->close();
?>