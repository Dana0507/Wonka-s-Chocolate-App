<?php
require_once 'connection.php';
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Content-Type");

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(["status" => "error", "message" => "Use POST"]);
    exit();
}

$data = json_decode(file_get_contents("php://input"), true);

if (!isset($data['username'], $data['password'], $data['email'], $data['phone'])) {
    echo json_encode(["status" => "error", "message" => "Missing fields"]);
    exit();
}

$username = trim($data['username']);
$password = trim($data['password']);
$email = trim($data['email']);
$phone = trim($data['phone']);


$check = $con->prepare("SELECT id FROM users WHERE username = ?");
$check->bind_param("s", $username);
$check->execute();
$result = $check->get_result();

if ($result->num_rows > 0) {
    echo json_encode(["status" => "error", "message" => "Username already taken"]);
    exit();
}

$hashedPassword = password_hash($password, PASSWORD_DEFAULT);

$stmt = $con->prepare("INSERT INTO users(username, password, email, phone) VALUES(?, ?, ?, ?)");
$stmt->bind_param("ssss", $username, $hashedPassword, $email, $phone);

if ($stmt->execute()) {
    $user_id = $stmt->insert_id;

    echo json_encode([
        "status" => "success",
        "message" => "Registration successful!",
        "user_id" => $user_id
    ]);
} else {
    echo json_encode([
        "status" => "error",
        "message" => "Registration failed"
    ]);
}

$stmt->close();
$con->close();
?>
