<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
require_once 'connection.php';

if (!isset($_POST['id'])) {
    echo json_encode(["status" => "error", "message" => "id required"]);
    exit();
}

$id = intval($_POST['id']);

$sql = "SELECT id, username, email, phone, profile_pic 
        FROM users 
        WHERE id = ?";

$stmt = $con->prepare($sql);
$stmt->bind_param("i", $id);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {

    $user = $result->fetch_assoc();

    // Build full URL to profile picture
    if (!$user['profile_pic']) {
        $user['profile_pic_url'] = null; // no image uploaded
    } else {
        $user['profile_pic_url'] = "http://10.0.2.2/wonka/uploads/" . $user['profile_pic'];
    }

    echo json_encode([
        "status" => "success",
        "user" => $user
    ]);

} else {
    echo json_encode([
        "status" => "error",
        "message" => "User not found"
    ]);
}

$stmt->close();
$con->close();
?>