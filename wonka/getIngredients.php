<?php
require_once 'connection.php'; 
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");

if (!isset($_GET['id'])) {
    echo json_encode([]);
    exit;
}

$id = intval($_GET['id']);

$stmt = $con->prepare("SELECT ingredients FROM products WHERE id = ?");
$stmt->bind_param("i", $id);
$stmt->execute();
$stmt->bind_result($ingredients_json);
$stmt->fetch();
$stmt->close();

if ($ingredients_json) {
    echo stripslashes($ingredients_json);
} 
else {
    echo json_encode([]);
}
?>
