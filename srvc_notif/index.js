var express = require('express');
var mysql = require('mysql');
var bodyParser = require("body-parser")

var app = express(express.json());
const cors = require('cors');
app.use(cors());
app.use(bodyParser.json())

var urlParser = bodyParser.urlencoded({extended : false})

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


var sql = "CREATE TABLE IF NOT EXISTS alerts (name VARCHAR(255), la DECIMAL(18, 15), log DECIMAL(18,15))";
con.query(sql, function(err, result) {
    if (err) throw err;
    console.log("Table alerts created");
});

app.post('/alert', bodyParser.json(),function(req, res){
    const name = req.body.name;
    const lat = req.body.lat;
    const long = req.body.lon;
    var sql = "INSERT INTO alerts (name, la, log) VALUES ('" + name
        + "', "+lat+", "+long+");";
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
