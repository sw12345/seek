<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//connect to database
include 'db_connect.php';

if(isset($_POST['email_address'])) { $email_address = $_POST['email_address']; }

//make a query
$query = "SELECT user_id, email_address FROM users WHERE email_address = ?";

if ($stmt = $conn->prepare($query)) {
    $stmt-> bind_param('s', $email_address);

    //send query to db
    $stmt-> execute();
    $stmt->store_result();
    $stmt->bind_result($user_id, $email_address);

    //fetch values by looping through each row
    while ($stmt->fetch()) {
        $jsonResponse = array(
            'userId' => $user_id,
            'email' => $email_address
        );
    }

   // json_encode($jsonResponse);
}

// success
//$jsonResponse["success"] = 1;
//print_r($jsonResponse);
echo json_encode($jsonResponse);

//close statement
$stmt->close();

//close connection
$conn->close();

?>
