<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//connect to database
include 'db_connect.php';

//check value is set or not
if(isset($_POST['user_id'])) { $user_id = $_POST['user_id']; }

//make a query
$query = "SELECT user_id, first_name, last_name, phone_nmbr, address, post_code, birth_date, under_18, gender, int_tags, use_currloc, see_currloc, see_details FROM users WHERE user_id = ?";

if ($stmt = $conn->prepare($query))
{
    $stmt->bind_param('i', $user_id);

    //send query to db
    $stmt->execute();
    $stmt->store_result();
    $stmt->bind_result(
        $user_id,
        $first_name,
        $last_name,
        $phone_nmbr,
        $address,
        $post_code,
        $birth_date,
        $under_18,
        $gender,
        $int_tags,
        $use_currloc,
        $see_currloc,
        $see_details
    );

    //fetch values by looping through each row
    while ($stmt->fetch())
    {
        $jsonResponse = array(
            'userId' => $user_id,
            'firstname' => $first_name,
            'lastname' => $last_name,
            'phone' => $phone_nmbr,
            'address' => $address,
            'postcode' => $post_code,
            'birthdate' => $birth_date,
            'under18' => $under_18,
            'gender' => $gender,
            'interests' => $int_tags,
            'currloc' => $use_currloc,
            'contdetails' => $see_details
        );
        json_encode($jsonResponse);
    };

    echo json_encode($jsonResponse);

    //close statement
    $stmt->close();
}

//close connection
$conn->close();

?>
