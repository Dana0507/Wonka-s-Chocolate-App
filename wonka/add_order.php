<?php
include "connection.php";

$user_id = $_POST["user_id"];
$shipping_address = $_POST["shipping_address"];
$payment_method = $_POST["payment_method"];
$total_amount = $_POST["total_amount"];
$order_type = $_POST["order_type"];
$products = $_POST["products"];

$sql = "INSERT INTO orders (user_id, shipping_address, payment_method, total_amount)
        VALUES ('$user_id', '$shipping_address', '$payment_method', '$total_amount')";

if (mysqli_query($con, $sql)) {

    $order_id = mysqli_insert_id($con);

    $items = explode(",", $products);

    foreach ($items as $item) {
        if (empty($item)) continue;

        list($product_id, $qty) = explode(":", $item);

        $p = mysqli_query($con, "SELECT price FROM products WHERE id='$product_id'");
        $row = mysqli_fetch_assoc($p);
        $price = $row["price"];

        mysqli_query($con, 
            "INSERT INTO order_items (order_id, product_id, quantity, price)
             VALUES ('$order_id', '$product_id', '$qty', '$price')"
        );
    }

    echo "success";
} else {
    echo "error: " . mysqli_error($con);
}

?>
