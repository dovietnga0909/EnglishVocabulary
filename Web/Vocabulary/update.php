<?php
	require_once('Database.php');
	if(isset($_POST['json'])){
		$json = $_POST['json'];
		$db = new Database();
		$result = $db->update($json);
		if($result)
			success();
		else
			error("Lỗi, không xác định.");
	}else{
		error("Thiếu nội dung.");
	}
	
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