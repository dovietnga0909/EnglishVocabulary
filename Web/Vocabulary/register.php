<?php
	require_once('Database.php');
	if(isset($_POST['username']) && isset($_POST['password']) && isset($_POST['fullname'])){
		$username = $_POST['username'];
		$password = $_POST['password'];
		$fullname = $_POST['fullname'];
		if($username!="" && $password != "" && $fullname != ""){
			$db = new Database();
			$check = $db->checkUser3($username);
			if($check){
				error("Tài khoản đã tồn tại.");
			}else{
				$reg = $db->register($username, $password, $fullname);
				if($reg)
					success();
				else
					error("Lỗi không xác thực.");
			}
		}else
			error("Dữ liệu gửi lên không chính xác.");
	}else
		error("Dữ liệu gửi lên không đầy đủ.");
	function success(){
    	$response = array();
		$response["error"] = 0;
		$response["success"] = 1;
		echo json_encode($response);
	}
	
	function error($msg){
		$response["error"] = 1;
		$response["success"] = 0;
		$response["error_msg"] = $msg;
		echo json_encode($response);
	}
?>