<?php
	require_once('Database.php');
	if(isset($_POST['userId']) && isset($_POST['username'])&&isset($_POST['json'])){
		$userId = $_POST['userId'];
		$username = $_POST['username'];
		$json = $_POST['json'];
		$db = new Database();
		$check = $db->checkUser($userId, $username);
		if($check){
			$result = $db->update($json);
			if($result)
				success();
			else
				error("Lỗi, không xác định.");
		}else
			error("Tài khoản không tồn tại");
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