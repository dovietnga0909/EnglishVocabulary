<?php 
	class Database{
		private $hostname = "localhost";
		private $username = "root";
		private $password = "";
		private $dbname = "english_vocabulary";
		private $connect = NULL;
		private $result = NULL;
		
		public function Database(){
			$this->connect();
		}
		
		private function connect(){
			$this->connect = mysql_connect($this->hostname, $this->username, $this->password);
			mysql_select_db($this->dbname);
		}
		
		public function disconnect(){
			if($this->connect){
				mysql_close($this->connect);
			}
		}
		
		public function query($sql){
			$result = $this->result = mysql_query($sql);
			if($result)
				return true;
			else
				return false;
		}
		
		public function num_row(){
			if($this->result){
				$row = mysql_num_rows($this->result);
			}else{
				return 0;
			}
			return $row;
		}
		
		public function fetch_array(){
			if($this->result){
				$data = mysql_fetch_array($this->result);
			}else{
				return 0;
			}
			return $data;
		}
		
		public function insert_id(){
			return mysql_insert_id();
		}
		
		public function insert($json){
			$newarr = json_decode($json, true, 512);
			$result = array();
			for($i=0; $i<count($newarr); $i++){
				$item = $newarr[$i];
				$r = $this->query("INSERT INTO ".$item['table']." VALUES(".$item['sql'].");");
				//echo "INSERT INTO ".$item['table']." VALUES(".$item['sql'].");<br/>";
				if($r){
					$str = explode(",",$item['sql']);
					$idClient = str_replace(array("'"), "", $str[1]);
					$arr = array();
					$arr['table'] = $item['table'];
					$arr['id_client'] = $idClient;
					$arr['id_server'] = "".$this->insert_id();
					array_push($result, $arr);
				}
			}
			return $result;
		}
		
		public function update($json){
			$result = false;
			$newarr = json_decode($json, true, 512);
			for($i=0; $i<count($newarr); $i++){
				$result = true;
				$item = $newarr[$i];
				if($item['table'] == "categories"){
					$this->query("UPDATE ".$item['table']." SET ".$item['sql']." WHERE cate_id=".$item['cate_id'].";");
					//echo "UPDATE ".$item['table']." SET ".$item['sql']." WHERE cate_id=".$item['cate_id'].";<br/>";
				}else{
					$this->query("UPDATE ".$item['table']." SET ".$item['sql']." WHERE voca_id=".$item['voca_id'].";");
					//echo "UPDATE ".$item['table']." SET ".$item['sql']." WHERE voca_id=".$item['voca_id'].";<br/>";
				}
			}
			return $result;
		}
		
		public function delete($json){
			$result = false;
			$newarr = json_decode($json, true, 512);
			for($i=0; $i<count($newarr); $i++){
				$result = true;
				$item = $newarr[$i];
				$ids = explode(",", $item['sql']);
				if($item['table'] == "categories"){
					$this->query("DELETE FROM ".$item['table']." WHERE cate_id IN(".$item['sql'].");");
					//echo "DELETE FROM ".$item['table']." WHERE cate_id IN(".$item['sql'].");<br/>";
				}else{
					$this->query("DELETE FROM ".$item['table']." WHERE voca_id IN(".$item['sql'].");");
					//echo "DELETE FROM ".$item['table']." WHERE voca_id IN(".$item['sql'].");<br/>";
				}
			}
			return $result;
		}
		
		public function allData($id){
			$result = array();
			$result['categories'] = array();
			$result['vocabularies'] = array();
			$sql1 = "SELECT * FROM categories WHERE user_id = '".$id."';";
			$sql2 = "SELECT * FROM vocabularies WHERE user_id = '".$id."';";
			if($this->query($sql1)){
				while($row = $this->fetch_array()){
					$arr = array();
					$arr['cate_id'] = $row['cate_id'];
					$arr['id_clien'] = $row['id_clien'];
					$arr['name'] = $row['name'];
					array_push($result['categories'], $arr);
				}
				if($this->query($sql2)){
					while($row = $this->fetch_array()){
						$arr = array();
						$arr['voca_id'] = $row['voca_id'];
						$arr['id_clien'] = $row['id_clien'];
						$arr['cate_id'] = $row['cate_id'];
						$arr['english'] = $row['english'];
						$arr['vietnamese'] = $row['vietnamese'];
						array_push($result['vocabularies'], $arr);
					}
					return $result;
				}
			}
			return false;
		}
		
		public function checkUser($id, $username){
			$result = false;
			$sql = "SELECT count(*) as 'count' FROM users WHERE user_id='".$id."' and username='".$username."';";
			$r = $this->query($sql);
			if($r){
				$arr = $this->fetch_array();
				$count = $arr['count'];
				if($count>0)
					$result = true;
			}
			return $result;
		}
		
		public function checkUser2($username, $password){
			$result = false;
			$sql = "SELECT user_id, fullname FROM users WHERE username='".$username."' and password=md5('".$password."');";
			$r = $this->query($sql);
			if($r){
				$arr = $this->fetch_array();
				return $arr;
			}
			return $result;
		}
		
		public function checkUser3($username){
			$result = false;
			$sql = "SELECT count(*) as 'count' FROM users WHERE username='".$username."';";
			$r = $this->query($sql);
			if($r){
				$arr = $this->fetch_array();
				$count = $arr['count'];
				if($count>0)
					$result = true;
			}
			return $result;
		}
		
		public function register($username, $password, $fullname){
			$sql = "INSERT INTO users VALUES(null, '".$username."', md5('".$password."'), '".$fullname."')";
			$r = $this->query($sql);
			if($r){
				return true;
			}
			return false;
		}
	}
?>