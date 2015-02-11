<?php

//report any error
error_reporting(E_ALL);
ini_set('display_errors', 1);
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//establish connection with db
include 'db_connect.php';

if(!empty($_POST)) {
    // Check for no empty fields
    if (empty($_POST['first_name']) || empty($_POST['last_name']) || empty($_POST['phone_nmbr']) || empty($_POST['address']) || empty($_POST['post_code']) || empty($_POST['birth_date'])) {
        $response["success"] = 0;
        $response["message"] = "Fields cannot be empty";

        die(json_encode($response));
    }

    $user_id = $_POST['user_id'];
    $first_name = $_POST['first_name'];
    $last_name = $_POST['last_name'];
    $phone_nmbr = $_POST['phone_nmbr'];
    $address = $_POST['address'];
    $post_code = $_POST['post_code'];
    $birth_date = $_POST['birth_date'];
    $under_18 = $_POST['under_18'];
    $gender = $_POST['gender'];
    $int_tags = $_POST['int_tags'];
    $use_currloc = $_POST['use_currloc'];
    $see_details = $_POST['see_details'];

    // connect to google geocode api
    $trimaddress = str_replace(" ","+", trim($address));
    $trimpostcode = str_replace(" ","+", trim($post_code));
    $url = "https://maps.googleapis.com/maps/api/geocode/json?address=$trimaddress+$trimpostcode+UK&key=AIzaSyCaweuQFb5W4JYsPurl5y71DYqLiD6XjaU";
    $json = file_get_contents($url);
    $geocode = json_decode($json, true);

    // get latitude & longitude of the post code / address
    $latitude = $geocode['results'][0]['geometry']['location']['lat'];
    $longitude = $geocode['results'][0]['geometry']['location']['lng'];

    //make a query
    $query = "UPDATE users
    SET
    first_name = ?,
    last_name = ?,
	phone_nmbr = ?,
    address = ?,
	post_code = ?,
	birth_date = ?,
	under_18 = ?,
	gender = ?,
    int_tags = ?,
    use_currloc = ?,
    see_details = ?,
    latitude = ?,
    longitude = ?
	WHERE user_id = ?";

    //prepare statement
    if ($stmt = $conn->prepare($query))
    {
        $stmt->bind_param(
            "sssssssssssssi",
            $_POST['first_name'],
            $_POST['last_name'],
            $_POST['phone_nmbr'],
            $_POST['address'],
            $_POST['post_code'],
            $_POST['birth_date'],
            $_POST['under_18'],
            $_POST['gender'],
            $_POST['int_tags'],
            $_POST['use_currloc'],
            $_POST['see_details'],
            $latitude,
            $longitude,
            $_POST['user_id']
        );

        $stmt->execute();

        //update successful
        $response["success"] = 1;
        $response["message"] = "Profile updated";

        echo json_encode($response);

    } else
    {
        //Unable to update
        $response["success"] = 0;
        $response["message"] = "Unable to update profile";

        echo json_encode($response);

        //close statement
        $stmt->close();
    }

    //close connection
    $conn->close();
}

?>