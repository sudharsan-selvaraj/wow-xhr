var path = require("path");
var proxy = require('express-http-proxy');
var express = require('express');
const queryString = require('query-string');
var app = express();

var authorize = function (req, res, next) {
    if(req.headers['wow-xhr-token'] && req.headers["wow-xhr-token"] === "invalid") {
        console.log("Proxy found")
        return res.status(500).send({});
    } else {
        console.log("No proxy found")
        return next();
    }
}

app.post("/api/login", proxy('https://reqres.in/api/login'));
app.use('/node_modules', express.static(path.join(__dirname, '/node_modules/')))
app.use('/js', express.static(path.join(__dirname, '/public/js/')))
app.use('/angular', express.static(path.join(__dirname, '/public/angular')))
app.use("/api/delay", [authorize] ,proxy('http://jsonplaceholder.typicode.com', {
    proxyReqPathResolver: function (req) {
        return new Promise(function (resolve, reject) {
            var params = queryString.parse(req.url.split("?")[1] || "?a=a");
            if(params["delay"]) {
                console.log(params["delay"] +" ms delay")
                setTimeout(function () {
                    resolve(req.url);
                }, params["delay"]);
            } else {
                resolve(req.url)
            }
        });
    }
}));
app.use("/api", proxy('http://jsonplaceholder.typicode.com'));

app.get("/ng", function (req,res) {
    res.sendFile(path.join(__dirname, '/public/angular/index.html'));
})

app.get("/ng/login", function (req,res) {
    res.sendFile(path.join(__dirname, '/public/angular/login.html'));
})

app.listen(3000);