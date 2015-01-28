<?php

//report any error
error_reporting(E_ALL);
ini_set('display_errors', 1);
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

//establish connection with db
include 'db_connect.php';

if (isset($_POST['user_id']))
{
    $user_id = $_POST['user_id'];
    $first_name = $_POST['first_name'];
    $last_name = $_POST['last_name'];
    $phone_nmbr = $_POST['phone_nmbr'];
    $post_code1 = $_POST['post_code1'];
    $post_code2 = $_POST['post_code2'];
    $post_code = $_POST['post_code'];
    $birth_date = $_POST['birth_date'];
    $under_18 = $_POST['under_18'];
    $gender = $_POST['gender'];
    $int_tags = $_POST['int_tags'];
    $use_currloc = $_POST['use_currloc'];
    $see_currloc = $_POST['see_currloc'];
    $see_details = $_POST['see_details'];

    //make a query
    $query = "UPDATE users
    SET
    first_name = ?,
    last_name = ?,
	phone_nmbr = ?,
    post_code1 = ?,
	post_code2 = ?,
	post_code = ?,
	birth_date = ?,
	under_18 = ?,
	gender = ?,
    int_tags = ?,
    use_currloc = ?,
    see_currloc = ?,
    see_details = ?,
	
	WHERE user_id = ?";

    //prepare statement
    if ($stmt = $conn->prepare($query))
    {
        $stmt->bind_param(
            "issssssssssss",
            $_POST['user_id'],
            $_POST['first_name'],
            $_POST['last_name'],
            $_POST['phone_nmbr'],
            $_POST['post_code1'],
            $_POST['post_code2'],
            $_POST['post_code'],
            $_POST['under_18'],
            $_POST['gender'],
            $_POST['int_tags'],
            $_POST['use_currloc'],
            $_POST['see_currloc'],
            $_POST['see_details']);

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