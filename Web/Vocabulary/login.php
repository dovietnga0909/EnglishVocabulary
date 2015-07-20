<?php
	require_once('Database.php');
	if(isset($_POST['username']) && isset($_POST['password'])){
		$username = $_POST['username'];
		$password = $_POST['password'];
		$db = new Database();
		$check = $db->checkUser2($username, $password);
		if($check){
			$response = array();
			$response["error"] = 0;
			$response["success"] = 1;
			$response["user_id"] = $check['user_id'];
			$response["fullname"] = $check['fullname'];
			echo json_encode($response);
		}else
			error("Sai tài khoản hoặc mật khẩu.");
		
	}else
		error("Dữ liệu gửi lên không đầy đủ.");
	
	function error($msg){
		$response["error"] = 1;
		$response["success"] = 0;
		$response["error_msg"] = $msg;
		echo json_encode($response);
	}
?>