<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//connect to database
include 'db_connect.php';

// from user's current location
if(isset($_POST['radius'])) {
    $radius = $_POST['radius'];
    $lat = $_POST['latitude'];
    $long = $_POST['longitude'];
//var_dump($radius, $lat, $long);
}

//example sql query
/*SELECT user_id, first_name, last_name, end_date, post_code1, post_code2, post_code, ( 6371 * ACOS( COS( RADIANS( 51.554381 ) ) * COS( RADIANS( latitude ) ) * COS( RADIANS( longitude ) - RADIANS( - 0.1205535 ) ) + SIN( RADIANS( 51.554381 ) ) * SIN( RADIANS( latitude ) ) ) ) AS distance
FROM venue
HAVING distance <5
ORDER BY distance
LIMIT 0 , 20*/

//make a query
$query = "SELECT user_id, first_name, last_name, int_tags, ( 6371 * ACOS( COS( RADIANS( ? ) ) * COS( RADIANS( latitude ) ) * COS( RADIANS( longitude ) - RADIANS( ? ) ) + SIN( RADIANS( ? ) ) * SIN( RADIANS( latitude ) ) ) ) AS distance FROM users HAVING distance < ? ORDER BY distance LIMIT 0 , 20";

if ($stmt = $conn->prepare($query)) {
    $stmt-> bind_param('ssss', $lat, $long, $lat, $radius);

    //send query to db
    $stmt-> execute();
    $stmt->store_result();
    $stmt->bind_result($user_id, $first_name, $last_name, $int_tags, $distance);


    //fetch values by looping through each row
    while ($stmt->fetch()) {
        $jsonResponse []= array(
            'userId' => $user_id,
            'firstname' => $first_name,
            'lastname' => $last_name,
            'interesttags' => $int_tags,
            'distance' => $distance,
        );
        // json_encode($jsonResponse);
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