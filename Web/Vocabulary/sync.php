<?php
	//json update
	$arr = array();
	$arr['table'] = "table";
	$arr['row'] = array();
	for($i=1; $i<=5; $i++){
		$ar = array();
		$ar['id'] = "".$i;
		$ar['sql'] = "name = 'acb ".$i."' and status = '1'";
		array_push($arr['row'], $ar);
	}
	$update = json_encode($arr);
	//json delete
	$arr = array();
	$arr['table'] = "table";
	$arr['row'] = array();
	for($i=1; $i<=5; $i++){
		$ar = array();
		$ar['id'] = "".$i;
		array_push($arr['row'], $ar);
	}
	$delete = json_encode($arr);
	//json insert
	$arr = array();
	$arr['table'] = "table";
	$arr['row'] = array();
	for($i=1; $i<=5; $i++){
		$ar = array();
		$ar['sql'] = "null, name ".$i.", 0";
		array_push($arr['row'], $ar);
	}
	$insert = json_encode($arr);
	//=====================================//
	$action = 'insert';
	if($action == "update"){
		update($update);
	}else if($action == "delete"){
		delete($delete);
	}else if($action == "insert"){
		insert($insert);
	}
	
	function update($json){
		$newarr = json_decode($json, true, 512);
		for($i=0; $i<count($newarr['row']); $i++){
			$item = $newarr['row'][$i];
			echo "UPDATE ".$newarr['table']." SET ".$item['sql']." WHERE id=".$item['id'].";<br/>";
		}
	}
	function delete($json){
		$newarr = json_decode($json, true, 512);
		for($i=0; $i<count($newarr['row']); $i++){
			$item = $newarr['row'][$i];
			echo "DELETE FROM ".$newarr['table']." WHERE id=".$item['id'].";<br/>";
		}
	}
	function insert($json){
		$newarr = json_decode($json, true, 512);
		for($i=0; $i<count($newarr['row']); $i++){
			$item = $newarr['row'][$i];
			echo "INSERT INTO ".$newarr['table']." VALUES(".$item['sql'].");<br/>";
		}
	}
?>