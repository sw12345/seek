<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//connect to database
include 'db_connect.php';

// from user's current location
if(isset($_POST['post_code'])) {
    $post_code = $_POST['post_code'];

    // connect to google geocode api
    $url = "https://maps.googleapis.com/maps/api/geocode/json?address=$post_code+UK&key=AIzaSyCaweuQFb5W4JYsPurl5y71DYqLiD6XjaU";
    $json = file_get_contents($url);
    $geocode = json_decode($json, true);

    // get latitude & longitude of the post code / address
    $latitude = $geocode['results'][0]['geometry']['location']['lat'];
    $longitude = $geocode['results'][0]['geometry']['location']['lng'];

//example sql query
    /*SELECT user_id, first_name, last_name, int_tags, post_code1, post_code2, post_code, ( 6371 * ACOS( COS( RADIANS( 51.554381 ) ) * COS( RADIANS( latitude ) ) * COS( RADIANS( longitude ) - RADIANS( - 0.1205535 ) ) + SIN( RADIANS( 51.554381 ) ) * SIN( RADIANS( latitude ) ) ) ) AS distance
    FROM users
    HAVING distance <5
    ORDER BY distance
    LIMIT 0 , 20*/

    // Make a query distance between two places
    $query = "SELECT user_id, first_name, last_name, int_tags, ( 6371 * ACOS( COS( RADIANS( ? ) ) * COS( RADIANS( latitude ) ) * COS( RADIANS( longitude ) - RADIANS( ? ) ) + SIN( RADIANS( ? ) ) * SIN( RADIANS( latitude ) ) ) ) AS distance FROM users WHERE TIMESTAMPDIFF( YEAR, birth_date, CURDATE( ) ) >18 ORDER BY distance LIMIT 0 , 20";

    if ($stmt = $conn->prepare($query)) {
        $stmt->bind_param('sss', $latitude, $longitude, $latitude);

        //send query to db
        $stmt->execute();
        $stmt->store_result();
        $stmt->bind_result($user_id, $first_name, $last_name, $int_tags, $distance);

        //fetch values by looping through each row
        while ($stmt->fetch()) {
            $jsonResponse [] = array(
                'userId' => $user_id,
                'firstname' => $first_name,
                'lastname' => $last_name,
                'interesttags' => $int_tags,
                'distance' => $distance,
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
}
?>