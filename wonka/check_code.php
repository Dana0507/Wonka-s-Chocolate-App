<?php
header("Content-Type: application/json");
require_once "connection.php";

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(["status" => "error", "message" => "Use POST method"]);
    exit();
}

// Use $_POST instead of JSON
if (!isset($_POST['code'])) {
    echo json_encode(["status" => "error", "message" => "Code is required"]);
    exit();
}

$code = trim($_POST['code']);

$stmt = $con->prepare("SELECT code FROM tickets WHERE code = ?");
$stmt->bind_param("s", $code);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    echo json_encode(["status" => "success", "message" => "Code is valid"]);
} else {
    echo json_encode(["status" => "error", "message" => "Invalid code"]);
}

$stmt->close();
$con->close();
?>