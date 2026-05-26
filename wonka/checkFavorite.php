<?php
require_once "connection.php";
header('Content-Type: application/json');

if (!isset($_GET['user_id']) || !isset($_GET['product_id'])) {
    echo json_encode(["error" => "Missing parameters"]);
    exit;
}

$user_id = $_GET['user_id'];
$product_id = $_GET['product_id'];

$stmt = $con->prepare("SELECT COUNT(*) AS c FROM favorites WHERE user_id = ? AND product_id = ?");
$stmt->bind_param("ii", $user_id, $product_id);
$stmt->execute();
$result = $stmt->get_result();
$row = $result->fetch_assoc();

echo json_encode([
    "isFavorite" => ($row['c'] > 0)
]);

$stmt->close();
$con->close();
?>
