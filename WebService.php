<?php

//Configure your DB Connections
$servername = "mysql7.000webhost.com"; // i hosted it in 000webhost.com
$username = "username";
$password = "password";

// Create connection
$conn= mysql_connect($servername,$username,$password);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
mysql_select_db('a6910159_db', $conn);
if($_GET['q']=="login"){
	$mobile=$_GET["mobile"];
	$pass=$_GET["pass"];
	$sql="select uid from userData where mobile='".$mobile."' and pass='".$pass."'";
	

	
	$retval = mysql_query( $sql, $conn );
	if(mysql_num_rows($retval)>0)
		$status="Success";
	else
		$status="Fail";
	$array=array("status"=>$status);
	header('Content-Type: application/json');
	echo json_encode($array);	
}
if($_GET['q']=="createUser"){
	$name=$_GET['name'];
	$dob=$_GET['dob'];
	$blood=$_GET['blood'];
	$mobile=$_GET['mobile'];
	$pass=$_GET['pass'];
	$long=$_GET['long'];
	$lat=$_GET['lat'];
        if($blood[1]=="P")
           $blood[1]="+";
        else
           $blood[1]="-";
	$sql="insert into userData(name,dob,blood,loclong,loclat,mobile,pass) values('".$name."','".$dob."','".$blood."','".$long."','".$lat."','".$mobile."','".$pass."')";
        
	$retval = mysql_query( $sql, $conn );
	if (!$retval) {	
   		$status="Fail";
	}
	else
		$status="Success";
	$array=array("status"=>$status);
	header('Content-Type: application/json');
	echo json_encode($array);	
	
}
if($_GET['q']=="createPost"){
        $uid=$_GET['uid'];
        $desc=$_GET['desc'];
	$head=$_GET['head'];
	$blood=$_GET['blood'];
	$mobile=$_GET['mobile'];
	$urgency=$_GET['urgency'];
	$long=$_GET['long'];
	$lat=$_GET['lat'];
        $date=date("Y/m/d") ;
        if($blood[1]=="P")
           $blood[1]="+";
        else
           $blood[1]="-";
        

        $sql="insert into postData(uid,heading,descPost,blood,loclong,loclat,mobile,Postdate,urgency) values('".$uid."','".$head."','".$desc."','".$blood."','".$long."','".$lat."','".$mobile."','".$date."','".$urgency."')";
		
	$retval = mysql_query( $sql, $conn );
	if (!$retval) {	
   		$status="Fail";
	}
	else
		$status="Success";
	$array=array("status"=>$status);
	header('Content-Type: application/json');
	echo json_encode($array);	
 
}
if($_GET['q']=="getPostOfUser"){
       if(isset($_GET['mobile'])){
       $uid=$_GET['mobile'];
       $sql="select * from postData where mobile=".$uid;
}
if(isset($_GET['blood'])){ 
$blood=$_GET['blood'];
 if($blood[1]=="P")
           $blood[1]="+";
        else
           $blood[1]="-";
       $sql="select * from postData where blood like CONVERT( _utf8 '".$blood."'
USING latin1 ) 
COLLATE latin1_general_ci";
}      
       $retval = mysql_query( $sql, $conn );
       if (!$retval) {	
   		$status="Fail";
       }
       else{
               $array=array("count"=>0);
               while($row = mysql_fetch_array($retval))
{
   
   $obj=array("pid"=>$row[0],"uid"=>$row[1],"heading"=>$row[2],"descPost"=>$row[3],"postDate"=>$row[4],"mobile"=>$row[5],"blood"=>$row[6],"loclong"=>$row[7],"loclat"=>$row[8],"urgency"=>$row[9]);
   array_push($array,$obj);
   $array["count"]++;
} 

       }
      
       header('Content-Type: application/json');
       echo json_encode($array);

}

/*$retval = mysql_query( $sql, $conn );
if(! $retval )
{
  die('Could not get data: ' . mysql_error());
}
while($row = mysql_fetch_array($retval, MYSQL_ASSOC))
{
    echo "Tutorial ID :{$row['tutorial_id']}  <br> ".
         "Title: {$row['tutorial_title']} <br> ".
         "Author: {$row['tutorial_author']} <br> ".
         "Submission Date : {$row['submission_date']} <br> ".
         "--------------------------------<br>";
} 

echo "Fetched data successfully\n";
*/
mysql_close($conn);

?> 								