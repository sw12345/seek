<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//if new user input
if($_POST) {
    $user_id = $_POST['user_id'];
    $latitude = $_POST['latitude'];
    $longitude = $_POST['longitude'];

    //connect to database
    include 'db_connect.php';

    //array for JSON response
    $response = array();

    //sql query
    /* For testing purpose, the user_id is set to 1 because it's the foreign key that connects the tables. */
    $query = "UPDATE users SET curr_latitude = ?, curr_longitude = ? WHERE user_id = ?";

    //if the statement was prepared
    if($statement = $conn->prepare($query)){
        $statement->bind_param(
            "ssi",
            $_POST['latitude'],
            $_POST['longitude'],
            $_POST['user_id']
        );

        //execute the insert query
        if($statement->execute()) {
            // successfully inserted into database
            $response["success"] = 1;
            $response["message"] = "Event created successfully.";

            // echoing JSON response
            echo json_encode($response);

        } else {
            // failed to insert row
            $response["success"] = 0;
            $response["message"] = "Failed to create event.";

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
