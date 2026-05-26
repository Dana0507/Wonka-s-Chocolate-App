<?php
header("Content-Type: application/json");

// ---- CONFIG ----
$servername = "localhost";
$username   = "root";
$password   = ""; 
$dbname     = "wonka";

// ---- CHECK USER ID ----
if (!isset($_GET['user_id'])) {
    echo json_encode(["error" => "user_id is required"]);
    exit;
}

$user_id = intval($_GET['user_id']);

try {
    // ---- CONNECT ----
    $conn = new mysqli($servername, $username, $password, $dbname);
    $conn->set_charset("utf8mb4");

    if ($conn->connect_error) {
        echo json_encode(["error" => "Connection failed"]);
        exit;
    }

    // ---- QUERY FAVORITES ----
    $sql = "
        SELECT p.id, p.name, p.price, p.img, p.img_path
        FROM favorites f
        JOIN products p ON f.product_id = p.id
        WHERE f.user_id = ?
        ORDER BY p.name ASC
    ";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $user_id);
    $stmt->execute();

    $result = $stmt->get_result();
    $favorites = [];

    while ($row = $result->fetch_assoc()) {
        $favorites[] = [
            "id"    => $row["id"],
            "name"  => $row["name"],
            "price" => $row["price"],
            "image" => $row["img"],
            "image_path" => $row["img_path"]
        ];
    }

    echo json_encode([
        "success" => true,
        "count"   => count($favorites),
        "data"    => $favorites
    ]);

    $stmt->close();
    $conn->close();

} catch (Exception $e) {
    echo json_encode(["error" => "Unexpected error occurred"]);
}
?>
