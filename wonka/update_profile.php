<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
require_once "connection.php";

// ------------------ VALIDATION ------------------
if (!isset($_POST['id'])) {
    echo json_encode(["status" => "error", "message" => "id missing"]);
    exit;
}

$id = intval($_POST['id']);
$username = $_POST['username'] ?? "";
$email    = $_POST['email'] ?? "";
$phone    = $_POST['phone'] ?? "";

// ------------------ PROFILE PIC PROCESSING ------------------
$uploaded_file_name = null;

if (isset($_FILES['profile_pic']) && $_FILES['profile_pic']['error'] === UPLOAD_ERR_OK) {

    $uploadDir = "uploads/";
    if (!file_exists($uploadDir)) mkdir($uploadDir, 0777, true);

    $originalName = $_FILES['profile_pic']['name'];
    $extension = strtolower(pathinfo($originalName, PATHINFO_EXTENSION));

    // Allowed extensions
    $allowed = ["jpg", "jpeg", "png", "webp"];

    if (!in_array($extension, $allowed)) {
        echo json_encode(["status" => "error", "message" => "Invalid image type"]);
        exit;
    }

    // Final filename: id + extension
    $uploaded_file_name = $id . "." . $extension;
    $targetPath = $uploadDir . $uploaded_file_name;

    // DELETE old image with any extension
    foreach ($allowed as $ext) {
        $oldFile = $uploadDir . $id . "." . $ext;
        if (file_exists($oldFile)) unlink($oldFile);
    }

    // Move the new file
    move_uploaded_file($_FILES['profile_pic']['tmp_name'], $targetPath);
}

// ------------------ SQL UPDATE ------------------

if ($uploaded_file_name === null) {
    // Update without changing picture
    $sql = "UPDATE users SET username=?, email=?, phone=? WHERE id=?";
    $stmt = $con->prepare($sql);
    $stmt->bind_param("sssi", $username, $email, $phone, $id);

} else {
    // Update with picture filename
    $sql = "UPDATE users SET username=?, email=?, phone=?, profile_pic=? WHERE id=?";
    $stmt = $con->prepare($sql);
    $stmt->bind_param("ssssi", $username, $email, $phone, $uploaded_file_name, $id);
}

if ($stmt->execute()) {

    // Return updated user info
    $sql = "SELECT id, username, email, phone, profile_pic 
            FROM users WHERE id=?";
    $get = $con->prepare($sql);
    $get->bind_param("i", $id);
    $get->execute();
    $user = $get->get_result()->fetch_assoc();

    echo json_encode([
        "status"  => "success",
        "message" => "Profile updated",
        "user"    => $user
    ]);

} else {
    echo json_encode(["status" => "error", "message" => "Update failed"]);
}

$stmt->close();
$con->close();
?>