<?php

header("Content-Type: application/json");

$host = "localhost";
$user = "root";
$pass = "";
$db   = "wonka";

$conn = new mysqli($host, $user, $pass, $db);

if ($conn->connect_error) {
    die(json_encode(["status" => "error", "message" => "Database connection failed"]));
}

$sql = "SELECT id, name, price, img, img_path, ingredients FROM products";
$result = $conn->query($sql);

$products = array();

while ($row = $result->fetch_assoc()) {
    // Convert JSON ingredients string to array
    $row["ingredients"] = json_decode($row["ingredients"], true);
    $products[] = $row;
}

echo json_encode([
    "status" => "success",
    "products" => $products
]);

$conn->close();
?>
