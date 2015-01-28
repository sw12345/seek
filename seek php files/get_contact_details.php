<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//connect to database
include 'db_connect.php';

if(isset($_POST['user_id'])) { $user_id = $_POST['user_id']; }

//make a query
$query = "SELECT first_name, last_name, email_address, gender, int_tags FROM users WHERE user_id = ?";

if ($stmt = $conn->prepare($query)) {
    $stmt-> bind_param('i', $user_id);

    //send query to db
    $stmt-> execute();
    $stmt->store_result();
    $stmt->bind_result($first_name, $last_name, $email_address, $gender, $int_tags);

    //fetch values by looping through each row
    while ($stmt->fetch()) {
        $jsonResponse = array(
            'userId' => $user_id,
            'firstname' => $first_name,
            'lastname' => $last_name,
            'email' => $email_address,
            'gender' => $gender,
            'interests' => $int_tags
        );
        json_encode($jsonResponse);
    };

    // success
    //$response["success"] = 1;
    echo json_encode($jsonResponse);

    //close statement
    $stmt->close();

}

//close connection
$conn->close();

?>
