<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

include 'db_connect.php';

$response = array();

if (!empty($_POST)) {
	//check for no empty fields
	if (empty($_POST['email_address']) || empty($_POST['user_pass'])) {
		$response["success"] = 0;
		$response["message"] = "Both fields cannot be empty";

		die(json_encode($response));
	} else {
		if(isset($_POST["user_pass"]) && isset($_POST["email_address"])) {
			$user_pass=$_POST["user_pass"];
			$email_address=$_POST["email_address"];

			//get user's input based on the email address
			$query = "SELECT user_id, email_address, user_pass FROM users WHERE email_address = '$email_address' and user_pass = '$user_pass'";

			$stmt = $conn->prepare($query);

			//send query to db
			$stmt->execute();

			//fetch the row from the query
			$row = $stmt->fetch();

			//validate password
			if (!empty($row)) {
				$response["success"] = 1;
				$response["message"] = "Login successful!";

				echo json_encode($response);
			} else {
				$response["success"] = 0;
				$response["message"] = "Invalid email address or password";

				die(json_encode($response));
			}
		}
	}
}

?>
