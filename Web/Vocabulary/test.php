<?php
	$arr = array();
	$arr['insert'] = array();
	for($i=1; $i<=5; $i++){
		$ar = array();
		if($i<3){
			$ar['table'] = "categories";
			$ar['sql'] = "null,'".$i."','name ".$i."','".$i."'";
		}else{
			$ar['table'] = "vocabularies";
			$ar['sql'] = "null,'".$i."','".$i."','en ".$i."','vi ".$i."', '1'";
		}
		array_push($arr['insert'], $ar);
	}
	echo json_encode($arr['insert']);
	echo '<br/>';
	$arr = array();
	$arr['update'] = array();
	for($i=1; $i<=5; $i++){
		$ar = array();
		if($i<3){
			$ar['table'] = "categories";
			$ar['cate_id'] = $i;
			$ar['sql'] = "name = 'acb ".$i."' and status = '1'";
		}else{
			$ar['table'] = "vocabularies";
			$ar['voca_id'] = $i;
			$ar['sql'] = "name = 'acb ".$i."'";
		}
		array_push($arr['update'], $ar);
	}
	echo json_encode($arr['update']);
?>
<br/>
[{"table":"vocabularies","sql":"5,2"},{"table":"categories","sql":"3,2"}]
<br/>
Insert
<form action="insert.php" method="post">
    <input type="text" name="userId" placeholder="userId" />
    <input type="text" name="username" placeholder="username" />
	<input type="text" name="json" placeholder="json" />
    <input type="submit" value="Gửi" />
</form>
Update
<form action="update.php" method="post">
	<input type="text" name="userId" placeholder="userId" />
    <input type="text" name="username" placeholder="username" />
	<input type="text" name="json" placeholder="json" />
    <input type="submit" value="Gửi" />
</form>
Delete
<form action="delete.php" method="post">
	<input type="text" name="userId" placeholder="userId" />
    <input type="text" name="username" placeholder="username" />
	<input type="text" name="json" placeholder="json" />
    <input type="submit" value="Gửi" />
</form>
All
<form action="alldata.php" method="post">
	<input type="text" name="userId" placeholder="userId" />
    <input type="text" name="username" placeholder="username" />
    <input type="submit" value="Gửi" />
</form>
Login
<form action="login.php" method="post">
	<input type="text" name="username" placeholder="username" />
    <input type="text" name="password" placeholder="password" />
    <input type="submit" value="Gửi" />
</form>
Register
<form action="register.php" method="post">
	<input type="text" name="username" placeholder="username" />
    <input type="text" name="password" placeholder="password" />
    <input type="text" name="fullname" placeholder="fullname" />
    <input type="submit" value="Gửi" />
</form>