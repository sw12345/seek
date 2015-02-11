<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//connect to database
include 'db_connect.php';

// from user's current location
if(isset($_POST['radius'])) {
    $user_id = $_POST['user_id'];
    $radius = $_POST['radius'];
    $latitude = $_POST['latitude'];
    $longitude = $_POST['longitude'];

    // Make a query distance between two places for users below 18 years old
    $query_under18 = "SELECT FirstSet.user_id, FirstSet.first_name, FirstSet.last_name, FirstSet.int_tags, FirstSet.see_details, FirstSet.distance, SecondSet.age FROM
              (SELECT user_id, first_name, last_name, int_tags, see_details,(6371 * ACOS(COS(RADIANS(?)) * COS(RADIANS(latitude)) * COS(RADIANS(longitude) - RADIANS(?)) + SIN(RADIANS(?)) * SIN(RADIANS(latitude)))) AS distance FROM users) as FirstSet
              INNER JOIN (SELECT user_id, first_name, last_name, int_tags, see_details, TIMESTAMPDIFF( YEAR, birth_date, CURDATE()) AS age FROM users) AS SecondSet
              ON FirstSet.first_name = SecondSet.first_name
              AND FirstSet.distance< ?
              WHERE FirstSet.see_details = 'true' AND FirstSet.user_id NOT IN (?)
              ORDER BY distance";

    $stmt3 = $conn->prepare($query_under18);
    $stmt3->bind_param('ssssi', $latitude, $longitude, $latitude, $radius, $user_id);

    //send query to db
    $stmt3->execute();
    $stmt3->store_result();
    $stmt3->bind_result($user_id, $first_name, $last_name, $int_tags, $see_details, $distance, $age);

    //fetch values by looping through each row
    while ($stmt3->fetch()) {
        $jsonResponse [] = array(

            'userId' => $user_id,
            'firstname' => $first_name,
            'lastname' => $last_name,
            'interesttags' => $int_tags,
            'seedetails' => $see_details,
            'distance' => $distance,
            'age' => $age,

        );

    };


    echo json_encode($jsonResponse);

//close statement
    $stmt3->close();

//close connection
    $conn->close();

}

?>