var express = require('express');
var mysql = require('mysql');

var app = express(express.json());

var con = mysql.createConnection({
  host: "sql-alerts",
  user: "root",
  password: "sqladmin",
  database: "alerts"
});

console.log("Connecting...");
con.connect(function(err) {
  if (err) throw err;
  console.log("Connected!");
});


var sql = "CREATE TABLE IF NOT EXISTS alerts (name VARCHAR(255), la DECIMAL(12,10)"+
            ",log DECIMAL(12,10))";
con.query(sql, function(err, result) {
    if (err) throw err;
    console.log("Table alerts created");
});

app.post('/alert', function(req,res){
    const name = req.body.name;
    const lat = req.body.lat;
    const long = req.body.log;
    var sql = "INSERT INTO alerts (name, la, log) VALUES ('" + name
        + "', "+lat+", "+lon+")";
    con.query(sql, function(err, result) {
        if (err) throw err;
        console.log("1 record inserted");
    });

    res.send(req.body);
});

app.get('/alerts', function(req, res){
    con.query("SELECT * FROM alerts", function(err, result, fields) {
        if (err) throw err;
        res.send(result)
    });
});

app.listen(8080);
