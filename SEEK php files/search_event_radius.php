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
/*SELECT event_id, e_name, start_date, end_date, post_code1, post_code2, post_code, ( 6371 * ACOS( COS( RADIANS( 51.554381 ) ) * COS( RADIANS( latitude ) ) * COS( RADIANS( longitude ) - RADIANS( - 0.1205535 ) ) + SIN( RADIANS( 51.554381 ) ) * SIN( RADIANS( latitude ) ) ) ) AS distance
FROM event
HAVING distance <5
ORDER BY distance
LIMIT 0 , 20*/

//make a query
$query = "SELECT event_id, e_name, start_date, end_date, post_code1, post_code2, post_code, ( 6371 * acos( cos( radians(?) ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(?) ) + sin( radians(?) ) * sin( radians( latitude ) ) ) ) AS distance FROM event HAVING distance < ? ORDER BY distance LIMIT 0 , 20";

if ($stmt = $conn->prepare($query)) {
    $stmt-> bind_param('ssss', $lat, $long, $lat, $radius);

    //send query to db
    $stmt-> execute();
    $stmt->store_result();
    $stmt->bind_result($event_id, $e_name, $start_date, $end_date, $post_code1, $post_code2, $post_code, $distance);


    //fetch values by looping through each row
    while ($stmt->fetch()) {
        $jsonResponse []= array(
            'eventId' => $event_id,
            'name' => $e_name,
            'startdate' => $start_date,
            'enddate' => $end_date,
            'postcode1' => $post_code1,
            'postcode2' => $post_code2,
            'postcode' => $post_code,
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
