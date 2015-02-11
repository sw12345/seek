<?php

//report any error
error_reporting(E_ALL); ini_set('display_errors', 1); mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//connect to database
include 'db_connect.php';

// from user's current location
if(isset($_POST['post_code'])) {
    $user_id = $_POST['user_id'];
    $post_code = $_POST['post_code']; // this could be postcodes or places

    // connect to google geocode api
    $url = "https://maps.googleapis.com/maps/api/geocode/json?address=$post_code+UK&key=AIzaSyCaweuQFb5W4JYsPurl5y71DYqLiD6XjaU";
    $json = file_get_contents($url);
    $geocode = json_decode($json, true);

    // get latitude & longitude of the post code / address
    $latitude = $geocode['results'][0]['geometry']['location']['lat'];
    $longitude = $geocode['results'][0]['geometry']['location']['lng'];

    // Make an age query for user
    $query_age = "SELECT under_18, TIMESTAMPDIFF (YEAR, birth_date, CURDATE()) AS user_age FROM users WHERE user_id = ?";

    // Make a query distance between two places for users above 18 years old
    $query_above18 = "SELECT FirstSet.user_id, FirstSet.first_name, FirstSet.last_name, FirstSet.int_tags, FirstSet.see_details, FirstSet.distance, SecondSet.age FROM
              (SELECT user_id, first_name, last_name, int_tags, see_details,(6371 * ACOS(COS(RADIANS(?)) * COS(RADIANS(latitude)) * COS(RADIANS(longitude) - RADIANS(?)) + SIN(RADIANS(?)) * SIN(RADIANS(latitude)))) AS distance FROM users) as FirstSet
              INNER JOIN (SELECT user_id, first_name, last_name, int_tags, see_details, TIMESTAMPDIFF( YEAR, birth_date, CURDATE()) AS age FROM users) AS SecondSet
              ON FirstSet.first_name = SecondSet.first_name
              AND SecondSet.age>18
              AND FirstSet.distance<100
              WHERE FirstSet.see_details = 'true' AND FirstSet.user_id NOT IN (?)
              ORDER BY distance";

    // Make a query distance between two places for users below 18 years old
    $query_under18 = "SELECT FirstSet.user_id, FirstSet.first_name, FirstSet.last_name, FirstSet.int_tags, FirstSet.see_details, FirstSet.distance, SecondSet.age FROM
              (SELECT user_id, first_name, last_name, int_tags, see_details,(6371 * ACOS(COS(RADIANS(?)) * COS(RADIANS(latitude)) * COS(RADIANS(longitude) - RADIANS(?)) + SIN(RADIANS(?)) * SIN(RADIANS(latitude)))) AS distance FROM users) as FirstSet
              INNER JOIN (SELECT user_id, first_name, last_name, int_tags, see_details, TIMESTAMPDIFF( YEAR, birth_date, CURDATE()) AS age FROM users) AS SecondSet
              ON FirstSet.first_name = SecondSet.first_name
              AND FirstSet.distance<100
              WHERE FirstSet.see_details = 'true' AND FirstSet.user_id NOT IN (?)
              ORDER BY distance";

    $stmt = $conn->prepare($query_age);
    $stmt->bind_param('i', $user_id);
    $stmt->execute();
    //$stmt->store_result();
    $stmt->bind_result($under_18, $user_age);
    $stmt->fetch();

    while ($stmt->fetch()) {
        $response [] = array(
            'under18'=> $under_18,
            'userage' => $user_age,
        );
    };
    if ($under_18 === 'false' && $user_age > 18) {
        $stmt2 = $conn->prepare($query_above18);
        $stmt2->bind_param('sssi', $latitude, $longitude, $latitude, $user_id);

        //send query to db
        $stmt2->execute();
        $stmt2->store_result();
        $stmt2->bind_result($user_id, $first_name, $last_name, $int_tags, $see_details, $distance, $age);

        //fetch values by looping through each row
        while ($stmt2->fetch()) {
            $jsonResponse [] = array(

                'userId' => $user_id,
                'firstname' => $first_name,
                'lastname' => $last_name,
                'interesttags' => $int_tags,
                'seedetails' => $see_details,
                'distance' => $distance,
                'age' => $age,

            );

            json_encode($jsonResponse);

        };
    } else if ($under_18 === 'true' && $user_age < 18) {
        $stmt3 = $conn->prepare($query_under18);
        $stmt3->bind_param('sssi', $latitude, $longitude, $latitude, $user_id);
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

            json_encode($jsonResponse);

        };
    }


    echo json_encode($jsonResponse);

    //close statement
    $stmt->close();



    //close connection
    $conn->close();

}

?>