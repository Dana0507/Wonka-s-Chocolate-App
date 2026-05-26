<?php<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Debug log: check if file runs
file_put_contents("debug.txt", "Request method: " . $_SERVER['REQUEST_METHOD'] . "\n", FILE_APPEND);
require_once 'connection.php';
header('Content-Type: application/json');

// Only accept POST
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(["status" => "error", "message" => "Use POST"]);
    exit();
}

// Check required fields
if (!isset($_POST['name'], $_POST['price'], $_POST['ingredients'])) {
    echo json_encode(["status" => "error", "message" => "Missing fields"]);
    exit();
}

// Handle image upload
$imgPath = null;
$imgName = null;
if (isset($_FILES['image'])) {
    $uploadDir = __DIR__ . "/uploads/";
    if (!is_dir($uploadDir)) mkdir($uploadDir, 0777, true);

    $imgName = time() . "_" . basename($_FILES['image']['name']);
    $imgPath = "uploads/" . $imgName;

    if (!move_uploaded_file($_FILES['image']['tmp_name'], $imgPath)) {
        echo json_encode(["status" => "error", "message" => "Failed to upload image"]);
        exit();
    }
}

// Sanitize input
$name = trim($_POST['name']);
$price = floatval($_POST['price']);
$ingredients = trim($_POST['ingredients']); // can store as JSON string

// Check if chocolate with same name exists
$check = $con->prepare("SELECT id FROM products WHERE name = ?");
$check->bind_param("s", $name);
$check->execute();
$result = $check->get_result();

if ($result->num_rows > 0) {
    echo json_encode(["status" => "error", "message" => "Chocolate with this name already exists"]);
    exit();
}

// Insert into products table
$stmt = $con->prepare("INSERT INTO products(name, price, img, img_path, ingredients) VALUES(?, ?, ?, ?, ?)");
$stmt->bind_param("sdsss", $name, $price, $imgName, $imgPath, $ingredients);

if ($stmt->execute()) {
    $product_id = $stmt->insert_id;
    echo json_encode([
        "status" => "success",
        "message" => "Chocolate added successfully",
        "product_id" => $product_id
    ]);
} else {
    echo json_encode([
        "status" => "error",
        "message" => "Failed to add chocolate"
    ]);
}

$stmt->close();
$con->close();
?>