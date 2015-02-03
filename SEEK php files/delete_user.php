<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//array for JSON response
$response = array();

//connect to db
include 'db_connect.php';

//get user id from URL
if(isset($_POST['user_id'])) {
    $user_id = $_POST['user_id'];
    /**
     * The current database is a MyISAM database type due to it being a free hosting. Thus, deleting user won't delete
     * event or venue created by this user. UnLtd should ensure that the database is of InnoDB to enable foreign key
     * constraint that would allow cascading upon deletion or update (delete event or venue created by the user
     * deleted).
     */
    
    //make a query
    $query = "DELETE FROM users WHERE user_id = ?";

    //prepare statement
    if ($stmt = $conn->prepare($query)) {
        $stmt-> bind_param('i', $user_id);
        $stmt-> execute();

        //delete successful
        $response["success"] = 1;
        $response["message"] = "User deleted successfully";

        echo json_encode($response);

    } else {
        //no user found
        $response["success"] = 0;
        $response["message"] = "No user found";

        echo json_encode($response);

        //close statement
        $stmt-> close();
    }

    //close connection
    $conn-> close();
}
?>
