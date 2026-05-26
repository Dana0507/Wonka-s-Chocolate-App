<?php
require_once 'connection.php';

if (!isset($_GET['user_id']) || !isset($_GET['product_id']) || !isset($_GET['quantity'])) {
    echo "-1";
    exit;
}

$user_id = intval($_GET['user_id']);
$product_id = intval($_GET['product_id']);
$quantity = intval($_GET['quantity']);

$check = $con->prepare("SELECT quantity FROM carts WHERE user_id = ? AND product_id = ?");
$check->bind_param("ii", $user_id, $product_id);
$check->execute();
$result = $check->get_result();

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $newQuantity = $row['quantity'] + $quantity;

    $update = $con->prepare("UPDATE carts SET quantity = ? WHERE user_id = ? AND product_id = ?");
    $update->bind_param("iii", $newQuantity, $user_id, $product_id);

    if ($update->execute()) {
        echo "1";
    } 
    else {
        echo "-1";
    }
    $update->close();

} else {

    $insert = $con->prepare("INSERT INTO carts (user_id, product_id, quantity) VALUES (?, ?, ?)");
    $insert->bind_param("iii", $user_id, $product_id, $quantity);

    if ($insert->execute()) {
        echo "1";
    } 
    else {
        echo "-1";
    }
    $insert->close();
}

$check->close();
$con->close();
?>
