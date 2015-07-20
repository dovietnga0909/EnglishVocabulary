<?php
	require_once('Database.php');
	if(isset($_POST['userId']) && isset($_POST['username'])){
		$userId = $_POST['userId'];
		$username = $_POST['username'];
		$db = new Database();
		$check = $db->checkUser($userId, $username);
		if($check){
			$result = $db->allData($userId);
			if($result){
				$array = array();
				$array["error"] = 0;
				$array["success"] = 1;
				$array["data"] = $result;
				echo json_encode($array);
			}
		}else
			error("Tài khoản không tồn tại");
	}else
		error("Dữ liệu gửi lên không đầy đủ.");
	
	function error($msg){
		$response["error"] = 1;
		$response["success"] = 0;
		$response["error_msg"] = $msg;
		echo json_encode($response);
	}
?>