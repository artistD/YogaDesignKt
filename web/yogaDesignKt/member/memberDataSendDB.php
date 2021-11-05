<?php

    header('Content-Type:text/plain; charset=utf-8');

    $id = $_POST['id'];
    $pw = $_POST['pw'];

    $conn = mysqli_connect('localhost', 'willd88', 'messid88!!', 'willd88');
    mysqli_query($conn, 'set names utf8');

    $date = Date('Y-m-d H:i:s');

    $sql = "INSERT INTO yogaDesignKtMember(id, password, date) VALUE('$id', '$pw', '$date')";
    $result = mysqli_query($conn, $sql);

    if($result){
        echo "성공";
    }else{
        echo "실패";
    }

    mysqli_close($conn);



?>