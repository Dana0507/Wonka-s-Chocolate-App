<?php
header("Content-Type: application/json");
require_once "connection.php";

if (!isset($_GET['user_id'])) {
    echo json_encode([]);
    exit;
}

$user_id = intval($_GET['user_id']);

$stmt = $con->prepare("
    SELECT p.id, p.name, p.price, p.img_path, c.quantity
    FROM carts c
    JOIN products p ON p.id = c.product_id
    WHERE c.user_id = ?
");

$stmt->bind_param("i", $user_id);
$stmt->execute();

$result = $stmt->get_result();

$items = array();
while ($row = $result->fetch_assoc()) {
    $items[] = $row;
}

echo json_encode($items);

$stmt->close();
$con->close();
?>
