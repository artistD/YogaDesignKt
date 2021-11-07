<?php

    header('Content-Type:text/plain; charset=utf-8');

    $crud = $_POST['crud'];

    if($crud == "INSERT"){
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

    }else if($crud == "UPDATE"){
        $id = $_POST['id'];
        $nickName = $_POST['nickName'];

         $file = $_FILES['img'];

         $srcName = $file['name'];
         $tmpName = $file['tmp_name'];
         $size = $file['size'];

         $dstName = "./uploaded". date('YmdHis'). $srcName;
         move_uploaded_file($tmpName, $dstName);


         $conn = mysqli_connect('localhost', 'willd88', 'messid88!!', 'willd88');
         mysqli_query($conn, 'set names utf8');
 
         $date = Date('Y-m-d H:i:s');
 
         $sql = "UPDATE yogaDesignKtMember SET nickName = '$nickName', profileUrl = '$dstName' WHERE id='$id'";
         $result = mysqli_query($conn, $sql);
 
         if($result){
             echo "성공";
         }else{
             echo "실패";
         }
 
         mysqli_close($conn);
    }

?>