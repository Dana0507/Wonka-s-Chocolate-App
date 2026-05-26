<?php

$host = "localhost";
$user = "root";
$pass = "";
$db   = "wonka";

$conn = new mysqli($host, $user, $pass, $db);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$user_id = isset($_POST['user_id']) ? intval($_POST['user_id']) : 0;

if ($user_id > 0) {
    $stmt = $conn->prepare("DELETE FROM carts WHERE user_id = ?");
    $stmt->bind_param("i", $user_id);

    if ($stmt->execute()) {
        echo "success";
    } else {
        echo "error";
    }

    $stmt->close();
} else {
    echo "invalid_user";
}

$conn->close();
?>
