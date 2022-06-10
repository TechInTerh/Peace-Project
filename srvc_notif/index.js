var express = require('express');
var mysql = require('mysql');

var app = express();

var con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "sqladmin",
  database: "alerts"
});

console.log("Connecting...");
con.connect(function(err) {
  if (err) return;
  console.log("Connected!");
  con.query("CREATE DATABASE alerts", function (err, result) {
    if (err) return;
    console.log("Database created");
  });
});


var sql = "CREATE TABLE alerts (name VARCHAR(255), la DECIMAL(12,10)"+
            ",lo DECIMAL(12,10))";
console.log(sql);
con.query(sql, function (err, result) {
    if (err) throw err;
    console.log("Table alerts created");
});

var sql = "INSERT INTO alerts (name, la, log) VALUES ('test', 1, 2)";
con.query(sql, function (err, result) {
    if (err) throw err;
    console.log("1 record inserted");
});
var sql = "INSERT INTO alerts (name, la, log) VALUES ('test2', 1, 2)";
con.query(sql, function (err, result) {
    if (err) throw err;
    console.log("1 record inserted");
});
var sql = "INSERT INTO alerts (name, la, log) VALUES ('test3', 1, 2)";
con.query(sql, function (err, result) {
    if (err) throw err;
    console.log("1 record inserted");
});

app.get('/', function(req,res){
    res.send("Hello world!");
});

app.listen(8080);
