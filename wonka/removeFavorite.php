<?php
require_once 'connection.php';

if (!isset($_GET['user_id']) || !isset($_GET['product_id'])) {
    echo "-1";
    exit;
}

$user_id = intval($_GET['user_id']);
$product_id = intval($_GET['product_id']);

$stmt = $con->prepare("DELETE FROM favorites WHERE user_id = ? AND product_id = ?");
if (!$stmt) {
    echo "-1";
    exit;
}

$stmt->bind_param("ii", $user_id, $product_id);

if ($stmt->execute()) {
    echo "1";
} 
else {
    echo "-1";
}


$stmt->close();
$con->close();
?>
