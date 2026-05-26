<?php
require_once "connection.php";

if (!isset($_GET['user_id']) || !isset($_GET['product_id']) || !isset($_GET['quantity'])) {
    echo "-1";
    exit;
}

$user_id = intval($_GET['user_id']);
$product_id = intval($_GET['product_id']);
$qty = intval($_GET['quantity']);

$stmt = $con->prepare("UPDATE carts SET quantity = ? WHERE user_id = ? AND product_id = ?");
$stmt->bind_param("iii", $qty, $user_id, $product_id);

if ($stmt->execute()) {
    echo "1";
} 
else {
    echo "-1";
}

$stmt->close();
$con->close();
?>
